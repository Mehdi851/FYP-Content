package com.jawad.driverapp;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.widget.Toolbar;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.Manifest.*;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap myGoogleMap;
    private Button button;
    private TextView textView;
    private EditText NameET;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle togglebtn;
    private Switch aSwitch;
    private String status = "Off";
    private LocationManager locationManager;
    private LocationListener locationListener;
    DatabaseReference databaseReference;
    AlertDialog alertDialog;
    public static double lat;
    public static double log;
    public static Intent intent;
    public static String NAME = "";
    public static String UserID = "";

    private Button logout;
    //public static final String LOG = "com.jawad.assign03.LOG";
    private void alert() {
//        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(MainActivity.this);
//        LayoutInflater inflater= getLayoutInflater();
//        final View dialogView=inflater.inflate(R.layout.update_layout,null);
//        dialogBuilder.setView(dialogView);

        // dialogBuilder.setTitle("Username");
//        alertDialog =dialogBuilder.create();
//        NameET = (EditText) findViewById(R.id.NameET);
//        button = (Button) findViewById(R.id.Save);
        aSwitch = (Switch) findViewById(R.id.Location_switch);
        //alertDialog.show();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (aSwitch.isChecked()) {
                    status = "On";
                } else {
                    status = "Off";
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alert();

        Toolbar toolbar=(Toolbar)findViewById(R.id.map_Toolbar);
        setSupportActionBar(toolbar);
//        logout=(Button) findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences sharedpref=getSharedPreferences("LoginPref",MODE_PRIVATE);
//                SharedPreferences.Editor editor=sharedpref.edit();
//
//                editor.putBoolean("isLogin",false);
//            }
//        });
       // ActionBar(toolbar);
//        setSupportActionBar(toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        togglebtn=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(togglebtn);
        togglebtn.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sharepref=getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
       // UserID=sharepref.getString("username","jawad");
//        boolean isLogin=sharepref.getBoolean("isLogin",false);
//        if (isLogin)
//        {
//            Intent i=new Intent(MainActivity.this,Login.class);
//            startActivity(i);
//        }
//        else{
//            Toast.makeText(this,"yess",Toast.LENGTH_LONG).show();
//        }

        logout=(Button) findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences sharedpref=getSharedPreferences("LoginPref",MODE_PRIVATE);
//                SharedPreferences.Editor editor=sharedpref.edit();
//                editor.putBoolean("isLogin",false);
                Intent i=new Intent(MainActivity.this,Login.class);
                startActivity(i);
            }
        });


        final Intent intent = getIntent();



        databaseReference = FirebaseDatabase.getInstance().getReference("Drivar");
        initMap();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lat = location.getLatitude();
                log = location.getLongitude();

                //saving lat lng into firebase using geofire
               UserID = intent.getStringExtra("username");
                if (!(UserID == "")) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Driver").child(UserID);
                    //databaseReference=FirebaseDatabase.getInstance().getReference("Driver");
                    //  DriverDatail driver= new DriverDatail("1",NAME,lat,log);
                    databaseReference.child("status").setValue(status);
                    databaseReference.child("Name").setValue(UserID);
                    databaseReference.child("lat").setValue(lat);
                    databaseReference.child("lng").setValue(log);

                }

            }//end of onlocationchange

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 0, locationListener);



    }//end of onCreate method
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

    }
    public void onMapReady(GoogleMap googleMap) {
        myGoogleMap = googleMap;

       // goToLocationZoom(lat, log, 12,NAME);
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
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//               // NameET=(EditText)findViewById(R.id.NameET);
//                NAME=NameET.getText().toString();
//
//                Toast.makeText(MainActivity.this,"Lat="+lat+" Log="+log,Toast.LENGTH_LONG ).show();
//               // intent.putExtra("Latitude",lat);
//                //intent.putExtra("Longitude",log);
//               // startActivity(intent);
//              //  alertDialog.dismiss();
//               // locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
//
//            }
//        });
//        locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);

    }
    //Action bar toogle functiotion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (togglebtn.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLocationZoom(double lat, double lng, float zoom, String name) {
        LatLng ll=new LatLng(lat,lng);
        myGoogleMap.addMarker(new MarkerOptions().position(ll)
                .title(name));
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(ll,zoom);
        myGoogleMap.moveCamera(update);
    }


}
