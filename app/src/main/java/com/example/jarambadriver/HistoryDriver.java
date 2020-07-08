package com.example.jarambadriver;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryDriver extends AppCompatActivity {
    private ImageView rate;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    private DatabaseReference database;

    String nama, trayek, id_trip, id_bus, key;

    private ArrayList<HistoryData> historyData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_driver);

        recyclerView = findViewById(R.id.rv_dtlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        nama = intent.getStringExtra("nama");
        trayek = intent.getStringExtra("trayek");
        id_trip = intent.getStringExtra("id_trip");
        id_bus = intent.getStringExtra("id_bus");
        key = intent.getStringExtra("key");   //coba key manual
        //key = "-MAevbqfQXiV35elXuHw";

        database = FirebaseDatabase.getInstance().getReference();

        database.child("Mobile_Apps").child("Driver").child(key).child("History_Trip_Driver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                historyData.clear();

                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    HistoryData data = new HistoryData();
                    data = noteDataSnapshot.getValue(HistoryData.class);
                    data.setKey(noteDataSnapshot.getKey());

                    historyData.add(data);
                }

                Collections.reverse(historyData);
                recyclerAdapter = new RecyclerAdapter(historyData);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        BottomNavigationView bottomNavigationView =  findViewById(R.id.menu_navigasi_history);
        bottomNavigationView.setSelectedItemId(R.id.history);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent intent = new Intent(HistoryDriver.this, HomeActivity.class);
                        intent.putExtra("nama", nama);
                        intent.putExtra("id_trip", id_trip);
                        intent.putExtra("key",key);
                        intent.putExtra("trayek", trayek);
                        intent.putExtra("id_bus", id_bus);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.trip:
                        Intent intent1 = new Intent(HistoryDriver.this, Trip_start.class);
                        intent1.putExtra("nama", nama);
                        intent1.putExtra("id_trip", id_trip);
                        intent1.putExtra("key",key);
                        intent1.putExtra("trayek", trayek);
                        intent1.putExtra("id_bus", id_bus);
                        startActivity(intent1);
                        finish();
                        break;
                    case R.id.profile:
                        Intent intent2 = new Intent(HistoryDriver.this, ProfileDriverActivity.class);
                        intent2.putExtra("nama", nama);
                        intent2.putExtra("id_trip", id_trip);
                        intent2.putExtra("key",key);
                        intent2.putExtra("trayek", trayek);
                        intent2.putExtra("id_bus", id_bus);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });
    }
}
