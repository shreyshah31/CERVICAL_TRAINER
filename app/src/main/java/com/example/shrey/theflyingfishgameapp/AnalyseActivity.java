package com.example.shrey.theflyingfishgameapp;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class AnalyseActivity extends AppCompatActivity {
    private AnalyseView analyseView;
    public BluetoothSocket socket=BluetoothActivity.socket;
    private Handler handler1=new Handler();
    private static long Interval1=30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_analyse);
        analyseView=new AnalyseView(this);
        setContentView(analyseView);

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        analyseView.invalidate();
                    }
                });
            }
        },0,Interval1);
    }
}
