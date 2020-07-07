package com.example.jarambadriver;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jarambadriver.service.LocationService;
import com.example.jarambadriver.service.constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {

    //maps properti
    private GoogleMap mMap;
    Location curentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    LocationRequest locationRequest;

    Marker userLocationMarker;
    Circle userLocationAccuracy;

    private static final int REQUEST_CODE_LOCATION_PERMISSION =1;

    String nama, key;
    String trayek, id_trip, id_bus;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        nama = i.getStringExtra("nama");
        key = i.getStringExtra("key");
        trayek = i.getStringExtra("trayek");
        id_trip = i.getStringExtra("id_trip");
        id_bus = i.getStringExtra("id_bus");



        if(trayek==null){
            trayek = "Belum memilih trayek";
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();


        BottomNavigationView bottomNavigationView =  findViewById(R.id.menu_navigasi);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
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
                    case R.id.trip:
                        Intent intent = new Intent(HomeActivity.this, Trip_start.class);
                        intent.putExtra("nama", nama);
                        intent.putExtra("id_trip", id_trip);
                        intent.putExtra("key",key);
                        intent.putExtra("trayek", trayek);
                        intent.putExtra("id_bus", id_bus);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.profile:
                        Intent intent2 = new Intent(HomeActivity.this, ProfileDriverActivity.class);
                        intent2.putExtra("nama", nama);
                        intent2.putExtra("id_trip", id_trip);
                        intent2.putExtra("key",key);
                        intent2.putExtra("trayek",trayek);
                        intent2.putExtra("id_bus", id_bus);
                        startActivity(intent2);
                        finish();
                        break;
                }
                return false;
            }
        });

        locationRequest = new LocationRequest();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("LOCATION_RESULT", String.valueOf(locationResult.getLastLocation()));

            if(mMap != null){
                setUserLocationMarker(locationResult.getLastLocation());
            }


        }
    };

    private void setUserLocationMarker(Location location){

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if(userLocationMarker == null){
            //create new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bus));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            markerOptions.title("Trayek : "+ trayek);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

        }else {
            //use the prev creatd marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(location.getBearing());
            userLocationMarker.setTitle("Trayek : " + trayek);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        }

        if(userLocationAccuracy == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 0,0,255));
            circleOptions.fillColor(Color.argb(32, 0,0,255));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracy = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracy.setCenter(latLng);
            userLocationAccuracy.setRadius(location.getAccuracy());
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver Location");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("key", key);
        hashMap.put("gps", location.getLatitude() + ", " + location.getLongitude());

        ref.child(key).updateChildren(hashMap);
    }

    private void startLocationUpdates(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            startLocationUpdates();
        } else {
            //req permission
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void fetchLastLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                            curentLocation = location;
                            SupportMapFragment supportMapFragment = (SupportMapFragment)
                                    getSupportFragmentManager().findFragmentById(R.id.map);
                            supportMapFragment.getMapAsync(HomeActivity.this);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }



//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
//            startLocationServices();
//        } else {
//            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private boolean isLocationSrviceRunning() {
//        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//        if(activityManager !=  null){
//            for(ActivityManager.RunningServiceInfo service :  activityManager.getRunningServices(Integer.MAX_VALUE)){
//                if(LocationService.class.getName().equals(service.service.getClassName())){
//                    if(service.foreground){
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//        return false;
//    }
//
//    private void startLocationServices() {
//        if(!isLocationSrviceRunning()){
//            Intent intent = new Intent(getApplicationContext(), LocationService.class);
//            intent.setAction(constants.ACTION_START_LOCATION_SERVICE);
//            startService(intent);
//
//        }
//    }
//
//    private void stopLocationServices() {
//        if(isLocationSrviceRunning()){
//            Intent intent = new Intent(getApplicationContext(), LocationService.class);
//            intent.setAction(constants.ACTION_STOP_LOCATION_SERVICE);
//            startService(intent);
//
//        }
//    }
//
//    public void start(View view) {
//        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(
//                    HomeActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
//        } else {
//            startLocationServices();
//        }
//    }
//
//    public void finish(View view) {
//        stopLocationServices();
//    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        // Add a marker in Sydney and move the camera
//        LatLng itb = new LatLng(-6.891469, 107.610756);
//        mMap.addMarker(new MarkerOptions().position(itb).title("Institut Teknologi Bandung"));
//        mMap.setMyLocationEnabled(true);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itb , 14.0f));

        LatLng latLng = new LatLng(
                curentLocation.getLatitude(),
                curentLocation.getLongitude()
        );



//        MarkerOptions markerOptions = new MarkerOptions()
//                .position(latLng).title("Posisi anda saat ini").icon(
//                        BitmapDescriptorFactory.fromResource(R.drawable.marker_bus));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
//        mMap.addMarker(markerOptions);
//        mMap.setMyLocationEnabled(true);
    }
}
