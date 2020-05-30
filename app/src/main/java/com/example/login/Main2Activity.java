package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {

    EditText p,o,n;
    Button s,v;
    String ph,phone,name,code;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        p=(EditText)findViewById(R.id.editText);
        n=(EditText)findViewById(R.id.editText3);
        o=(EditText)findViewById(R.id.editText2);
        s=(Button)findViewById(R.id.button);
        v=(Button)findViewById(R.id.button2);
        mAuth=FirebaseAuth.getInstance();
      s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ph=p.getText().toString();
                if(TextUtils.isEmpty(ph)){
                    Toast.makeText(Main2Activity.this,"Enter Phone Number",Toast.LENGTH_LONG).show();
                }
                else{
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            ph,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            Main2Activity.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(Main2Activity.this,"Enter correct number and OTP",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText(Main2Activity.this,"Code has been sent",Toast.LENGTH_LONG).show();
            }
        };

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=o.getText().toString();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(Main2Activity.this,"Please Enter OTP",Toast.LENGTH_LONG).show();
                }
                else{
                    phone=p.getText().toString();
                    name=n.getText().toString();
                    final DatabaseReference rootref;
                    rootref=FirebaseDatabase.getInstance().getReference();
                    rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!(dataSnapshot.child("Users").child(phone).exists())){
                                HashMap<String, Object> userdataMap=new HashMap<>();
                                userdataMap.put("phone", phone);
                                userdataMap.put("name", name);
                                rootref.child("Users").child(phone).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                                            signInWithPhoneAuthCredential(credential);
                                        }
                                        else{
                                            Toast.makeText(Main2Activity.this,"Error",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                          }
                            else{
                                Toast.makeText(Main2Activity.this,"This "+phone+" already exists.",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Main2Activity.this,"Congo",Toast.LENGTH_LONG).show();
                        }
                        else {
                            String m= task.getException().toString();
                            Toast.makeText(Main2Activity.this,"Error :" + m,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void back1(View view) {
        startActivity(new Intent(Main2Activity.this,MainActivity.class));
    }
}
