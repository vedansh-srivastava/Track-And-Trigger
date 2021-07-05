package com.example.tracktrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class ToDoListActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private MyAdapterToDoList adapter;
    private List<ModelForToDoList> list;
    private FirebaseFirestore db;

    @Override
    public void onResume(){
        super.onResume();
        showToDoList();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        recyclerView=findViewById(R.id.recyclerViewToDoList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter= new MyAdapterToDoList(this,list);
        recyclerView.setAdapter(adapter);
        db=FirebaseFirestore.getInstance();

        ItemTouchHelper touchHelper= new ItemTouchHelper(new TouchHelperToDoList(adapter));
        touchHelper.attachToRecyclerView(recyclerView);

        showToDoList();
    }


    public void showToDoList(){
        auth= FirebaseAuth.getInstance();
        FirebaseUser currentUser=auth.getCurrentUser();
        String userId=currentUser.getUid();
        db.collection(userId+" To-Do List").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        list.clear();
                        for(DocumentSnapshot snapshot : task.getResult()){
                            ModelForToDoList model= new ModelForToDoList(snapshot.getString("Title"),snapshot.getString("Deadline Time"));
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ToDoListActivity.this, "Error in Displaying", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void addtask(View view){
        Intent intent= new Intent(ToDoListActivity.this,AddToDoListField.class);
        startActivity(intent);
    }

}