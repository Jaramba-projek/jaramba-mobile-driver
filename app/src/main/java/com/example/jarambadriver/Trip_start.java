package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class Trip_start extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ImageView greetImg;
    TextView greetText;
    Button btnstart;
    Spinner trayek, noKendaraan;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_start);

       greetImg = findViewById(R.id.greeting_img);
        greetText = findViewById(R.id.greeting_text);

        trayek = findViewById(R.id.btn_trayek);
        trayek.setOnItemSelectedListener(this);
        noKendaraan = findViewById(R.id.btn_plat);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.no_kendaraan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noKendaraan.setAdapter(adapter);
        noKendaraan.setOnItemSelectedListener(this);


        greeting();

        btnstart = findViewById(R.id.btn_start);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Trip_finish.class));
            }
        });

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
                        return true;
                  //  case R.id.home:
                    //    return true;
                    case R.id.trip:
                        return true;
                    //case R.id.profile:
                      //  return true;

                }
                return false;
            }
        });




    }

    @SuppressLint("SetTextI18n")
    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 18) {
            if(timeOfDay > 3 && timeOfDay <12 ) {
                greetText.setText("Good Morning");
            } else if(timeOfDay >=12) {
                greetText.setText("Good Afternoon");
            }
            greetImg.setImageResource(R.drawable.img_default_half_morning);
            Glide.with(Trip_start.this).load(R.drawable.img_default_half_morning).into(greetImg);
        }else if (timeOfDay >= 18 && timeOfDay < 24) {
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
        Toast.makeText(this, parent.getSelectedItem().toString().trim(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}