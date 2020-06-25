package com.example.login;

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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class FeedBackFormActivity extends AppCompatActivity {

    private Spinner spinner;
    private String typeOfFeedBack="";
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String safetyType="Yes";
    private EditText feedbackText, textViewAddress;
    private String phoneNum;
    private Button submitButton;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private double firebaseLat, firebaseLng;
    private String firebaseAddress;


    private String feedBackTextString="";


    private String currentDate, currentTime;

    private ImageButton backButton;

    private final static int GPS_REQUEST_CODE = 9003;





//    FusedLocationProviderClient client;


    Button getCurrentLocationButton;
    ProgressBar progressBar,progressBarSubmit;
    private final static int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ResultReceiver resultReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);

        backButton = findViewById(R.id.back_button_feedback);
        submitButton = findViewById(R.id.submit_feedback_button);


        resultReceiver = new AddressResultReceiver(new Handler());
        getCurrentLocationButton = findViewById(R.id.button_get_current_location);

        textViewAddress = findViewById(R.id.textAddress);

        progressBar = findViewById(R.id.progress_dialog);
        progressBarSubmit = findViewById(R.id.progress_dialog_submit_feedback);




        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();

        phoneNum = mAuth.getCurrentUser().getPhoneNumber();

        phoneNum = phoneNum.substring(3);
        radioGroup = findViewById(R.id.radio_group);



        feedbackText = findViewById(R.id.feedback_text);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToMainPage();
            }
        });


        getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGPSEnabled()){
                    System.out.println("You are good to go!");
                }
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FeedBackFormActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    getCurrentLocation();
                }
            }
        });



        spinner = (Spinner) findViewById(R.id.spinner_type_of_feedback);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.typeoffeedback));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


//                if(position==1)
//                {
//                        typeOfFeedBack = "Reporting a nearby corona case";
//                }
//
//                if(position==2)
//                {
//                        typeOfFeedBack = "Need Govt. support";
//                }
//
//                if(position==3)
//                {
//                    typeOfFeedBack = "Queries Regarding Lockdown";
//                }
//
//                if(position==4)
//                {
//                    typeOfFeedBack = "Lack of Medical Facilities";
//                }
//
//                if(position==5)
//                {
//                    typeOfFeedBack = "Custom";
//                }

                typeOfFeedBack = parent.getItemAtPosition(position).toString();




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(FeedBackFormActivity.this, "Please select the type", Toast.LENGTH_SHORT).show();
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
        LocationServices.getFusedLocationProviderClient(FeedBackFormActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {


                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(FeedBackFormActivity.this)
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

    private class AddressResultReceiver extends ResultReceiver{


        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == Constants.SUCCESS_RESULT)
            {
                textViewAddress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            }
            else{
                Toast.makeText(FeedBackFormActivity.this, resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(View.GONE);
        }
    }



    public void checkbox(View view)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        safetyType = radioButton.getText().toString();

    }

    public void submitFeedback(View view)
    {

        UpdateSettings();
    }

    private void UpdateSettings()
    {
        feedBackTextString = feedbackText.getText().toString();

        if(TextUtils.isEmpty(feedBackTextString))
        {
            Toast.makeText(this, "Please write a feedback", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(safetyType))
        {
            Toast.makeText(this, "Please let us know if you are safe", Toast.LENGTH_SHORT).show();
        }


        else
            {

                submitButton.setVisibility(View.INVISIBLE);
                progressBarSubmit.setVisibility(View.VISIBLE);
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat curentDateFormat = new SimpleDateFormat("MMM dd,  yyyy") ;
                currentDate=curentDateFormat.format(calForDate.getTime());

                Calendar calForTime = Calendar.getInstance();
                SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a") ;
                currentTime=currentTimeFormat.format(calForTime.getTime());

                String feedbackKey = RootRef.child("users").child(phoneNum).child("feedback").push().getKey();

                firebaseAddress = textViewAddress.getText().toString();


                HashMap<String, Object> feedbackMap = new HashMap<>();
                feedbackMap.put("uid", currentUserId);
                feedbackMap.put("date", currentDate);
                feedbackMap.put("time", currentTime);
//                feedbackMap.put("safety type", safetyType);
                feedbackMap.put("feedback category", typeOfFeedBack);
                feedbackMap.put("user feedback", feedBackTextString);

                feedbackMap.put("latitude", firebaseLat);
                feedbackMap.put("longitude", firebaseLng);
                feedbackMap.put("user_address", firebaseAddress);







                RootRef.child("users").child(phoneNum).child("feedback").child(feedbackKey).updateChildren(feedbackMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(FeedBackFormActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                                    SendUserToMainPage();
                                }

                                else
                                    {
                                        String message = task.getException().toString();
                                        Toast.makeText(FeedBackFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                        progressBarSubmit.setVisibility(View.GONE);
                                        submitButton.setVisibility(View.VISIBLE);
                                    }
                            }
                        });
                RootRef.child("users").child(phoneNum).child("Safety").setValue(safetyType);


            }




    }

    private void SendUserToMainPage()
    {
        Intent mainIntent = new Intent(FeedBackFormActivity.this,Mainpage.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    public void tellAboutAddress(View view)
    {
        Toast.makeText(this, "In case the Address does not match the address of your current location please mention the required address manually", Toast.LENGTH_LONG).show();
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