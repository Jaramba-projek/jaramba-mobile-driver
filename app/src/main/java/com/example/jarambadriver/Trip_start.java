package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class Trip_start extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DatabaseReference databaseReference;


    ImageView greetImg;
    TextView greetText;
    Spinner trayek, noKendaraan;
    Button btnStart, btnFinish;

    String key, platNumber, trayex, status, price;
    String id_trip;

    String nama, driverKey;
    String concat, concats;
    String trayek_pilihan;


    ProgressDialog progressDialog;

    ValueEventListener listener;
    ArrayAdapter<String> adapter, adapter2;
    ArrayList<String> spinnerDataList1, spinnerDataList2;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_start);

       databaseReference = FirebaseDatabase.getInstance().getReference("bus");
        progressDialog = new ProgressDialog(this);

        greetImg = findViewById(R.id.greeting_img);
        greetText = findViewById(R.id.greeting_text);

        trayek = findViewById(R.id.btn_trayek);
        noKendaraan = findViewById(R.id.btn_plat);
        btnStart = findViewById(R.id.btn_start_trip);

        spinnerDataList1 = new ArrayList<>();
        spinnerDataList2 = new ArrayList<>();
        adapter = new ArrayAdapter<String>(Trip_start.this,
                android.R.layout.simple_spinner_dropdown_item, spinnerDataList1);
        adapter2 = new ArrayAdapter<String>(Trip_start.this,
                android.R.layout.simple_spinner_dropdown_item, spinnerDataList2);

        trayek.setAdapter(adapter);
        noKendaraan.setAdapter(adapter2);


        Intent i = getIntent();
        nama = i.getStringExtra("nama");
        driverKey = i.getStringExtra("key");
        trayek_pilihan = i.getStringExtra("trayek");
        id_trip = i.getStringExtra("id_trip");
        key = i.getStringExtra("id_bus");

        if(id_trip!=null){
            btnStart.setVisibility(View.GONE);
            trayek.setEnabled(false);
            noKendaraan.setEnabled(false);
        }


        retrieveData();


//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.no_kendaraan, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        noKendaraan.setAdapter(adapter);
//        noKendaraan.setOnItemSelectedListener(this);


        greeting();


        BottomNavigationView bottomNavigationView =  findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.trip);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.history:
