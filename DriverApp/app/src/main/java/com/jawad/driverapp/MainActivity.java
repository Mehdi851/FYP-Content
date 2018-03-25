package com.jawad.driverapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.*;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap myGoogleMap;
    private Button button;
    private TextView textView;
    private EditText NameET;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public static  double lat;
    public static double log;
    public static Intent intent;
    public static String NAME ;
    //public static final String LOG = "com.jawad.assign03.LOG";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textview);

        intent=new Intent(this,MapsActivity.class);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

               // textView.append("\n "+location.getLatitude()+" "+ location.getLongitude());
                 lat =location.getLatitude();
                //get the longitude
                 log =location.getLongitude();

            }//end of onlocationchange

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };//end of locationListener
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            requestPermissions(new String[]{permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION, permission.INTERNET}, 10);
            return;
        } else {
            ConfugerButton();
        }


    }

    private void initMap() {
        MapFragment mapFragment =(MapFragment)getFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);
    }
    public void onMapReady(GoogleMap googleMap) {
        myGoogleMap = googleMap;
        goToLocationZoom(lat, log, 12,NAME);
        // Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        // Log.d(TAG, "onMapReady: map is ready");
        // mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        myGoogleMap.setMyLocationEnabled(true);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    ConfugerButton();
                }
        }
    }

    private void ConfugerButton() {
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NameET=(EditText)findViewById(R.id.NameET);
                NAME=NameET.getText().toString();
                initMap();
                Toast.makeText(MainActivity.this,"Lat="+lat+" Log="+log,Toast.LENGTH_LONG ).show();
                intent.putExtra("Latitude",lat);
                intent.putExtra("Longitude",log);
               // startActivity(intent);
            }
        });
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

    }
    private void goToLocationZoom(double lat, double lng, float zoom,String name) {
        LatLng ll=new LatLng(lat,lng);
        myGoogleMap.addMarker(new MarkerOptions().position(ll)
                .title(name));
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll,zoom);
        myGoogleMap.moveCamera(update);
    }

}
