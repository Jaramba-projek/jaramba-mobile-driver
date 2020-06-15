package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileDriverActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_driver);

        bottomNavigationView = findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.profile);

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


}
