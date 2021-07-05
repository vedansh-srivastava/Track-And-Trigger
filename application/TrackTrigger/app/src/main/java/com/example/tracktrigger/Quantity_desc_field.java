package com.example.tracktrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Quantity_desc_field extends AppCompatActivity {

private RecyclerView recyclerView;
private FirebaseAuth auth;
public  String s;
private  FirebaseFirestore db;
private  MyAdapterQuantityDesc adapter;
private  List<ModelForQuantityDesc> list;
private Button button2;

    @Override
    public void onResume(){
        super.onResume();
        showData(s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity_desc_field);



        button2=findViewById(R.id.button2);
        recyclerView=findViewById(R.id.recyclerViewQuantity_desc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db=FirebaseFirestore.getInstance();
        list=new ArrayList<>();
        adapter=new MyAdapterQuantityDesc(this,list);

        Intent intent=getIntent();

         s=intent.getStringExtra("Dashboard Field Name");
        TextView quantity_desc_field=findViewById(R.id.quantity_desc_field);
        quantity_desc_field.setText(s);

        //Intent intent3=new Intent(Quantity_desc_field.this,Update_quantity_desc_field.class);
        //intent3.putExtra("Field Name Update",quantity_desc_field.getText());

        recyclerView.setAdapter(adapter);

        ItemTouchHelper touchHelper= new ItemTouchHelper(new TouchHelperQuantityDesc(adapter));
        touchHelper.attachToRecyclerView(recyclerView);

        showData(s);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Quantity_desc_field.this,AddQuantityField.class);
                intent.putExtra("Field Name",quantity_desc_field.getText());
                startActivity(intent);
            }
        });

    }
    public void showData(String s){
        Intent intent=getIntent();
        auth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=auth.getCurrentUser();
        String userId=currentUser.getUid();
        db.collection(userId+" "+intent.getStringExtra("Dashboard Field Name")).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            ModelForQuantityDesc model= new ModelForQuantityDesc(snapshot.getString("Title"),snapshot.getString("Quantity or Description"),snapshot.getString("Image"));
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Quantity_desc_field.this, "Failed To Display New Field", Toast.LENGTH_SHORT).show();
            }
        });
      
    }
}