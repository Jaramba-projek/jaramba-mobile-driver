package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HistoryDriverDetail extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_driver_detail);

        bottomNavigationView = findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.history);

        setOnNavigationSelectedListener();
    }

    private void setOnNavigationSelectedListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(HistoryDriverDetail.this, ProfileDriverActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                }

                return false;
            }
        });
    }
}