//                        startActivity(new Intent(getApplicationContext()
//                                ,history.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.nav_home:
                        Intent intent2 = new Intent(Trip_start.this, HomeActivity.class);
                        intent2.putExtra("nama", nama);
                        intent2.putExtra("trayek",trayek_pilihan);
                        intent2.putExtra("key", driverKey);
                        intent2.putExtra("id_bus",key);
                        intent2.putExtra("id_trip", id_trip);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.profile:
                        Intent intent = new Intent(Trip_start.this, ProfileDriverActivity.class);
                        intent.putExtra("nama", nama);
                        intent.putExtra("trayek",trayek_pilihan);
                        intent.putExtra("id_trip", id_trip);
                        intent.putExtra("id_bus",key);
                        startActivity(intent);
                        finish();
                        break;
                }
                return false;
            }
        });




    }

    private void retrieveData () {
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    spinnerDataList1.add(ds.child("trayek").getValue().toString());
                    spinnerDataList2.add(ds.child("plat_number").getValue().toString());
                }
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay > 0 && timeOfDay < 18) {
            if(timeOfDay > 3 && timeOfDay <12 ) {
                greetText.setText("Good Morning");
                greetImg.setImageResource(R.drawable.img_default_half_morning);
                Glide.with(Trip_start.this).load(R.drawable.img_default_half_morning).into(greetImg);
            } else if(timeOfDay >=12) {
                greetText.setText("Good Afternoon");
                greetImg.setImageResource(R.drawable.img_default_half_afternoon);
                Glide.with(Trip_start.this).load(R.drawable.img_default_half_afternoon).into(greetImg);
            }

        }else if (timeOfDay >= 18 && timeOfDay < 23) {
            if(timeOfDay < 21 ) {
                greetText.setText("Good Evening");
                greetText.setTextColor(Color.WHITE);
                Glide.with(Trip_start.this).load(R.drawable.img_default_half_without_sun).into(greetImg);
                greetImg.setImageResource(R.drawable.img_default_half_without_sun);
            } else if(timeOfDay > 21) {
                greetText.setText("Good Night");
                greetText.setTextColor(Color.WHITE);
                Glide.with(Trip_start.this).load(R.drawable.img_default_half_night).into(greetImg);
                greetImg.setImageResource(R.drawable.malamhari);
            }
        Intent i = getIntent();
        String nama = i.getStringExtra("NAMA");

        if (timeOfDay > 0 && timeOfDay < 12) {
            greetText.setText("Selamat Pagi\n" + nama);
            greetImg.setImageResource(R.drawable.img_default_half_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 15) {
            greetText.setText("Selamat Siang\n" + nama);
            greetImg.setImageResource(R.drawable.img_default_half_afternoon);
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            greetText.setText("Selamat Sore\n" + nama);
            greetImg.setImageResource(R.drawable.img_default_half_without_sun);
        }else if (timeOfDay >= 18 && timeOfDay < 23) {
            greetText.setText("Selamat Malam\n" + nama);
            greetText.setTextColor(Color.WHITE);
            greetImg.setImageResource(R.drawable.malamhari);
        }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, parent.getSelectedItem().toString().trim(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void startTripDriver(View view) {
//        //trayek and number of vieicle must be match as in firebase database
        final String getTrayek = trayek.getSelectedItem().toString().trim();
        final String getPlatNumber = noKendaraan.getSelectedItem().toString().trim();

        Query query = databaseReference.orderByChild("plat_number").equalTo(getPlatNumber);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    platNumber = ""+ds.child("plat_number").getValue();
                    trayex = ""+ds.child("trayek").getValue();
                    key = ""+ds.child("key").getValue();
                    status = ""+ds.child("status").getValue();
                    price = ""+ds.child("price").getValue();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        concat = trayex + "_" + platNumber;
        concats = getTrayek + "_" + getPlatNumber;

        Toast.makeText(Trip_start.this, concat + "\n" + concats, Toast.LENGTH_LONG).show();


        if(concat.equals(concats)) {
            if(status.equals("Bus tidak aktif")) {
                setStartTrip();
            }else {
                Toast.makeText(Trip_start.this, "Maaf, Bus sedang aktif", Toast.LENGTH_SHORT).show();
            }
        } else{
            //can't continue
            Toast.makeText(Trip_start.this, "Tekan sekali lagi, \nBila kemunculan tetap sama, mungkin bus yang anda pilih tidak sesuai dengan trayek ", Toast.LENGTH_SHORT).show();
        }
    }

    private void setStartTrip() {
        //create alert dialog before start trip
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("perintah memulai perjalanan");

        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setMessage("Anda yakin ingin memulai perjalanan ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mulai perjalanan

                startTrip();

                //INI YG NYEBABIN BUG
                HashMap<String, Object> status = new HashMap<>();
                status.put("status", "Bus aktif");
                databaseReference.child(key).updateChildren(status);

                //ini sementara solusinya
                adapter.clear();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void startTrip() {
        progressDialog();
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        trayek = findViewById(R.id.btn_trayek);
        noKendaraan = findViewById(R.id.btn_plat);
        btnStart = findViewById(R.id.btn_start_trip);
        btnFinish = findViewById(R.id.btn_finish_trip);


        trayek_pilihan = trayek.getSelectedItem().toString().trim();
        String nomor_kendaraan_pilihan = noKendaraan.getSelectedItem().toString().trim();


        //getCurrent time clock
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        Date currentLocalTime = cal.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        String localTime = dateFormat.format(currentLocalTime);


        Date c = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String currentDate = df.format(c);

        id_trip = key + "_" + currentDate + "_" + localTime;


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("driver_name", "Bagas Ganteng");
        hashMap.put("trayek", trayek_pilihan);
        hashMap.put("plate_number", nomor_kendaraan_pilihan);
        hashMap.put("start_time", localTime);
        hashMap.put("end_time", "");
        hashMap.put("gps", "");
        hashMap.put("id_bus", key);
        hashMap.put("id_driver", driverKey);
        hashMap.put("key",id_trip);
        hashMap.put("price", price);
        hashMap.put("rating", "");
        hashMap.put("status", "aktif");
        hashMap.put("tanggal", currentDate);
        hashMap.put("total_passenger", "");



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("history_trip_dashboard");
        reference.child(id_trip).setValue(hashMap);


        btnStart.setVisibility(View.GONE);
        trayek.setEnabled(false);
        noKendaraan.setEnabled(false);


        if(timeOfDay > 18) {
            btnStart.setVisibility(View.VISIBLE);
            trayek.setEnabled(true);
            noKendaraan.setEnabled(true);
        }

        progressDialog.dismiss();
        Toast.makeText(this, "Berhasil memilih...", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Selamat memulai perjalanan, jangan lupa berdoa", Toast.LENGTH_SHORT).show();

    }

    private void progressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void finishTripDriver(View view) {
        //create alert dialog finish trip driver
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("perintah menyelesaikan perjalanan");

        builder.setIcon(R.drawable.ic_warning_black_24dp);
        builder.setMessage("Anda yakin ingin menyelesaikan perjalanan ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //menyelesaikan perjalanan

                finishTrip();

                //INI YG NYEBABIN BUG
                HashMap<String, Object> status = new HashMap<>();
                status.put("status", "Bus tidak aktif");
                databaseReference.child(key).updateChildren(status);

                //matikan ini ketika finish trip
                trayek_pilihan = null;
                id_trip = null;
                key = null;

                //ini sementara solusinya
                adapter2.clear();
                adapter.clear();

            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

    private void finishTrip() {
        trayek = findViewById(R.id.btn_trayek);
        noKendaraan = findViewById(R.id.btn_plat);
        btnStart = findViewById(R.id.btn_start_trip);
        btnFinish = findViewById(R.id.btn_finish_trip);

        //getCurrent time clock
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        Date currentLocalTime = cal.getTime();
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        String localTime = dateFormat.format(currentLocalTime);

        HashMap<String, Object> status = new HashMap<>();
        status.put("end_time", localTime);
        status.put("status", "tidak aktif");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("history_trip_dashboard");
        reference.child(id_trip).updateChildren(status);


        btnStart.setVisibility(View.VISIBLE);
        trayek.setEnabled(true);
        noKendaraan.setEnabled(true);

        Toast.makeText(Trip_start.this, "Anda menyelesaikan perjalanan", Toast.LENGTH_SHORT).show();

    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi keluar aplikasi");
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp);
        builder.setMessage("Anda yakin ingin keluar aplikasi ? ");
        builder.setCancelable(false);

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAndRemoveTask();
                finish();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}