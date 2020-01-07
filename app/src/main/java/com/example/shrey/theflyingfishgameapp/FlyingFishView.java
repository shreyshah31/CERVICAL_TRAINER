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
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.spec.ECField;
import java.util.Set;
import java.util.UUID;

public class FlyingFishView extends View {

    private Animation animation = new Animation();

    public BluetoothSocket socket = BluetoothActivity.socket;

    private final String DEVICE_ADDRESS = "98:D3:51:F5:E4:A0";// 00:18:E4:40:00:06-roborehab ka hai //98:D3:51:F5:E4:A0-yeh wala mere bluetooth ka hai
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private InputStream inputStream;
    private OutputStream outputStream;
    boolean deviceConnected;
    int ekkbaar = 0;
    byte buffer[];
    private BluetoothDevice device;
    boolean stopThread;
    String stringnew;
    private Bitmap res;

    private int fishX = 10, fishY, canvasWidth, canvasHeight, fishSpeed;
    private Bitmap fish[] = new Bitmap[2];

    private Bitmap backgroundImage;

    private Paint scorePaint = new Paint();
    private Bitmap life[] = new Bitmap[2];
    private boolean touch = false;
    private int yellowX, yellowY, yellowSpeed = 16;
    private String data;
    private int blueY = 0;
    private String thod;
    long data1 = 0;
    int sum=0;

    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    private int val = 0, limit;
    private int datamila = 1;
    private String thod1;
    private int z = 0;
    private String string;
    int slen=0;
    private int datas=1;

    private Paint yellowPaint = new Paint();
    private int score, lifecounter;
    private int once = 0;


    private int greenX, greenY, greenSpeed = 20;
    private Paint greenPaint = new Paint();

    private int redX, redX1, redY, redY1, redSpeed = 18;
    private Paint redPaint = new Paint();
    private int x_an,y_an,row_an=0;
    Bitmap[] image = new Bitmap[25];





    public FlyingFishView(Context context) {
        super(context);

            res = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);


        for (int i = 0; i < 25; i++) {

            if (i % 5 == 0 && i > 0) {row_an++;}

            image[i] = Bitmap.createBitmap(res, (i - (5 * row_an)) * 204, row_an* 204, 204, 204);
        }


        /* for (int i = 0; i < 25; i++) {


            if (i % 5 == 0 && i > 0) row_an++;

            image[i] = Bitmap.createBitmap(res, (i - (5 * row_an)) * 100, row_an* 100, 100, 100);

        }
        */

        /*Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion),
                200, 200- 30, 100, 100, 25);*/

        fish[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fish1);
        fish[1] = BitmapFactory.decodeResource(getResources(), R.drawable.fish2);
        fishY = 50;
        score = 0;
        lifecounter = 3;
        once = 1;

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.hearts);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_grey);

       /* for(int i=0;i<25;i++)
        {
            if (i % 5 == 0 && i > 0) row_an++;
            image[i] = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.explosion),
                    (i - (5 * row_an)) * width, row_an* height, width, height);
        }*/
        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(70);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(false);

        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);

        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);



    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();


        canvas.drawBitmap(backgroundImage, 0, 0, null);



        setUiEnabled(true);
