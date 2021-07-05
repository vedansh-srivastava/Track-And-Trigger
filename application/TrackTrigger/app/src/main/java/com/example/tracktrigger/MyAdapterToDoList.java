package com.example.tracktrigger;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allyants.notifyme.NotifyMe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyAdapterToDoList extends RecyclerView.Adapter<MyAdapterToDoList.MyViewHolder> {


    private ToDoListActivity activity;
    private List<ModelForToDoList> mList;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public MyAdapterToDoList(ToDoListActivity activity,List<ModelForToDoList> mList){
        this.activity=activity;
        this.mList=mList;
    }


    public void deleteData(int position){
        ModelForToDoList item=mList.get(position);
        FirebaseAuth auth;
        auth= FirebaseAuth.getInstance();
        FirebaseUser currentUser=auth.getCurrentUser();
        String userId=currentUser.getUid();
        db.collection(userId+" To-Do List").document("To-Do List "+item.getTitle()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            notifyRemoved(position);
                            //NotifyMe.cancel(a,item.getTitle());
                        }
                    }
                });
    }

   private void notifyRemoved(int position){
        mList.remove(position);
        notifyItemRemoved(position);
        activity.showToDoList();
   }

    @NonNull
    @Override
    public MyAdapterToDoList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(activity).inflate(R.layout.to_do_list_display,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterToDoList.MyViewHolder holder, int position) {
        holder.titleDeadline.setText(mList.get(position).getTitle());
        holder.deadline.setText(mList.get(position).getDeadline());
    }

    @Override
    public int getItemCount() { return mList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleDeadline,deadline;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleDeadline=itemView.findViewById(R.id.titleDeadline);
            deadline=itemView.findViewById(R.id.deadline);
           // itemView.setOnClickListener(this);
        }

    }
}
