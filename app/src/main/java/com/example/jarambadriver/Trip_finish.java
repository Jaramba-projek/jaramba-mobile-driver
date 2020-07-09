package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Calendar;

public class Trip_finish extends AppCompatActivity {

    ImageView greetImg;
    TextView greetText;
    Button btnfinish;

    String nama, driverKey, trayek_pilihan, id_trip, key, chKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_finish);

        greetImg = findViewById(R.id.greeting_img);
        greetText = findViewById(R.id.greeting_text);

        greeting();

        Intent i = getIntent();
        nama = i.getStringExtra("nama");
        driverKey = i.getStringExtra("key");
        trayek_pilihan = i.getStringExtra("trayek");
        id_trip = i.getStringExtra("id_trip");
        key = i.getStringExtra("id_bus");
        chKey = i.getStringExtra("chKey");

        btnfinish = findViewById(R.id.btn_finish);


      /*  BottomNavigationView bottomNavigationView =  findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.trip);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.history:
                        Intent intent3 = new Intent(Trip_finish.this, HistoryDriver.class);
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
                        Intent intent2 = new Intent(Trip_finish.this, HomeActivity.class);
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
                        Intent intent = new Intent(Trip_finish.this, ProfileDriverActivity.class);
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
                return false;
            }
        });
*/
        ChipNavigationBar bottomNavigationView =  findViewById(R.id.chipNavigationBar);
        bottomNavigationView.setItemSelected(R.id.trip,true);
        bottomNavigationView.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.history:
//                        startActivity(new Intent(getApplicationContext()
//                                ,history.class));
                        overridePendingTransition(0,0);
                        finish();
                        break;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), TripDriver.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileDriverActivity.class));
                        finish();
                        break;
                }
            }
        });




    }

    @SuppressLint("SetTextI18n")
    private void greeting() {
        Calendar calendar = Calendar.getInstance();
        int timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetText.setText("Selamat Pagi\nBudi Makarti");
            greetImg.setImageResource(R.drawable.img_default_half_morning);
        } else if (timeOfDay >= 12 && timeOfDay < 15) {
            greetText.setText("Selamat Siang\nBudi Makarti");
            greetImg.setImageResource(R.drawable.img_default_half_afternoon);
        } else if (timeOfDay >= 15 && timeOfDay < 18) {
            greetText.setText("Selamat Sore\nBudi Makarti");
            greetImg.setImageResource(R.drawable.img_default_half_without_sun);
        }else if (timeOfDay >= 18 && timeOfDay < 24) {
            greetText.setText("Selamat Malam\nBudi Makarti");
            greetText.setTextColor(Color.WHITE);
            greetImg.setImageResource(R.drawable.malamhari);
        }
    }
}