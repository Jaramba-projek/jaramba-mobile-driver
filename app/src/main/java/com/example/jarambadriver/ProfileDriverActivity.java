package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class ProfileDriverActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView greetImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_driver);

        bottomNavigationView = findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        greetImg = findViewById(R.id.greeting_img_profile);

        setOnNavigationSelectedListener();


    }

    private void setOnNavigationSelectedListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.history:
                        startActivity(new Intent(ProfileDriverActivity.this, HistoryDriverDetail.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                }

                return false;
            }
        });
    }

    public void logout(View view) {

    }


    @SuppressLint("SetTextI18n")
    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 18) {
            greetImg.setImageResource(R.drawable.img_default_half_morning);
            Glide.with(ProfileDriverActivity.this).load(R.drawable.img_default_half_morning).into(greetImg);
        }else if (timeOfDay >= 18 && timeOfDay < 24) {
            Glide.with(ProfileDriverActivity.this).load(R.drawable.img_default_half_night).into(greetImg);
            greetImg.setImageResource(R.drawable.malamhari);
        }

    }


}
