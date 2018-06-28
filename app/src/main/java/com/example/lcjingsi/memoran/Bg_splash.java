package com.example.lcjingsi.memoran;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Bg_splash extends AppCompatActivity {
    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bg_splash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Bg_splash.this,MainActivity.class));
                finish();
            }
        },4000);
    }
}
