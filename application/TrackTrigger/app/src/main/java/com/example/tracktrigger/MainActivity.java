package com.example.tracktrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this,HomeMakerDashboard.class);
            startActivity(intent);
            finish();
        }
    }
    public void opensignIn(View view){
        Intent i = new Intent(MainActivity.this,SignIn.class);
        startActivity(i);
    }
    public void opensignUp(View view){
        Intent i = new Intent(MainActivity.this,SignUp.class);
        startActivity(i);
    }

}