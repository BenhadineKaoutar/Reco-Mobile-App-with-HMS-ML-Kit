package com.cocoben.reco;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //doFirstRunCheckup();
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashActivity.this, IntroActivity.class);
            startActivity(i);
            finish();
        }, SPLASH_TIME_OUT);
    }

    private void doFirstRunCheckup() {
        startActivity(new Intent(SplashActivity.this, IntroActivity.class));
        finish();
    }

}