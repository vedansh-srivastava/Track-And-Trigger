package com.example.tracktrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class KYC extends AppCompatActivity {
    private Button Clicksubmit;
private EditText user_name,phone_no,gmail_id;
private Spinner prof;
private FirebaseDatabase db=FirebaseDatabase.getInstance();
private DatabaseReference root=db.getReference().child("USERS");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_y_c);

        prof = findViewById(R.id.Profession);
        user_name = findViewById(R.id.userName);
        phone_no = findViewById(R.id.phoneNo);
        gmail_id = findViewById(R.id.gmail);
        Clicksubmit = findViewById(R.id.onSubmit);

        phone_no.setText(enter_mobile_otp.VerfiedNo);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            user_name.setText(personName);
            gmail_id.setText(personEmail);
        }

        Spinner spinner = (Spinner) findViewById(R.id.Profession);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.professions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Clicksubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=user_name.getText().toString();
                String phoneNO=phone_no.getText().toString();
                String gmail=gmail_id.getText().toString();
                String PROFESSION = prof.getSelectedItem().toString();
                HashMap<String,String> userMap = new HashMap<>();
                userMap.put("USER-NAME",userName);
                userMap.put("PHONE NUMBER",phoneNO);
                userMap.put("GMAIL ID",gmail);
                userMap.put("PROFESSION",PROFESSION);
                root.push().setValue(userMap);
                Intent intent = new Intent(KYC.this,HomeMakerDashboard.class);

                startActivity(intent);
            }
        });
    }
}