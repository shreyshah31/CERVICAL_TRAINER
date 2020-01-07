package com.example.shrey.theflyingfishgameapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    private final String DEVICE_ADDRESS="98:D3:51:F5:E4:A0"; //98:D3:51:F5:E4:A0-yeh wala mere bluetooth ka hai// 00:18:E4:40:00:06  ,00:13:EF:00:91:CD -roborehab ka hai
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    public static BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button startButton,sendButton,clearButton,stopButton,nextpage;
    TextView textView;
    EditText editText;
    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPostion;
    boolean stopThread;

    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        startButton = (Button) findViewById(R.id.buttonStart);
        sendButton = (Button) findViewById(R.id.buttonSend);
        clearButton = (Button) findViewById(R.id.buttonClear);
        stopButton = (Button) findViewById(R.id.buttonStop);
        nextpage = (Button) findViewById(R.id.nextpage);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        setUiEnabled(false);

        nextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(BluetoothActivity.this,DatabaseActivity.class);
                startActivity(i);
            }
        });
    }


    public void setUiEnabled(boolean bool)

    {
        startButton.setEnabled(!bool);
        sendButton.setEnabled(bool);
        stopButton.setEnabled(bool);
        textView.setEnabled(bool);

    }


    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }
    public boolean BTconnect()
    {
        boolean connected=true;
        try
        {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try
            {
                outputStream=socket.getOutputStream();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                inputStream=socket.getInputStream();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return connected;
    }
    public void onClickStart(View view)
    {
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;
                beginListenForData();
                textView.append("\nConnection Opened!\n");
            }
        }
    }
    void beginListenForData()
    {

//        final Handler handler = new Handler();
//        stopThread = false;
//        buffer = new byte[1024];
//        Thread thread  = new Thread(new Runnable()
//
//        {
//
//            public void run()
//
//            {
//                while(!Thread.currentThread().isInterrupted() && !stopThread)
//                {
//                    try
//                    {
//                        int byteCount = inputStream.available();
//                        if(byteCount > 0)
//                        {
//                            byte[] rawBytes = new byte[byteCount];
//                            inputStream.read(rawBytes);
//                            final String string=new String(rawBytes,"UTF-8");
//                            handler.post(new Runnable()
//                            {
//
//                                public void run()
//
//                                {
//                                    textView.append(string);
//                                }
//
//
//                            });
//                        }
//                    }
//                    catch (IOException ex)
//                    {
//                        stopThread = true;
//                    }
//                }
//
//            }
//        });
//        thread.start();
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = inputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                textView.append(data);
                                                //Log.d(TAG, "inputstream:" + data);
                                                //myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public void onClickSend(View view)
    {
        String string = editText.getText().toString();
        //string.concat("\n");
        try
        {
            outputStream.write(string.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        textView.append("\nSent Data:"+string+"\n");

    }
    public void onClickStop(View view) throws IOException
    {

        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
        setUiEnabled(false);
        deviceConnected=false;
        textView.append("\nConnection Closed!\n");
    }
    public void onClickClear(View view)
    {
        textView.setText("");
    }
}
