package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class TripDriver extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng>arrayList = new ArrayList<LatLng>();
    LatLng Sydney = new LatLng(-34,151);
    LatLng Tamworth = new LatLng(-31.083332, 150.916672);
    LatLng NewCastle = new LatLng(-32, 151);

    private DatabaseReference databaseReference;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    private EditText editTextLatitude;
    private EditText editTextLongitude;


    private static  final long START_TIME_IN_MILLIS = 600000;
    private TextView vCounter;
    private Button btnStart;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMills = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_driver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        arrayList.add(Sydney);
        arrayList.add(Tamworth);
        arrayList.add(NewCastle);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        editTextLatitude = findViewById(R.id.editText1);
        editTextLongitude = findViewById(R.id.editText2);

        databaseReference = FirebaseDatabase.getInstance().getReference("Driver Location");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String databaseLatitudeString = dataSnapshot.child("latitude").getValue().toString().substring(1,dataSnapshot.child("latitude").getValue().toString().length()-1);
                    String databaseLongitudeString = dataSnapshot.child("longitude").getValue().toString().substring(1,dataSnapshot.child("longitude").getValue().toString().length()-1);

                    String[] stringLat = databaseLatitudeString.split(", ");
                    Arrays.sort(stringLat);
                    String latitude = stringLat[stringLat.length-1].split("=")[1];

                    String[] stringLong = databaseLongitudeString.split(", ");
                    Arrays.sort(stringLong);
                    String longitude = stringLong[stringLong.length-1].split("=")[1];

                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    mMap.addMarker(new MarkerOptions().position(latLng).title(latitude+", "+longitude).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bus)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        vCounter = findViewById(R.id.txt_count);
        btnStart = findViewById(R.id.update);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        updateCountDownText();

    }

    public void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMills = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();
        mTimerRunning = true;
    }

    private void updateCountDownText(){
        int minutes = (int) (mTimeLeftInMills / 1000) / 60;
        int seconds = (int) (mTimeLeftInMills / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        vCounter.setText(timeLeftFormatted);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                try {
                    editTextLatitude.setText(Double.toString(location.getLatitude()));
                    editTextLongitude.setText(Double.toString(location.getLongitude()));

                    databaseReference.child("latitude").push().setValue(editTextLatitude.getText().toString());
                    databaseReference.child("longitude").push().setValue(editTextLongitude.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void updateButtonOnClick(View view){
        databaseReference.child("latitude").push().setValue(editTextLatitude.getText().toString());
        databaseReference.child("longitude").push().setValue(editTextLongitude.getText().toString());
    }

}