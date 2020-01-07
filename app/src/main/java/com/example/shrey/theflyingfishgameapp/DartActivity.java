package com.example.shrey.theflyingfishgameapp;

import android.bluetooth.BluetoothSocket;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class DartActivity extends AppCompatActivity {
    private dartView dart;
    String patient_name;
    public BluetoothSocket socket=BluetoothActivity.socket;
    private Handler handler1=new Handler();
    private static long Interval1=30;
    public int x,y,z;

    private databasehelper mDatabaseHelper = DatabaseActivity.mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dart);
        dart= new dartView(this);
        setContentView(dart);

        patient_name= getIntent().getStringExtra("patientName");



//        x=10;
//        y=10;
//        z=10;
//  System.out.println("x "+x +"y "+y +"z"+z);

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        dart.invalidate();
                    }
                });

            if(dart.database_send(1)==1) {
                x = dart.data1();
                y = dart.data2();
                z = dart.data3();

                DatabaseActivity.Addanalysis(Integer.toString(x), Integer.toString(y), Integer.toString(z), patient_name);
            }

            }
        },0,Interval1);

    }
}
