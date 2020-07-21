package com.example.jarambadriver;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class HistoryDriver extends AppCompatActivity {
    private ImageView rate;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    private DatabaseReference database;

    String nama, trayek, id_trip, id_bus, key, chKey, endtime_hist;

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

    @Override
    public void onBackPressed() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi keluar aplikasi");
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        builder.setMessage("Anda yakin ingin Logout ?\n\nJika anda belum menyelesaikan perjalanan, maka otomatis perjalanan anda akan diberhentikan ");
        builder.setCancelable(false);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(ProfileDriverActivity.this, LoginPage.class));
//                finish();

                DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference("Driver Location");
                HashMap<String, Object> driverLocRef = new HashMap<>();
                driverLocRef.put("trayek", null);
                driverLocationRef.child(key).updateChildren(driverLocRef);

                if(id_bus != null && id_trip != null){
                    DatabaseReference busRef = FirebaseDatabase.getInstance().getReference("bus");
                    HashMap<String, Object> status = new HashMap<>();
                    status.put("status", "tidak aktif");
                    busRef.child(id_bus).updateChildren(status);
                }

                if(id_trip != null){
                    //getCurrent time clock
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
                    Date currentLocalTime = cal.getTime();
                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
                    String localTime = dateFormat.format(currentLocalTime);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("history_trip_dashboard");
                    HashMap<String, Object> status_endTime = new HashMap<>();
                    status_endTime.put("end_time", localTime);
                    status_endTime.put("status", "tidak aktif");
                    endtime_hist = localTime; //mengirim data waktu selesai untuk history driver
                    reference.child(id_trip).updateChildren(status_endTime);

                    //Mengirim data ke DB history driver
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    HashMap<String, Object> Etime = new HashMap<>();
                    Etime.put("end_time", endtime_hist);
                    Etime.put("status", "done");
                    myRef.child("Mobile_Apps").child("Driver").child(key).child("History_Trip_Driver").child(chKey).updateChildren(Etime);
                }


                startActivity(new Intent(HistoryDriver.this, LoginPage.class));
                finish();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
