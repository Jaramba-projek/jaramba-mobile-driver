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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Trip_start extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    DatabaseReference databaseReference;


    ImageView greetImg;
    TextView greetText;
    Spinner trayek, noKendaraan;
    Button btnStart, btnFinish;

    String key, platNumber, trayex, status;

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
//        trayek.setOnItemSelectedListener(this);
        noKendaraan = findViewById(R.id.btn_plat);

        spinnerDataList1 = new ArrayList<>();
        spinnerDataList2 = new ArrayList<>();
        adapter = new ArrayAdapter<String>(Trip_start.this,
                android.R.layout.simple_spinner_dropdown_item, spinnerDataList1);
        adapter2 = new ArrayAdapter<String>(Trip_start.this,
                android.R.layout.simple_spinner_dropdown_item, spinnerDataList2);

        trayek.setAdapter(adapter);
        noKendaraan.setAdapter(adapter2);


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
                        startActivity(new Intent(getApplicationContext()
                                ,history.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileDriverActivity.class));
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
            } else if(timeOfDay >=12) {
                greetText.setText("Good Afternoon");
            }
            greetImg.setImageResource(R.drawable.img_default_half_morning);
            Glide.with(Trip_start.this).load(R.drawable.img_default_half_morning).into(greetImg);
        }else if (timeOfDay >= 18 && timeOfDay < 23) {
            if(timeOfDay < 21 ) {
                greetText.setText("Good Evening");
            } else if(timeOfDay > 21) {
                greetText.setText("Good Night");
            }
            greetText.setTextColor(Color.WHITE);
            Glide.with(Trip_start.this).load(R.drawable.img_default_half_night).into(greetImg);
            greetImg.setImageResource(R.drawable.malamhari);
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

        Query query = databaseReference.orderByChild("trayek").equalTo(getTrayek);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    platNumber = ""+ds.child("plat_number").getValue();
                    trayex = ""+ds.child("trayek").getValue();
                    key = ""+ds.child("key").getValue();
                    status = ""+ds.child("status").getValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String concat = trayex + "_" + platNumber;
        String concats = getTrayek + "_" + getPlatNumber;


        if(concat.equals(concats)) {
            if(status.equals("Bus tidak aktif")) {
                setStartTrip();
            }else {
                Toast.makeText(Trip_start.this, "Maaf, Bus sedang aktif", Toast.LENGTH_SHORT).show();
            }
        } else{
            //can't continue
            Toast.makeText(Trip_start.this, "Maaf, trayek dan nomor kendaraan tidak sesuai jalur yang ditentukan", Toast.LENGTH_SHORT).show();
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
                status.put("status", "Bus Aktif");
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


        String email = "bagasganteng88@gmail.com";
        String trayek_pilihan = trayek.getSelectedItem().toString().trim();
        String nomor_kendaraan_pilihan = noKendaraan.getSelectedItem().toString().trim();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("trayek", trayek_pilihan);
        hashMap.put("nomor_kendaraan", nomor_kendaraan_pilihan);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("driver_trips");
        reference.child(nomor_kendaraan_pilihan).setValue(hashMap);

        btnStart.setVisibility(View.GONE);
        trayek.setEnabled(false);
        noKendaraan.setEnabled(false);


        if(timeOfDay > 18) {
            btnStart.setVisibility(View.VISIBLE);
            trayek.setEnabled(true);
            noKendaraan.setEnabled(true);
        }

        progressDialog.dismiss();
        Toast.makeText(this, email + "\n" + trayek_pilihan + "\n" + nomor_kendaraan_pilihan, Toast.LENGTH_SHORT).show();
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

                //ini sementara solusinya
                adapter2.clear();

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