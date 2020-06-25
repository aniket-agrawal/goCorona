package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SocialServiceDescriptionActivity extends AppCompatActivity {

    private TextView nameOfProvider, typeOfService, dateOfService, timeOfService, descriptionOfService,addressOfService;
    String id,name,phone,date,time,seat,description,address;
    DatabaseReference reference;

    private ImageButton backButton, navigationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_service_description);
        Intent i = getIntent();
        id = i.getStringExtra("id");
        System.out.println(id);
        reference = FirebaseDatabase.getInstance().getReference().child("Social Service").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("User Name").getValue().toString();
                System.out.println(name);
                phone = dataSnapshot.child("Phone Number").getValue().toString();
                date = dataSnapshot.child("Date of Service").getValue().toString();
                date= date.substring(0,6) + "20" + date.substring(6);
                time = dataSnapshot.child("Time of Service").getValue().toString();
                if(time.charAt(1)==':'){
                    time = '0' + time;
                }
                seat = dataSnapshot.child("Number of People valid").getValue().toString();
                description = dataSnapshot.child("user description").getValue().toString();
                address = dataSnapshot.child("user_address").getValue().toString();
                nameOfProvider.setText(name);
                dateOfService.setText(date);
                timeOfService.setText(time);
                descriptionOfService.setText(description);
                addressOfService.setText(address);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        backButton = (ImageButton)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialServiceDescriptionActivity.this,Mainpage.class));
                finish();
            }
        });

        nameOfProvider = findViewById(R.id.provider_name);
        typeOfService = findViewById(R.id.type_service);
        dateOfService = findViewById(R.id.date_service);
        timeOfService = findViewById(R.id.time_service);
        descriptionOfService = findViewById(R.id.description_service);
        addressOfService = findViewById(R.id.address_service);
    }
}