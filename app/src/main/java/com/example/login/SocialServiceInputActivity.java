package com.example.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SocialServiceInputActivity extends AppCompatActivity {

    private Spinner spinner,timeSpinner;
    private String typeOfSocial="";
    private EditText customSocial;
    private EditText serviceTime,serviceDate;
//    private RadioGroup radioGroup;
//    private RadioButton radioButton;
//    private String safetyType="Yes";
    private EditText socialText, textViewAddressSocial, numberOfPeople;
    private String phoneNum;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private double firebaseLat, firebaseLng;
    private String firebaseAddress;


    private String socialTextString="";


    private String currentDate, currentTime,serviceDateString="", serviceTimeString="",serviceTimeStringType="";





//    FusedLocationProviderClient client;


    Button getCurrentLocationButton;
    ProgressBar progressBar;
    private final static int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private final static int GPS_REQUEST_CODE = 9003;
    private ResultReceiver resultReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_service_input);

        serviceDate = findViewById(R.id.editTextDate);
        serviceTime = findViewById(R.id.editTextTime);



        getCurrentLocationButton = findViewById(R.id.button_get_current_location_social);


        resultReceiver = new AddressResultReceiver(new Handler());
        getCurrentLocationButton = findViewById(R.id.button_get_current_location_social);

        textViewAddressSocial = findViewById(R.id.textAddress_social);

        progressBar = findViewById(R.id.progress_dialog_social);
        numberOfPeople = findViewById(R.id.editTextNumberOfPeople);




        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();

        phoneNum = mAuth.getCurrentUser().getPhoneNumber();

        phoneNum = phoneNum.substring(3);
//        radioGroup = findViewById(R.id.radio_group);



        socialText = findViewById(R.id.social_text);



        getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGPSEnabled()){
                    Toast.makeText(SocialServiceInputActivity.this, "You are good to go!", Toast.LENGTH_SHORT).show();
                }
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SocialServiceInputActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    getCurrentLocation();
                }
            }
        });



        spinner = (Spinner) findViewById(R.id.spinner_social_type);

        timeSpinner = (Spinner) findViewById(R.id.spinner_am_pm);
        customSocial = findViewById(R.id.custom_social_type);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type0fService));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);


        ArrayAdapter<String> my1Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.typeOfTime));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(my1Adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


                if(position==1)
                {
                    customSocial.setVisibility(View.INVISIBLE);
                    typeOfSocial = "Providing Food";
                }

                if(position==2)
                {
                    customSocial.setVisibility(View.INVISIBLE);
                    typeOfSocial = "Providing Essential Items other than Food";
                }

                if(position==3)
                {
                    customSocial.setVisibility(View.INVISIBLE);
                    typeOfSocial = "Providing Work to Poor or Monetary help";
                }

                if(position==4)
                {
                    customSocial.setVisibility(View.INVISIBLE);
                    typeOfSocial = "Providing Masks or Sanitizers";
                }

                if(position==5)
                {
                    customSocial.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SocialServiceInputActivity.this, "Please select the type", Toast.LENGTH_SHORT).show();
            }
        });



        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


                if(position==0)
                {
                    serviceTimeStringType = "A.M.";
                }

                if(position==1)
                {
                    serviceTimeStringType = "P.M.";
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SocialServiceInputActivity.this, "Please select the correct time", Toast.LENGTH_SHORT).show();
            }
        });



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
        progressBar.setVisibility(View.VISIBLE);

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
        LocationServices.getFusedLocationProviderClient(SocialServiceInputActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {


                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(SocialServiceInputActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestlocationIndex).getLatitude();

                            double longitude =
                                    locationResult.getLocations().get(latestlocationIndex).getLongitude();

                            firebaseLat = latitude;
                            firebaseLng = longitude;

                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);






                            fetchAddressFromLatLong(location);


                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
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

    public void tellAboutAddress(View view)
    {
        Toast.makeText(this, "In case the Address does not match the address of your current location please mention the required address manually", Toast.LENGTH_LONG).show();
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
                textViewAddressSocial.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            }
            else{
                Toast.makeText(SocialServiceInputActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(View.GONE);
        }
    }



//    public void checkbox(View view)
//    {
//        int radioId = radioGroup.getCheckedRadioButtonId();
//        radioButton = findViewById(radioId);
//
//        safetyType = radioButton.getText().toString();
//
//    }

    public void submitSocial(View view)
    {
        UpdateSettings();
    }

    private void UpdateSettings()
    {
        socialTextString = socialText.getText().toString();

        serviceDateString = serviceDate.getText().toString();
        serviceTimeString = serviceTime.getText().toString();

        String NumberOfPeopleFirebase = numberOfPeople.getText().toString();

        if(TextUtils.isEmpty(socialTextString))
        {
            Toast.makeText(this, "Please write a description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(serviceTimeString)){
            Toast.makeText(this, "Select the time please!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(serviceDateString)){
            Toast.makeText(this, "Select the date please!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(serviceTimeStringType)){
            Toast.makeText(this, "Select time correctly!", Toast.LENGTH_SHORT).show();
        }

//        else if(TextUtils.isEmpty(typeOfSocial))
//        {
//            Toast.makeText(this, "Please let us know if you are safe", Toast.LENGTH_SHORT).show();
//        }


        else
        {

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat curentDateFormat = new SimpleDateFormat("MMM dd,  yyyy") ;
            currentDate=curentDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a") ;
            currentTime=currentTimeFormat.format(calForTime.getTime());

            String SocialKey = RootRef.child(phoneNum).child("Social Service").push().getKey();

            firebaseAddress = textViewAddressSocial.getText().toString();



            if(typeOfSocial.equals(""))
            {
                typeOfSocial = customSocial.getText().toString();
            }

            HashMap<String, Object> SocialMap = new HashMap<>();
            SocialMap.put("uid", currentUserId);
            SocialMap.put("date", currentDate);
            SocialMap.put("time", currentTime);
//                SocialMap.put("safety type", safetyType);
            SocialMap.put("Social category", typeOfSocial);
            SocialMap.put("user description", socialTextString);

            SocialMap.put("latitude", firebaseLat);
            SocialMap.put("longitude", firebaseLng);
            SocialMap.put("user_address", firebaseAddress);
            SocialMap.put("Number of People valid", NumberOfPeopleFirebase);
            SocialMap.put("Date of Service", serviceDateString);
            SocialMap.put("Time of Service", serviceTimeString + " " + serviceTimeStringType);







            RootRef.child("users").child(phoneNum).child("Social Service").child(SocialKey).updateChildren(SocialMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                SendUserToMainPage();
                            }

                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(SocialServiceInputActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//            RootRef.child("users").child(phoneNum).child("Safety").setValue(safetyType);


        }




    }

    private void SendUserToMainPage()
    {
        Intent mainIntent = new Intent(SocialServiceInputActivity.this,Mainpage.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
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