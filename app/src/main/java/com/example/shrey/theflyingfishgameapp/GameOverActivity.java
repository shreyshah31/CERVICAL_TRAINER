package com.example.shrey.theflyingfishgameapp;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import javax.xml.transform.Templates;

public class GameOverActivity extends AppCompatActivity {
    public BluetoothSocket socket=BluetoothActivity.socket;
    private Button startGame;
    private TextView Score;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        str=getIntent().getExtras().get("score").toString();

        startGame=(Button) findViewById(R.id.ply_again_btn);
        Score=(TextView)findViewById(R.id.displayScore);

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playagain = new Intent(GameOverActivity.this,MainActivity.class);
                startActivity(playagain);
            }
        });
        Score.setText("SCORE "+str);
    }
}
