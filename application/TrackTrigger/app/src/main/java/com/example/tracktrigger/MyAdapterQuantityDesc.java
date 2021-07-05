package com.example.tracktrigger;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class MyAdapterQuantityDesc extends RecyclerView.Adapter<MyAdapterQuantityDesc.MyViewHolder> {

    private Quantity_desc_field activity;
    private List<ModelForQuantityDesc> mList;

    public MyAdapterQuantityDesc(Quantity_desc_field activity,List<ModelForQuantityDesc> mList){
            this.activity=activity;
            this.mList=mList;
    }

    public void shareData(int position){
            ModelForQuantityDesc item = mList.get(position);

            String text="Title: "+item.getTitle()+"\nDescription or Quantity: "+item.getDesc()+"\nImage URL: "+Uri.parse(item.getImageUri());



            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            //intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT,text);
            //intent.putExtra(Intent.EXTRA_STREAM,);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
            }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.quantity_desc_field_display,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.Title.setText(mList.get(position).getTitle());
        holder.desc.setText(mList.get(position).getDesc());
        Glide.with(activity).load(mList.get(position).getImageUri()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView Title,desc;
            ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.title_text);
            imageView=itemView.findViewById(R.id.fieldImage);
            desc=itemView.findViewById(R.id.quantity_desc_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position =getAdapterPosition();
            Intent intent = new Intent(activity, Update_quantity_desc_field.class);
            intent.putExtra("Title",mList.get(position).getTitle());
            intent.putExtra("Description",mList.get(position).getDesc());
            intent.putExtra("Field Name",activity.s);
            intent.putExtra("Image URL",mList.get(position).getImageUri());
            activity.startActivity(intent);
        }
    }
}
