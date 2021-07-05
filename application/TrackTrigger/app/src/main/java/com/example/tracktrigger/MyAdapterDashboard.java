package com.example.tracktrigger;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterDashboard extends RecyclerView.Adapter<MyAdapterDashboard.MyViewHolder> {

    private HomeMakerDashboard activity;
    private List<ModelForDashboard> mList;


    public MyAdapterDashboard(HomeMakerDashboard activity,List<ModelForDashboard> mList){
        this.activity=activity;
        this.mList=mList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(activity).inflate(R.layout.dashboard_display,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.newDashboardFieldName.setText(mList.get(position).getNewDashboardField());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView newDashboardFieldName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            newDashboardFieldName=itemView.findViewById(R.id.NewDashboardFieldName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                int position =getAdapterPosition();
             Intent intent =new Intent(activity,Quantity_desc_field.class);
             intent.putExtra("Dashboard Field Name",mList.get(position).getNewDashboardField());
             activity.startActivity(intent);
        }
    }

}