//        try {
//                for (int r = 0; r < 2; r++) {
//                    datasend("t");
//                }
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

       /* Thread t=new Thread() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);

                    datasend("t");

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



            }
        };
        t.start();*/

        data=string;
        try{
            slen=data.length();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try {
          /*  int index1 = data.indexOf("z");
            int index2 = data.indexOf("s");
            thod = data.substring(index1, index2);
            int slen1=thod.length();
            thod1=thod.substring(1,slen1);*/


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //val = Integer.parseInt(thod1);
            if (data == "p") {

            }
            else {
                if (slen >= 3)
                {
                    int index1 = data.indexOf("p");
                    thod = data.substring(index1 - 3, index1);
                    val = Integer.parseInt(thod);
                    val = val - 360;
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        if (val>limit-90&&val<limit+90 || once<100) {
            fishY = val*6;    // yeh fish ka up down movement ke liye hai
            limit = val;
            once++;
        }

        for(int j=0;j<17;j++)
        {
            sum=sum+val;
        }
        val=sum/17;
        sum=0;



        // fish ka movement and image formation hai
        int minFishY = fish[0].getHeight();
        int maxFishY = canvasHeight - fish[0].getHeight() * 2;

        //fishY = fishY + fishSpeed;

        if (fishY < minFishY) {
            fishY = minFishY;
        }
        if (fishY > maxFishY) {
            fishY = maxFishY;
        }
        fishSpeed = fishSpeed + 2;
        if (touch) {
            canvas.drawBitmap(fish[1], fishX, fishY, null);
            touch = false;
        } else {
            canvas.drawBitmap(fish[0], fishX, fishY, null);
        }

        canvas.drawBitmap(fish[0], fishX, fishY, null);



        //YELLOW BALL KA CODE IDDHAR SE HAI

        yellowX = yellowX - yellowSpeed;

        if (hitchecker(yellowX, yellowY)) {
            score = score + 10;
            yellowX = -100;
        }

        if (yellowX < 0) {
            yellowX = canvasWidth + 21;
            yellowY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }
        canvas.drawCircle(yellowX, yellowY, 50, yellowPaint);


        //GRENN BALL KA IDDHAR SE CODE HAI

        greenX = greenX - greenSpeed;

        if (hitchecker(greenX, greenY)) {
            score = score + 20;
            greenX = -100;
        }

        if (greenX < 0) {
            greenX = canvasWidth + 21;
            greenY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;
        }
        // greenPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(greenX, greenY, 30, greenPaint);

        //RED BALL KA IDDHAR SE CODE HAI

        if (score > 150) {
            redSpeed = 22;
        }
        redX = redX - redSpeed;
        // redY=redY-redSpeed;
        if (hitchecker(redX, redY)) {
            score = score - 30;
            redX = -100;
            lifecounter--;




            for(int i=0;i<25;i++){

                canvas.drawBitmap(image[i], redX+204, redY, null);

            }



            if (score < 0) {
                score = 0;
            }
            if (lifecounter == 0) {
                Toast.makeText(getContext(), "GAME OVER", Toast.LENGTH_SHORT).show();

                Intent GameOver = new Intent(getContext(), GameOverActivity.class);
                GameOver.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                GameOver.putExtra("score", score);
                getContext().startActivity(GameOver);
            }
        }

        if (redX < 0) {
            redX = canvasWidth + 21;
            redY = (int) Math.floor(Math.random() * (maxFishY - minFishY)) + minFishY;

        }
        canvas.drawRect(redX, redY, redX - 100, redY - 100, redPaint);


        // yeh score ke liye hai
        try {
            canvas.drawText("Score " + score, 20, 60, scorePaint);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // iddhar life ke liye hai

        for (int i = 0; i < 3; i++) {
            int x = (int) (580 + life[0].getWidth() * i * 1.5);
            int y = 20;

            if (i < lifecounter) {
                canvas.drawBitmap(life[0], x, y, null);
            } else {
                canvas.drawBitmap(life[1], x, y, null);
            }
        }


    }

    public void Explosion(Bitmap res, int x_an, int y_an, int width, int height, int numFrames) {
        this.x_an = x_an;
        this.y_an = y_an;

        for (int i = 0; i < image.length; i++) {

            if (i % 5 == 0 && i > 0) row_an++;
            image[i] = Bitmap.createBitmap(res, (i - (5 * row_an)) * width, row_an* height, width, height);

        }
    }









    public boolean hitchecker(int x, int y) {
        if (fishX < x && x < (fishX + fish[0].getWidth()) && fishY < y && y < (fishY + fish[0].getHeight())) {
            touch=true;
            return true;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            touch = true;
            fishSpeed = -22;
        }
        return true;
    }







// IDDHAR SE BLUETOOTH CHALU HOTA HAI

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
//                            string=new String(rawBytes,"UTF-8");
//                            stringnew=string;
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
                                                string=data;
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
