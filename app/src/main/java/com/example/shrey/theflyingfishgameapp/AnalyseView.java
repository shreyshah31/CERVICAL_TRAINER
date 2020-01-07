package com.example.shrey.theflyingfishgameapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class AnalyseView extends View {

    public BluetoothSocket socket = BluetoothActivity.socket;

    private final String DEVICE_ADDRESS = "98:D3:51:F5:E4:A0";// 00:18:E4:40:00:06-roborehab ka hai //98:D3:51:F5:E4:A0-yeh wala mere bluetooth ka hai
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private InputStream inputStream;
    private OutputStream outputStream;
    boolean deviceConnected;
    private int ekkbaar=0;
    byte buffer[];
    private BluetoothDevice device;
    boolean stopThread;
    private String stringnewd,stringd,datad,thodd,thoddy;
    private int slen,vald,valdy;
    private int limit,limity,once=0,once1=0,x,yp;

    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    private Bitmap dart,upd,downd,start,stop,reset;

    private Paint scorePaint1 = new Paint();
    private Paint scorePaint2 = new Paint();
    private Paint scorePaint3 = new Paint();

    private Paint redPaint1 = new Paint();
    private Paint redPaint2 = new Paint();
    private Paint blackPaint = new Paint();

    private int datamila;
    private int xtarget,ytarget,xprint,yprint;
    private int touch;
    private int analyse_start=0,analyse_stop=0;
    private int b1=0;
    private int previous_valuex=0,previous_valuey=0,current_valuex=0,current_valuey=0,analyse_valuex=0,analyse_valuey=0;

    private double canvasHeight,canvasWidth;
    private double dartHeigth,buttonWidth,buttonHeigth,buttonWidth1,buttonWidth2,textSize,textSize2,buttonWidthSub,buttonWidthSub2;

    Rect rect,rect1,rect2,rect3,rect4;

    public AnalyseView(Context context) {
        super(context);
        dart=BitmapFactory.decodeResource(getResources(), R.drawable.dart_paint);
        start=BitmapFactory.decodeResource(getResources(),R.drawable.start);
        stop=BitmapFactory.decodeResource(getResources(),R.drawable.stop);
        reset=BitmapFactory.decodeResource(getResources(),R.drawable.reset);



        //rect2=new Rect(0,0,1450,1400);
        datamila=1;
        redPaint1.setColor(Color.RED);
        redPaint1.setAntiAlias(false);

        redPaint2.setColor(Color.BLUE);
        redPaint2.setAntiAlias(false);

        blackPaint.setColor(Color.BLACK);
        blackPaint.setAntiAlias(false);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        dartHeigth=canvasHeight/1.65;
        buttonWidth=canvasWidth/4;
        textSize=canvasWidth/18;
        textSize2=canvasWidth/12;
        buttonWidth1=canvasWidth/1.625;
        buttonWidth2=canvasWidth/1;
        buttonHeigth=canvasHeight/1.35;
        buttonWidthSub=canvasWidth/4;
        buttonWidthSub2=canvasWidth/5;


        scorePaint1.setColor(Color.BLACK);
        scorePaint1.setTextSize((int)textSize2);
        scorePaint1.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint1.setAntiAlias(true);

        scorePaint2.setColor(Color.BLACK);
        scorePaint2.setTextSize((int)textSize);
        scorePaint2.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint2.setAntiAlias(true);

        scorePaint3.setColor(Color.BLACK);
        scorePaint3.setTextSize((int)textSize);
        scorePaint3.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint3.setAntiAlias(true);


        rect2=new Rect(0,0,(int)canvasWidth,(int)dartHeigth);

        rect=new Rect((int)buttonWidth-(int)buttonWidthSub,(int)canvasHeight-(int)buttonWidthSub2,(int)buttonWidth,(int)canvasHeight);
        rect1=new Rect((int)buttonWidth2-(int)buttonWidthSub,(int)canvasHeight-(int)buttonWidthSub2,(int)buttonWidth2,(int)canvasHeight);
        rect3=new Rect((int)buttonWidth1-(int)buttonWidthSub,(int)canvasHeight-(int)buttonWidthSub2,(int)buttonWidth1,(int)canvasHeight);

        rect4=new Rect((int)buttonWidth1-(int)buttonWidthSub,(int)canvasHeight-(int)(buttonWidthSub*10/4),(int)buttonWidth1,(int)canvasHeight-(int)(buttonWidthSub*7/4));

        if(b1>=7) {
            touch=1;
            b1=0;
        }
        b1++;


        canvas.drawBitmap(dart,null,rect2,null);

        canvas.drawBitmap(start,null,rect,null);
        canvas.drawBitmap(stop,null,rect1,null);
        canvas.drawBitmap(reset,null,rect3,null);
        canvas.drawBitmap(reset,null,rect4,null);



        setUiEnabled(true);

        datad=stringd;
        try{
            slen=datad.length();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
                  /*  int index1 = datad.indexOf("z");
                    int index2 = datad.indexOf("s");
                    thodd = datad.substring(index1, index2);
                     int slen1=thodd.length();
                     thodd1=thodd.substring(1,slen1);
                     */
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            // vald=Integer.parseInt(thodd1);

            if (datad == "y") {

            } else {
                if (slen >= 3) {
                    int index1 = datad.indexOf("y");
                    thodd = datad.substring(index1 - 3, index1);
                    vald = Integer.parseInt(thodd);
                    vald = vald - 360;
                }
            }

            if(datad=="p")
            {

            }
            else{
                if (slen >= 3) {
                    int index11 = datad.indexOf("p");
                    thoddy = datad.substring(index11 - 3, index11);
                    valdy = Integer.parseInt(thoddy);
                    valdy = valdy - 360;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        if (vald>limit-120&&vald<limit+120 || once<100) {
            limit = vald;
            once++;
            x= (int) map(vald,0,360,-180,180);
            x=x*6;
        }

        if (valdy>limity-120&&valdy<limity+120 || once1<100) {
            limity = valdy;
            once1++;
            yp= (int) map(valdy,0,360,-180,180);
            yp=yp*6;
        }


        //ANALYSE KARNE KA LOGIC
        if(analyse_start==1 && analyse_stop==0)
        {
           current_valuex=x/6;
           current_valuey=yp/6;
        }

        if(analyse_start==0 && analyse_stop==0)
        {
            previous_valuex=x/6;
            previous_valuey=yp/6;
        }


        if(analyse_start==1 && analyse_stop==1)
        {
            if(previous_valuex>current_valuex) {
                analyse_valuex = previous_valuex - current_valuex;
            }
            else
            {
                analyse_valuex = -previous_valuex +current_valuex;
            }
            if(previous_valuey>current_valuey) {
                analyse_valuey = previous_valuey - current_valuey;
            }
            else {
                analyse_valuey = -previous_valuey+current_valuey;
            }
        }



        xprint=x/6;
        yprint=yp/6;

        //MOVING RED CIRCLE
        canvas.drawCircle(725+x,700-yp, 30, redPaint1);

        //ALL TEXT VIEW
        canvas.drawText("X-AXIS : "+xprint, (int)buttonWidth-(int)buttonWidthSub2, (int)canvasHeight-(int)(buttonWidthSub*6/4), scorePaint2);
        canvas.drawText("Y-AXIS : "+yprint, (int)buttonWidth-(int)buttonWidthSub2, (int)canvasHeight-(int)(buttonWidthSub*1.2), scorePaint2);
        canvas.drawText("ANALYSED X: "+analyse_valuex, (int)buttonWidth1-(int)(buttonWidthSub2*0.5), (int)canvasHeight-(int)(buttonWidthSub*6/4), scorePaint2);
        canvas.drawText("ANALYSED Y: "+analyse_valuey, (int)buttonWidth1-(int)(buttonWidthSub2*0.5), (int)canvasHeight-(int)(buttonWidthSub*1.2), scorePaint2);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchx=event.getX();
        float touchy=event.getY();
        if(touch==1) {
            if (touchx >= (int)buttonWidth-(int)buttonWidthSub && touchx <= (int)buttonWidth && touchy >= (int)canvasHeight-(int)buttonWidthSub2 && touchy <= (int)canvasHeight) {
                touch = 0;
                analyse_start=1;
            }

            if (touchx >= 1450 && touchx <= 1650 && touchy >= 1950 && touchy <= 2250) {
                touch = 0;
            }

            if (touchx >= buttonWidth2-(int)buttonWidthSub && touchx <= buttonWidth2 && touchy >= (int)canvasHeight-(int)buttonWidthSub2 && touchy <= (int)canvasHeight) {
                touch = 0;
                analyse_stop=1;

            }
            if (touchx >= buttonWidth1-(int)buttonWidthSub && touchx <= buttonWidth1 && touchy >= (int)canvasHeight-(int)buttonWidthSub2 && touchy <= (int)canvasHeight) {
                touch = 0;
                analyse_stop=0;
                analyse_start=0;
                analyse_valuex=0;
                analyse_valuey=0;

            }
        }
        return true;
    }


    long map(long x, long in_min, long in_max, long out_min, long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }



    public void setUiEnabled(boolean bool)
    {
        if(ekkbaar==0) {
            Start();
        }

    }


    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            Toast.makeText(getContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

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
            Toast.makeText(getContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
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


    public void Start()
    {
        if(BTinit())
        {
            if(BTconnect())
            {
                ekkbaar=1;
                setUiEnabled(true);
                deviceConnected = true;
                beginListenForData();
                Toast.makeText(getContext(),"COnnection Opened \n",Toast.LENGTH_SHORT).show();
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
//                            stringd=new String(rawBytes,"UTF-8");
//                            stringnewd=stringd;
//                            handler.post(new Runnable()
//                            {
//
//                                public void run()
//
//                                {
//                                }
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
                                                stringd=data;
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

    public void datasend(String s1)
    {
        String string=s1;
        //string.concat("\n");
        try
        {
            outputStream.write(string.getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void ConClose() throws IOException
    {

        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
        setUiEnabled(false);
        deviceConnected=false;
    }
}
