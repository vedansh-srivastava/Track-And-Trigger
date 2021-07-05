package com.example.tracktrigger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddQuantityField extends AppCompatActivity {

    private EditText mTitle,quantity_desc;
    private ImageView imageView;
    private String userId;
    private Button AddQuantity_descField;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_quantity_field);

        auth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=auth.getCurrentUser();
         userId=currentUser.getUid();

        imageView=findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallerayIntent = new Intent();
                gallerayIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallerayIntent.setType("image/*");
                startActivityForResult(gallerayIntent,2);
            }
        });



        mTitle=findViewById(R.id.Title);
        quantity_desc=findViewById(R.id.Quantity_desc);
        AddQuantity_descField=findViewById(R.id.AddQuantity_descField);

        db=FirebaseFirestore.getInstance();
        AddQuantity_descField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = mTitle.getText().toString();
                String Quantity_desc = quantity_desc.getText().toString();
                String id ="Title Name "+Title;
                uploadToStorage(imageUri,Title,Quantity_desc,id,userId);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
                imageUri=data.getData();
                imageView.setImageURI(imageUri);
        }
    }

    private void uploadToStorage(Uri uri,String Title,String Quantity_desc,String id,String userId){
            StorageReference fileref=reference.child(userId+" "+mTitle.getText().toString());
            fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageID=uri.toString();
                                savetoFirestore(Title,Quantity_desc,id,userId,imageID);
                                AddQuantity_descField=findViewById(R.id.AddQuantity_descField);
                                //Toast.makeText(AddQuantityField.this, "Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            }
                        });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddQuantityField.this, "Failed to upload Image", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void savetoFirestore(String Title,String Quantity_desc,String id,String userId,String imageID){
        if(!Title.isEmpty() && !Quantity_desc.isEmpty()&& imageUri!=null){
            //Toast.makeText(this, "HELLO!!!!!!!!", Toast.LENGTH_SHORT).show();
            //uploadToStorage(imageUri);
            HashMap<String,String> map = new HashMap<>();
            map.put("Title",Title);
            map.put("Quantity or Description",Quantity_desc);
            map.put("Image",imageID);
            //uploadToStorage(imageUri);
            Intent intent=getIntent();
            db.collection(userId+" "+intent.getStringExtra("Field Name")).document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //start back the activity
                                Intent intent1 =new Intent(AddQuantityField.this,Quantity_desc_field.class);
                                intent1.putExtra("Dashboard Field Name",intent.getStringExtra("Field Name"));
                                startActivity(intent1);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddQuantityField.this, "Failed to Add", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}