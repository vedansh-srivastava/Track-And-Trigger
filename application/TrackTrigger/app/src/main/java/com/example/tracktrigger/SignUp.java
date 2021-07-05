package com.example.tracktrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
private EditText Username,PhoneNo,gmailID,password;
private Spinner profession;
private Button SignUpButton;
private FirebaseAuth mauth;
private FirebaseDatabase db=FirebaseDatabase.getInstance();
private DatabaseReference root=db.getReference().child("USERS");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner spinner = (Spinner) findViewById(R.id.Profession);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.professions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Username = (EditText) findViewById(R.id.userName);
        PhoneNo = (EditText) findViewById(R.id.phoneNo);
        gmailID = (EditText) findViewById(R.id.gmail);
        password = (EditText) findViewById(R.id.password);
        profession = (Spinner) findViewById(R.id.Profession);

        SignUpButton = (Button) findViewById(R.id.signUpbutton);
        mauth=FirebaseAuth.getInstance();
    }

    private Boolean isValidPassword(String password)
    {

        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(regex);
        if (password == null) {
            return false;
        }
        Matcher m = p.matcher(password);
        return m.matches();
    }


    public void registerUser(View view){
        String user_name = Username.getText().toString();
        String phone_num = PhoneNo.getText().toString();
        String gmail = gmailID.getText().toString();
        String Password = password.getText().toString();
        String prof = profession.getSelectedItem().toString();
        if(user_name.isEmpty()){
            Username.setError("Username is required!");
            Username.requestFocus();
            return;
        }

        if(phone_num.isEmpty()){
            PhoneNo.setError("Phone Number is required!");
            PhoneNo.requestFocus();
            return;
        }

        if(gmail.isEmpty()){
            gmailID.setError("E-mail is required!");
            gmailID.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }


        if(isValidPassword(Password)==false){
            password.setError("It should contain at least 8 characters and at most 20 characters,\nat least one digit,\nat least one upper case alphabet,\nat least one lower case alphabet,\nat least one special character which includes !@#$%&*()-+=^,\ndoesn’t contain any white space.");
            password.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(phone_num).matches()){
            PhoneNo.setError("Enter a valid phone number");
            PhoneNo.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(gmail).matches()){
            gmailID.setError("Please provide a valid email!");
            gmailID.requestFocus();
            return;
        }
        mauth.createUserWithEmailAndPassword(gmail,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(SignUp.this,HomeMakerDashboard.class);
                        HashMap<String,String> userMap = new HashMap<>();
                        userMap.put("USER-NAME",user_name);
                        userMap.put("PHONE NUMBER",phone_num);
                        userMap.put("GMAIL ID",gmail);
                        userMap.put("PROFESSION",prof);
                        root.push().setValue(userMap);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        });

    }
}