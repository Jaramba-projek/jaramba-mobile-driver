package com.example.jarambadriver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class HistoryDriver extends AppCompatActivity {
    private ImageView rate;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    private DatabaseReference database;

    String nama, trayek, id_trip, id_bus, key, chKey;

    Calendar calendar;
    ImageView greetImg;
    TextView headerHist, withHist;

    private ArrayList<HistoryData> historyData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_driver);

        recyclerView = findViewById(R.id.rv_dtlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        greetImg = findViewById(R.id.headerHistory);
        headerHist = findViewById(R.id.txtTrip);
        withHist = findViewById(R.id.tripWith);

        greeting();

        Intent intent = getIntent();
        nama = intent.getStringExtra("nama");
        trayek = intent.getStringExtra("trayek");
        id_trip = intent.getStringExtra("id_trip");
        id_bus = intent.getStringExtra("id_bus");
        key = intent.getStringExtra("key");
        chKey = intent.getStringExtra("chKey");

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

        ChipNavigationBar bottomNavigationView =  findViewById(R.id.chipNavigationBar);
        bottomNavigationView.setItemSelected(R.id.history,true);
        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.nav_home:
                        Intent intent = new Intent(HistoryDriver.this, HomeActivity.class);
                        intent.putExtra("nama", nama);
                        intent.putExtra("id_trip", id_trip);
                        intent.putExtra("key",key);
                        intent.putExtra("trayek", trayek);
                        intent.putExtra("id_bus", id_bus);
                        intent.putExtra("chKey", chKey);
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
                        intent1.putExtra("chKey", chKey);
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
                        intent2.putExtra("chKey", chKey);
                        startActivity(intent2);
                        finish();
                        break;
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void greeting() {
        calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 18) {
            greetImg.setImageResource(R.drawable.header_morning);
            Glide.with(HistoryDriver.this).load(R.drawable.header_morning).into(greetImg);
        }else if (timeOfDay >= 18 && timeOfDay < 24) {
            headerHist.setTextColor(Color.WHITE);
            withHist.setTextColor(Color.WHITE);
            Glide.with(HistoryDriver.this).load(R.drawable.header_night).into(greetImg);
            greetImg.setImageResource(R.drawable.header_night);
        }
    }

}
