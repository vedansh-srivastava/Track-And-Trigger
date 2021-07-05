package com.example.tracktrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        final EditText inputMobile = findViewById(R.id.mobileNO);
        Button buttonGetOTP = findViewById(R.id.sendOTP);

        final ProgressBar progressBar = findViewById(R.id.progbar);

        buttonGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(inputMobile.getText().toString().trim().isEmpty()){
                   Toast.makeText(VerifyPhoneNo.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                   inputMobile.requestFocus();
                   return;
               }
               progressBar.setVisibility(View.VISIBLE);
               buttonGetOTP.setVisibility(View.INVISIBLE);

               PhoneAuthProvider.getInstance().verifyPhoneNumber(
                      "+91"+inputMobile.getText().toString(),
                       60,
                       TimeUnit.SECONDS,
                       VerifyPhoneNo.this,
                       new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                           @Override
                           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                               progressBar.setVisibility(View.GONE);
                               buttonGetOTP.setVisibility(View.VISIBLE);
                           }

                           @Override
                           public void onVerificationFailed(@NonNull FirebaseException e) {
                               progressBar.setVisibility(View.GONE);
                               buttonGetOTP.setVisibility(View.VISIBLE);
                               Toast.makeText(VerifyPhoneNo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                               progressBar.setVisibility(View.GONE);
                               buttonGetOTP.setVisibility(View.VISIBLE);
                               Intent intent = new Intent(getApplicationContext(),enter_mobile_otp.class);
                               intent.putExtra("mobile",inputMobile.getText().toString());
                               intent.putExtra("verificationId",verificationId);
                               startActivity(intent);
                           }
                       }
               );

            }
        });
    }
}