package com.example.tracktrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddDashboardField extends AppCompatActivity {
private EditText NewDashboardField;
private Button NewDashboardFieldButton;
private FirebaseFirestore db;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dashboard_field);
        NewDashboardField= findViewById(R.id.NewDashboardField);
        NewDashboardFieldButton=findViewById(R.id.NewDashboardFieldButton);
        db=FirebaseFirestore.getInstance();
        NewDashboardFieldButton.setOnClickListener(v -> {
            String newDashboardField=NewDashboardField.getText().toString();
            String id = newDashboardField+" Dashboard Field";
            savetoFireStore(id,newDashboardField);
        });
    }
    private void savetoFireStore(String id,String newDashboardField){
        if(!newDashboardField.isEmpty()){
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String s= currentUser.getUid();
            HashMap<String,String> map= new HashMap<>();
            map.put("Name of New Dashboard Field",newDashboardField);
            db.collection(s+" Dashboard").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //show all activity
                               //HomeMakerDashboard.showData();
                                startActivity(new Intent(AddDashboardField.this,HomeMakerDashboard.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddDashboardField.this, "Failed to add Dashboard Field", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}