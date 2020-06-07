package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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
    private EditText customFeedback;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String safetyType;
    private EditText feedbackText;

    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private String feedBackTextString="";

    private String currentDate, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_form);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();

        radioGroup = findViewById(R.id.radio_group);



        feedbackText = findViewById(R.id.feedback_text);

        spinner = (Spinner) findViewById(R.id.spinner_type_of_feedback);
        customFeedback = findViewById(R.id.custom_feedback_type);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.typeoffeedback));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


                if(position==1)
                {
                    customFeedback.setVisibility(View.INVISIBLE);
                        typeOfFeedBack = "Reporting a nearby corona case";
                }

                if(position==2)
                {
                    customFeedback.setVisibility(View.INVISIBLE);
                        typeOfFeedBack = "Need Govt. support";
                }

                if(position==3)
                {
                    customFeedback.setVisibility(View.INVISIBLE);
                    typeOfFeedBack = "Queries Regarding Lockdown";
                }

                if(position==4)
                {
                    customFeedback.setVisibility(View.INVISIBLE);
                    typeOfFeedBack = "Lack of Medical Facilities";
                }

                if(position==5)
                {
                    customFeedback.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(FeedBackFormActivity.this, "Please select the type", Toast.LENGTH_SHORT).show();
            }
        });


        if(typeOfFeedBack.equals(""))
        {
            typeOfFeedBack = customFeedback.getText().toString();
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

        else if(TextUtils.isEmpty(typeOfFeedBack))
        {
            Toast.makeText(this, "Please mention a feedback type", Toast.LENGTH_SHORT).show();
        }
        else
            {

                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat curentDateFormat = new SimpleDateFormat("MMM dd,  yyyy") ;
                currentDate=curentDateFormat.format(calForDate.getTime());

                Calendar calForTime = Calendar.getInstance();
                SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a") ;
                currentTime=currentTimeFormat.format(calForTime.getTime());

                String feedbackKey = RootRef.child("Users").child(currentUserId).child("feedback").push().getKey();


                HashMap<String, Object> feedbackMap = new HashMap<>();
                feedbackMap.put("uid", currentUserId);
                feedbackMap.put("date", currentDate);
                feedbackMap.put("time", currentTime);
                feedbackMap.put("safety type", safetyType);
                feedbackMap.put("feedback category", typeOfFeedBack);
                feedbackMap.put("user feedback", feedBackTextString);



                RootRef.child("Users").child(currentUserId).child("feedback").child(feedbackKey).updateChildren(feedbackMap)
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
                                        Toast.makeText(FeedBackFormActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });


            }




    }

    private void SendUserToMainPage()
    {
        Intent mainIntent = new Intent(FeedBackFormActivity.this,Mainpage.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}