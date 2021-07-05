package com.example.tracktrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class HomeMakerDashboard extends AppCompatActivity {
private RecyclerView recyclerView;
private TextView dashboardTitle;
private FirebaseFirestore db;
private FirebaseAuth mAuth;
private MyAdapterDashboard adapter;
private ImageView log_out;
private List<ModelForDashboard> list;

    @Override
    public void onResume(){
        super.onResume();
        showData();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maker_dashboard);

        dashboardTitle=findViewById(R.id.dashboardTitle);
        SpannableString content = new SpannableString("DASHBOARD");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        dashboardTitle.setText(content);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        log_out=findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeMakerDashboard.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        db=FirebaseFirestore.getInstance();
        list = new ArrayList<>();
        adapter= new MyAdapterDashboard(this,list);
        recyclerView.setAdapter(adapter);
        showData();
    }
    public void showData(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String s= currentUser.getUid();
        db.collection(s+" Dashboard").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            ModelForDashboard model =new ModelForDashboard(snapshot.getString("Name of New Dashboard Field"));
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void addNewDashboardField(View view){
        Intent intent = new Intent(HomeMakerDashboard.this,AddDashboardField.class);
        startActivity(intent);
    }

    public void opengroceries(View view){
        Intent intent = new Intent(HomeMakerDashboard.this,Quantity_desc_field.class);
        String s = "Groceries";
        intent.putExtra("Dashboard Field Name",s);
        startActivity(intent);
    }

    public void opentodolist(View view){
        Intent intent = new Intent(HomeMakerDashboard.this,ToDoListActivity.class);
        startActivity(intent);
    }

    public void openhomebills(View view){
        Intent intent = new Intent(HomeMakerDashboard.this,Quantity_desc_field.class);
        String s = "Home Bills";
        intent.putExtra("Dashboard Field Name",s);
        startActivity(intent);
    }
    public void openpersonaldiary(View view){
        Intent intent = new Intent(HomeMakerDashboard.this,Quantity_desc_field.class);
        String s = "Personal Diary";
        intent.putExtra("Dashboard Field Name",s);
        startActivity(intent);
    }
}