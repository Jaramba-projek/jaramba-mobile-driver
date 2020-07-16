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
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

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
    TextView greetText, driversName;
    Spinner trayek, noKendaraan;
    Button btnStart, btnFinish;

    String key, platNumber, trayex, status, price;
    String id_trip;

    String nama, driverKey;
    String concat, concats;
    String trayek_pilihan;

    //data untuk history driver
    String starttime_hist, endtime_hist, tgl_hist;
    String chKey;


    ProgressDialog progressDialog;

    ValueEventListener listener;
    ArrayAdapter<String> adapter, adapter2;
    ArrayList<String> spinnerDataList1, spinnerDataList2;

    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_start);

       databaseReference = FirebaseDatabase.getInstance().getReference("bus");
        progressDialog = new ProgressDialog(this);

        greetImg = findViewById(R.id.greeting_img);
        greetText = findViewById(R.id.greeting_text);
        driversName = findViewById(R.id.username_driver);

        trayek = findViewById(R.id.btn_trayek);
        noKendaraan = findViewById(R.id.btn_plat);
        btnStart = findViewById(R.id.btn_start_trip);
        btnFinish = findViewById(R.id.btn_finish_trip);

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
        chKey = i.getStringExtra("chKey");

       // Toast.makeText(Trip_start.this, trayek_pilihan + "\n" + id_trip + "\n" + nama + "\n" + key, Toast.LENGTH_LONG).show();


        if(id_trip!=null){
            btnStart.setVisibility(View.GONE);
            trayek.setEnabled(false);
            noKendaraan.setEnabled(false);
        }

        //cek apakah masih ada trip yang berlangsung



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("jam");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String jam = snapshot.child("selesai").getValue(String.class);

                String x = jam.substring(0,2);
                String y = jam.substring(3);

                long  jam_selesai = Integer.parseInt(x) * 3600 * 1000;
                long  menit_selesai = Integer.parseInt(y) * 60 * 1000;

                long hasil = jam_selesai+menit_selesai;

                //Toast.makeText(HomeActivity.this, x + " + "+ y + "\n" + hasil, Toast.LENGTH_LONG).show();

                Calendar rightNow = Calendar.getInstance();
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                int currentMinute = rightNow.get(Calendar.MINUTE);
                int currentSec = rightNow.get(Calendar.SECOND);

                long currTime = (currentHour*3600*1000) + (currentMinute*60*1000) + (currentSec*1000);

                if(currTime > hasil) {
                    btnStart.setVisibility(View.GONE);
                    btnFinish.setVisibility(View.GONE);
                    trayek.setEnabled(false);
                    noKendaraan.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        retrieveData();


        greeting();


        ChipNavigationBar bottomNavigationView =  findViewById(R.id.chipNavigationBar);
        bottomNavigationView.setItemSelected(R.id.trip,true);
        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.history:
                        Intent intent3 = new Intent(Trip_start.this, HistoryDriver.class);
                        intent3.putExtra("nama", nama);
                        intent3.putExtra("trayek",trayek_pilihan);
                        intent3.putExtra("key", driverKey);
                        intent3.putExtra("id_bus",key);
                        intent3.putExtra("id_trip", id_trip);
                        intent3.putExtra("chKey", chKey);
                        startActivity(intent3);
                        finish();
                        break;
                    case R.id.nav_home:
                        Intent intent2 = new Intent(Trip_start.this, HomeActivity.class);
                        intent2.putExtra("nama", nama);
                        intent2.putExtra("trayek",trayek_pilihan);
                        intent2.putExtra("key", driverKey);
                        intent2.putExtra("id_bus",key);
                        intent2.putExtra("id_trip", id_trip);
                        intent2.putExtra("chKey", chKey);
                        startActivity(intent2);
                        finish();
                        break;
                    case R.id.profile:
                        Intent intent = new Intent(Trip_start.this, ProfileDriverActivity.class);
                        intent.putExtra("nama", nama);
                        intent.putExtra("trayek",trayek_pilihan);
                        intent.putExtra("key", driverKey);
                        intent.putExtra("id_trip", id_trip);
                        intent.putExtra("id_bus",key);
                        intent.putExtra("chKey", chKey);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });





    }

    private void retrieveData () {
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                adapter2.clear();
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
        calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 18) {
            if(timeOfDay >= 0 && timeOfDay <12 ) {
                greetText.setText("Good Morning");
                driversName.setText(nama);
                greetImg.setImageResource(R.drawable.header_morning);
                Glide.with(Trip_start.this).load(R.drawable.header_morning).into(greetImg);
            } else if(timeOfDay >=12) {
                greetText.setText("Good Afternoon");
                driversName.setText(nama);
                greetImg.setImageResource(R.drawable.header_morning);
                Glide.with(Trip_start.this).load(R.drawable.header_morning).into(greetImg);
            }

        }else if (timeOfDay >= 18 && timeOfDay < 24) {
            if(timeOfDay < 21 ) {
                greetText.setText("Good Evening");
                greetText.setTextColor(Color.WHITE);
                driversName.setText(nama);
                driversName.setTextColor(Color.WHITE);
                Glide.with(Trip_start.this).load(R.drawable.header_night).into(greetImg);
                greetImg.setImageResource(R.drawable.header_night);
            } else if(timeOfDay >= 21) {
                greetText.setText("Good Night");
                greetText.setTextColor(Color.WHITE);
                driversName.setText(nama);
                driversName.setTextColor(Color.WHITE);
                Glide.with(Trip_start.this).load(R.drawable.header_night).into(greetImg);
                greetImg.setImageResource(R.drawable.header_night);
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


        if(concat.equals(concats)) {
            if(status.equals("tidak aktif")) {
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
                status.put("status", "aktif");
                databaseReference.child(key).updateChildren(status);

//                //ini sementara solusinya
//                adapter.clear();
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
        hashMap.put("driver_name", nama);
        hashMap.put("trayek", trayek_pilihan);
        hashMap.put("plate_number", nomor_kendaraan_pilihan);
        hashMap.put("start_time", localTime);
        starttime_hist = localTime; //mengambil data waktu start untuk history driver
        hashMap.put("end_time", "");
        hashMap.put("gps", "");
        hashMap.put("id_bus", key);
        hashMap.put("id_driver", driverKey);
        hashMap.put("key",id_trip);
//        hashMap.put("price", price);
        hashMap.put("rating", "");
        hashMap.put("status", "aktif");
        hashMap.put("tanggal", currentDate);
        tgl_hist = currentDate;
//        hashMap.put("total_passenger", "");



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("history_trip_dashboard");
        reference.child(id_trip).setValue(hashMap);

        //Membuat key history driver
        char[] dd = {starttime_hist.charAt(0), starttime_hist.charAt(1), starttime_hist.charAt(3), starttime_hist.charAt(4)};
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
        String tgl = dt.format(c);
        String dtime = new String(dd);
        chKey = tgl + "" + dtime;

        //Mengirim data ke DB history driver
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Mobile_Apps").child("Driver").child(driverKey).child("History_Trip_Driver").child(chKey).setValue(new HistoryData(0, "", trayek_pilihan, platNumber, tgl_hist, starttime_hist, "", "not", id_trip, driverKey, "not"));

        btnStart.setVisibility(View.GONE);
        trayek.setEnabled(false);
        noKendaraan.setEnabled(false);


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
                status.put("status", "tidak aktif");
                databaseReference.child(key).updateChildren(status);

                //matikan ini ketika finish trip
                trayek_pilihan = null;
                id_trip = null;
                key = null;

                DatabaseReference driverLocationRef = FirebaseDatabase.getInstance().getReference("Driver Location");
                HashMap<String, Object> driverLocRef = new HashMap<>();
                driverLocRef.put("trayek", null);
                driverLocationRef.child(driverKey).updateChildren(driverLocRef);

                //ini sementara solusinya
//                adapter2.clear();
//                adapter.clear();

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
        endtime_hist = localTime; //mengirim data waktu selesai untuk history driver
        status.put("status", "tidak aktif");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("history_trip_dashboard");
        reference.child(id_trip).updateChildren(status);

        //Mengirim data ke DB history driver
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        HashMap<String, Object> Etime = new HashMap<>();
        Etime.put("end_time", endtime_hist);
        Etime.put("status", "done");
        myRef.child("Mobile_Apps").child("Driver").child(driverKey).child("History_Trip_Driver").child(chKey).updateChildren(Etime);

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