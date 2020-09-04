package com.jagdeesh.voicerecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {
    Handler handler;
    private TextView welcome, title, loading;
    private ImageView header_bg, logo;
    private Animation rightToLeft, leftToRight, downToTop, uptodown, fadeIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        welcome = findViewById(R.id.textView4);
        title = findViewById(R.id.textView5);
        loading = findViewById(R.id.loading);
        header_bg = findViewById(R.id.imageView4);
        logo = findViewById(R.id.imageView11);


        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);
    }
}