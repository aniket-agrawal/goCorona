package com.example.login;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SocialServiceDescriptionActivity extends AppCompatActivity {

    private TextView nameOfProvider, typeOfService, dateOfService, timeOfService, descriptionOfService,addressOfService,phone_number,seat_available;
    double lat,lang;
    String id,name,phone,date,time,seat,description,type,address,currentUserAddress="";
    DatabaseReference reference;

    private ImageButton backButton, navigationButton;


    private final static int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private final static int GPS_REQUEST_CODE = 9003;
    private ResultReceiver resultReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_service_description);
        Intent i = getIntent();
        id = i.getStringExtra("id");
        System.out.println(id);
        reference = FirebaseDatabase.getInstance().getReference().child("Social Service").child(id);
        resultReceiver = new AddressResultReceiver(new Handler());

        backButton = (ImageButton)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newFragment = new Fragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment, and add the transaction to the back stack
                transaction.replace(R.id.nav_gallery, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                //startActivity(new Intent(SocialServiceDescriptionActivity.this,Mainpage.class));
                //finish();
            }
        });

        nameOfProvider = findViewById(R.id.provider_name);
        typeOfService = findViewById(R.id.type_service);
        dateOfService = findViewById(R.id.date_service);
        seat_available = findViewById(R.id.seats_available);
        timeOfService = findViewById(R.id.time_service);
        phone_number = findViewById(R.id.contact_number);
        descriptionOfService = findViewById(R.id.description_service);
        addressOfService = findViewById(R.id.address_service);
        navigationButton = findViewById(R.id.navigation_button);
        navigationButton.setVisibility(View.GONE);

        boolean b = isGPSEnabled();
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SocialServiceDescriptionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }

        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentUserAddress.equals("") || address.equals("")){
                    Toast.makeText(SocialServiceDescriptionActivity.this, "Some Error occurred ,please retry!", Toast.LENGTH_SHORT).show();
                }
                else{
                    DisplayTrack(currentUserAddress,address);
                }
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("User Name").getValue().toString();
                phone = dataSnapshot.child("Phone Number").getValue().toString();
                type = dataSnapshot.child("Social category").getValue().toString();
                date = dataSnapshot.child("Date of Service").getValue().toString();
                time = dataSnapshot.child("Time of Service").getValue().toString();
                lat = dataSnapshot.child("latitude").getValue(double.class);
                lang = dataSnapshot.child("longitude").getValue(double.class);
                if(time.charAt(1)==':'){
                    time = '0' + time;
                }
                seat = dataSnapshot.child("Number of People valid").getValue().toString();
                description = dataSnapshot.child("user description").getValue().toString();
                address = dataSnapshot.child("user_address").getValue().toString();
                nameOfProvider.setText("Name of Provider - " + name);
                dateOfService.setText("Date - " + date);
                timeOfService.setText("Time - " + time);
                typeOfService.setText(type);
                descriptionOfService.setText(description);
                addressOfService.setText(address);
                phone_number.setText("Contact Number of Provider - " + phone);
                seat_available.setText("Maximum seats - " + seat);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void DisplayTrack(String currentUserAddress, String address)
    {
        try{
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + currentUserAddress + "/"+ address);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            intent.setPackage("com.google.android.apps.maps");

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }

        catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
//                getMap();
            } else {
                Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getCurrentLocation() {


        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


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
        LocationServices.getFusedLocationProviderClient(SocialServiceDescriptionActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {


                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(SocialServiceDescriptionActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestlocationIndex).getLatitude();

                            double longitude =
                                    locationResult.getLocations().get(latestlocationIndex).getLongitude();


                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);






                            fetchAddressFromLatLong(location);


                        }



                    }
                }, Looper.getMainLooper());

    }

    private void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver{


        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == Constants.SUCCESS_RESULT)
            {
                currentUserAddress = (resultData.getString(Constants.RESULT_DATA_KEY));
                navigationButton.setVisibility(View.VISIBLE);
            }
            else{
                Toast.makeText(SocialServiceDescriptionActivity.this, "Error, please try again", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private boolean isGPSEnabled(){

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(providerEnabled){
            return true;
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("GPS Permissions")
                    .setMessage("To Improve accuracy please set your settings to high accuracy.")
                    .setPositiveButton("Yes",(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, GPS_REQUEST_CODE );
                        }
                    }))
                    .show();
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GPS_REQUEST_CODE){
            if(isGPSEnabled()){
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Current Location may have errors!", Toast.LENGTH_SHORT).show();
            }
        }
    }



}