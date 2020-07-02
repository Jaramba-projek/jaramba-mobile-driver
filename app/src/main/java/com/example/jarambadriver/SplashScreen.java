package com.example.jarambadriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3500;

    //Variable
    Animation topanim, bottomanim;
    ImageView logo_image, logo_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        topanim = AnimationUtils.loadAnimation(this,R.anim.splash_top);
        bottomanim = AnimationUtils.loadAnimation(this,R.anim.splash_bottom);

        //Timing
        logo_image = findViewById(R.id.logo_image);
        logo_text = findViewById(R.id.logo_text);

        logo_image.setAnimation(topanim);
        logo_text.setAnimation(bottomanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,Trip_start.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}