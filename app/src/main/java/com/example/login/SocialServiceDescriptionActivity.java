package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class SocialServiceDescriptionActivity extends AppCompatActivity {

    private TextView nameOfProvider, typeOfService, dateOfService, timeOfService, descriptionOfService,addressOfService;

    private ImageButton backButton, navigationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_service_description);

        nameOfProvider = findViewById(R.id.provider_name);
        typeOfService = findViewById(R.id.type_service);
        dateOfService = findViewById(R.id.date_service);
        timeOfService = findViewById(R.id.time_service);
        descriptionOfService = findViewById(R.id.description_service);
        addressOfService = findViewById(R.id.address_service);
    }
}