package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Main2Activity extends AppCompatActivity {

    EditText p,o;
    TextView x,rotp,cn;
    Button s;
    String ph,a="+91",phone,code,str;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseAuth mAuth;
    ProgressBar bar;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        p=(EditText)findViewById(R.id.editText);
        o=(EditText)findViewById(R.id.editText2);
        cn=(TextView)findViewById(R.id.textView5);
        cn.setVisibility(View.INVISIBLE);
        bar=(ProgressBar)findViewById(R.id.progressBar2);
        x=(TextView)findViewById(R.id.textView6);
        rotp=(TextView)findViewById(R.id.textView7);
        s=(Button)findViewById(R.id.button);
        mAuth=FirebaseAuth.getInstance();
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (o.getVisibility() == View.INVISIBLE) {
                    str=p.getText().toString();
                    ph = a+str;
                    if (TextUtils.isEmpty(str)) {
                        p.setError("Required");
                    }
                    else {
                        phone=p.getText().toString();
                        s.setText("Sending Code ...");
                        bar.setVisibility(View.VISIBLE);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                ph,        // Phone number to verify
                                10,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                Main2Activity.this,               // Activity (for callback binding)
                                mCallbacks);
                    }
                }
                else {
                    code=o.getText().toString();
                    if(TextUtils.isEmpty(code)){
                        o.setError("Required");
                    }
                    else{
                        bar.setVisibility(View.VISIBLE);
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                        signInWithPhoneAuthCredential(credential);
                    }
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
                s.setText("Get  Verification Code");
                bar.setVisibility(View.INVISIBLE);
                Toast.makeText(Main2Activity.this,"Please Retry",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                bar.setVisibility(View.INVISIBLE);
                o.setVisibility(View.VISIBLE);
                o.setText("");
                p.setVisibility(View.INVISIBLE);
                x.setVisibility(View.VISIBLE);
                x.setText(str);
                cn.setVisibility(View.VISIBLE);
                s.setText("Verify");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        rotp.setVisibility(View.VISIBLE);
                    }
                },10000);
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final DatabaseReference rootref;
                            rootref=FirebaseDatabase.getInstance().getReference();
                            rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(!(dataSnapshot.child("users").child(phone).exists())){
                                        bar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(Main2Activity.this,"Verified",Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(Main2Activity.this,Registration.class);
                                        i.putExtra("mkey",str);
                                        startActivity(i);
                                        finish();
                                    }
                                    else{
                                        bar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(Main2Activity.this,"Verified",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Main2Activity.this,Mainpage.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            Toast.makeText(Main2Activity.this,"Error: Wrong OTP",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void number(View view) {
        p.setVisibility(View.VISIBLE);
        p.setText("");
        bar.setVisibility(View.INVISIBLE);
        o.setVisibility(View.INVISIBLE);
        s.setText("Get  Verification Code");
        x.setVisibility(View.INVISIBLE);
        cn.setVisibility(View.INVISIBLE);
        rotp.setVisibility(View.INVISIBLE);
    }

    public void resend(View view) {
        o.setVisibility(View.INVISIBLE);
        s.setText("Sending Code ...");
        bar.setVisibility(View.VISIBLE);
        rotp.setVisibility(View.INVISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                ph,        // Phone number to verify
                10,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                Main2Activity.this,               // Activity (for callback binding)
                mCallbacks);
    }
}

