package com.example.shrey.theflyingfishgameapp;

import android.animation.ObjectAnimator;
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
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class dartView extends View
{
    public BluetoothSocket socket = BluetoothActivity.socket;
    public static databasehelper mDatabaseHelper = DatabaseActivity.mDatabaseHelper;



    private double canvasHeight,canvasWidth;

    private final String DEVICE_ADDRESS = "98:D3:51:F5:E4:A0";// 00:18:E4:40:00:06-roborehab ka hai //98:D3:51:F5:E4:A0-yeh wala mere bluetooth ka hai
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private InputStream inputStream;
    private OutputStream outputStream;
    boolean deviceConnected;
    private int ekkbaar=0;
    byte buffer[];
    private BluetoothDevice device;
    boolean stopThread;
    private String stringnewd;
    private String datad;
    private int vald=0,valdy=0;
    private String thodd;
    private String thoddy;
    private int once,xprint;
    private int datamila=0;
    String laststr;
    private int o=0;
    private int slen=0;
    int z=0;
    String stringd;
    String thodd1;
    private int range=3;
    private int x2=0;

    private Bitmap dart;
    private Bitmap upd,downd;
    private Bitmap yaw_but,roll_but,pitch_but;

    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    private int score,target=100,level=0;
    private float xtarget=0,ytarget=0;
    private int limity=0,yp,yprint;
    private int y1=0,b1=0;
    private int yaw_bt=0,roll_bt=0,pitch_bt=0;
    String thodr;
    int valdr=0;

    private Paint redPaint1 = new Paint();
    private Paint redPaint2 = new Paint();
    private Paint blackPaint = new Paint();

    private int x=0,y=0,x1=0,m=0,t=1,touch,b=0,q=0;
    private int limit,limitr;
    private int once1=0,oncer=0;




    private Paint scorePaint1 = new Paint();
    private Paint scorePaint2 = new Paint();
    private Paint scorePaint3 = new Paint();

    private int dx=0,dy,dz,dx1=0,datacontrol=0,datacontrol1=0; // used for database updatedation

    Rect rect,rect1,rect2,rect3,rect4,rect5;


    private double dartHeigth,buttonWidth,buttonHeigth,buttonWidth1,buttonWidth2,textSize,textSize2,buttonWidthSub,buttonWidthSub2;


    public dartView(Context context) {

        super(context);
        once=0;
        dart=BitmapFactory.decodeResource(getResources(), R.drawable.dart_paint);
        upd=BitmapFactory.decodeResource(getResources(),R.drawable.plus);
        downd=BitmapFactory.decodeResource(getResources(),R.drawable.minus);

        yaw_but=BitmapFactory.decodeResource(getResources(),R.drawable.yaw);
        pitch_but=BitmapFactory.decodeResource(getResources(),R.drawable.pitch);
        roll_but=BitmapFactory.decodeResource(getResources(),R.drawable.roll);


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
        textSize=canvasWidth/16;
        textSize2=canvasWidth/12;
        buttonWidth1=canvasWidth/1.625;
        buttonWidth2=canvasWidth/1;
        buttonHeigth=canvasHeight/1.35;
        buttonWidthSub=canvasWidth/6;
        buttonWidthSub2=canvasWidth/5;

        scorePaint3.setColor(Color.BLACK);
        scorePaint3.setTextSize((int)textSize);
        scorePaint3.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint3.setAntiAlias(true);

        scorePaint2.setColor(Color.BLACK);
        scorePaint2.setTextSize((int)textSize);
        scorePaint2.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint2.setAntiAlias(true);

        scorePaint1.setColor(Color.BLACK);
        scorePaint1.setTextSize((int)textSize2);
        scorePaint1.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint1.setAntiAlias(true);


        rect=new Rect((int)buttonWidth,(int)canvasHeight-(int)buttonWidthSub,(int)buttonWidth+(int)buttonWidthSub,(int)canvasHeight);
        rect1=new Rect((int)buttonWidth1,(int)canvasHeight-(int)buttonWidthSub,(int)buttonWidth1+(int)buttonWidthSub,(int)canvasHeight);
        rect2=new Rect(0,0,(int)canvasWidth,(int)dartHeigth);   //rect2=new Rect(0,0,1450,1400);

        rect3=new Rect((int)buttonWidth-(int)buttonWidthSub2,(int)dartHeigth+20,(int)buttonWidth,(int)buttonHeigth);
        rect4=new Rect((int)buttonWidth1-(int)buttonWidthSub2,(int)dartHeigth+20,(int)buttonWidth1,(int)buttonHeigth);
        rect5=new Rect((int)buttonWidth2-(int)buttonWidthSub2,(int)dartHeigth+20,(int)buttonWidth2,(int)buttonHeigth);

      /*for(int i=0;i<10;i++) {
            blackPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(700, 500, 15 *i*3, blackPaint);
        }*/
        if(range>=12)
        {
            range=12;
        }
        if(range<=3)
        {
            range=3;
        }

        canvas.drawBitmap(dart,null,rect2,null);

        canvas.drawBitmap(upd,null,rect,null);
        canvas.drawBitmap(downd,null,rect1,null);

        canvas.drawBitmap(yaw_but,null,rect3,null);
        canvas.drawBitmap(pitch_but,null,rect4,null);
        canvas.drawBitmap(roll_but,null,rect5,null);



        setUiEnabled(true);
    /* try {
             for (int r = 0; r < 2; r++) {
                 datasend("t");
             }

         }
       catch (Exception e)
       {
           e.printStackTrace();
       }*/

        if(b1>=7) {
            touch=1;
            b1=0;
        }
        b1++;
       /*  Thread t=new Thread() {
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


        datad=stringd;
            try{
             slen=datad.length();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

//            data1();
//            data2();
//            data3();

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

        try {
            // vald=Integer.parseInt(thodd1);
            if (yaw_bt == 1) {
                yp = 0;
                if (datad == "y") {

                } else {
                    if (slen >= 3) {
                        int index1 = datad.indexOf("y");
                        thodd = datad.substring(index1 - 3, index1);
                        vald = Integer.parseInt(thodd);
                        vald = vald - 360;

                        dx1=vald;
                        if(dx1>dx)
                        {
                            dx=dx1;
                        }

                    }
                }
            }
            if (pitch_bt == 1) {
                x = 0;
                if (datad == "p") {

                } else {
                    if (slen >= 3) {
                        int index11 = datad.indexOf("p");
                        thoddy = datad.substring(index11 - 3, index11);
                        valdy = Integer.parseInt(thoddy);
                        valdy = valdy - 360;
                    }
                }
            }

            if (roll_bt == 1) {
                yp = 0;
                if (datad == "r") {

                } else {
                    if (slen >= 3) {
                        int index12 = datad.indexOf("r");
                        thodr = datad.substring(index12 - 3, index12);
                        valdr = Integer.parseInt(thodr);
                        valdr = valdr - 360;
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        q++;

        if(yaw_bt==1) {
            if (vald > limit - 120 && vald < limit + 120 || once < 100) {
                limit = vald;
                once++;
                x = (int) map(vald, 0, 360, -180, 180);
                x = x * 6;
            }
        }

        if(pitch_bt==1) {
            if (valdy > limity - 120 && valdy < limity + 120 || once1 < 100) {
                limity = valdy;
                once1++;
                yp = (int) map(valdy, 0, 360, -180, 180);
                yp = yp * 6;
            }
        }

            if(roll_bt==1)
            {
                if (valdr > limitr - 120 && valdr < limitr + 120 || oncer < 100) {
                    limitr = valdr;
                    oncer++;
                    x = (int) map(valdr, 0, 360, -180, 180);
                    x = x * 6;
                }
            }


        //y=400;

        //canvas.drawBitmap(dart,350,600,null);
        if(yaw_bt==1||pitch_bt==1||roll_bt==1) {
            if (x <= x1 + 12 && x >= x1 - 12 && yp <= y1 + 12 && yp >= y1 - 12) {
                if (yaw_bt == 1 || roll_bt == 1) {
                    y1 = 0;
                    x1 = (int) Math.floor(Math.random() * 50 * range);
                }
                if (pitch_bt == 1) {
                    x1 = 0;
                    y1 = (int) Math.floor(Math.random() * 50 * range);
                }
                score = score + 10;
                b++;

                if (b % 2 == 0) {
                    if (yaw_bt == 1 || roll_bt == 1) {
                        x1 = -x1;
                    }
                    if (pitch_bt == 1) {
                        y1 = -y1;
                    }
                }

                if (score == target) {
                    target = target + 50;
                    level++;


                        Toast.makeText(getContext(), "LEVEL " + level, Toast.LENGTH_SHORT).show();
                }
            }
        }

      while(xtarget!=x)
      {
          if(x>xtarget)
          {
              xtarget=xtarget+0.50f;
          }
          else if(x<xtarget)
          {
              xtarget=xtarget-0.50f;
          }
      }

        while(ytarget!=yp)
        {
            if(yp>ytarget)
            {
                ytarget= ytarget+0.50f;
            }
            else if(yp<ytarget)
            {
                ytarget= ytarget-0.50f;
            }
        }

      /*  if(xtarget>dx)
        {
            dx=(int)xtarget;
        }
        if(ytarget>dy)
        {
            dy=(int)ytarget;
        }*/




       xprint=(int)xtarget/6;
       yprint=(int)ytarget/6;
        //redPaint2.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(725+x1,700-y1, 50, redPaint2); //TARGET BLUE CIRCLE  canvas.drawCircle(725+x1,300+y, 50, redPaint2);

        canvas.drawCircle(725+xtarget,700-ytarget, 30, redPaint1);  //MOVING RED CIRCLE

        canvas.drawText("RANGE ", (int)buttonWidth+(int)buttonWidthSub, (int)canvasHeight-(int)buttonWidthSub, scorePaint2);
        canvas.drawText(""+ range*10, (int)buttonWidth+(int)buttonWidthSub2, (int)canvasHeight, scorePaint1);  //range*10
        canvas.drawText("X-AXIS : "+xprint, (int)buttonWidth-(int)buttonWidthSub2, (int)canvasHeight-(int)(buttonWidthSub2*6/4), scorePaint2);
        canvas.drawText("Y-AXIS : "+yprint, (int)buttonWidth-(int)buttonWidthSub2, (int)canvasHeight-(int)(buttonWidthSub2*1.2), scorePaint2);
        canvas.drawText("TARGET : "+target,(int)buttonWidth1,(int)canvasHeight-(int)(buttonWidthSub2*6/4),scorePaint2);
        canvas.drawText("SCORE : "+score,(int)buttonWidth1,(int)canvasHeight-(int)(buttonWidthSub2*1.2),scorePaint2);

    if(datacontrol1==1)
       {
           datacontrol=0;
           datacontrol1=0;
       }
    }

    public int database_send(int p)
    {
        if(datacontrol==p) {
            datacontrol1=1;
            return datacontrol;
        }
        datacontrol=0;
        return 0;
    }

    public int data1()
    {
        return xprint;
    }
    public int data2()
    {
        return yprint;
    }
    public int data3()
    {
        return 20;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchx=event.getX();
        float touchy=event.getY();
        if(touch==1) {
            if (touchx >= buttonWidth && touchx <= buttonWidth+(int)buttonWidthSub && touchy >= canvasHeight-(int)buttonWidthSub && touchy <= canvasHeight) {
                range++;

                datacontrol=1;

                touch = 0;
            }

            if (touchx >= buttonWidth1 && touchx <= buttonWidth1+(int)buttonWidthSub && touchy >= canvasHeight-(int)buttonWidthSub && touchy <= canvasHeight) {
                range--;
                touch = 0;
            }


            if (touchx >= buttonWidth-(int)buttonWidthSub2 && touchx <= buttonWidth && touchy >= dartHeigth+20 && touchy <= buttonHeigth) {
               yaw_bt=1;
               pitch_bt=0;
               roll_bt=0;

            }
            if (touchx >= buttonWidth1-(int)buttonWidthSub2 && touchx <= buttonWidth1 && touchy >= dartHeigth+20 && touchy <= buttonHeigth) {
                yaw_bt=0;
                pitch_bt=1;
                roll_bt=0;


            }
            if (touchx >= buttonWidth2-(int)buttonWidthSub2 && touchx <= buttonWidth2 && touchy >= dartHeigth+20 && touchy <= buttonHeigth) {
                yaw_bt=0;
                pitch_bt=0;
                roll_bt=1;

            }
        }
        return true;
    }

    long map(long x, long in_min, long in_max, long out_min, long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
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
