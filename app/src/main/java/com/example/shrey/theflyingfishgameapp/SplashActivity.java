package com.example.shrey.theflyingfishgameapp;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {
    private Button start,dart,analysebut,graph;
    private TextView text1;

    public BluetoothSocket socket=BluetoothActivity.socket;
    public static databasehelper mDatabaseHelper = DatabaseActivity.mDatabaseHelper;

    private String patient_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        patient_name=getIntent().getStringExtra("patientName");

        start=(Button) findViewById(R.id.startGame);
        dart=(Button)findViewById(R.id.dartGame);
        analysebut=(Button)findViewById(R.id.analyse);
        text1=(TextView)findViewById(R.id.textView1);
        graph=(Button)findViewById(R.id.graph);

        text1.setText("Hi " + patient_name);

        mDatabaseHelper.createAnalysisTable(patient_name);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(SplashActivity.this,MainActivity.class);
                i.putExtra("patientName", patient_name);
                startActivity(i);
            }
        });
        dart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s= new Intent(SplashActivity.this,DartActivity.class);
                s.putExtra("patientName", patient_name);
                startActivity(s);
            }
        });
        analysebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(SplashActivity.this,AnalyseActivity.class);
                i.putExtra("patientName", patient_name);
                startActivity(i);
            }
        });
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SplashActivity.this,Graph_Activity.class);
                i.putExtra("patientName",patient_name);
                startActivity(i);
            }
        });

       /* Thread thread = new Thread();
        {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }

        };
       thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();*/
    }
}
