package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Main3Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mAuth=FirebaseAuth.getInstance();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser()==null) {
                    startActivity(new Intent(Main3Activity.this, MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(Main3Activity.this,Mainpage.class));
                }
            }
        },1100);
    }

}
