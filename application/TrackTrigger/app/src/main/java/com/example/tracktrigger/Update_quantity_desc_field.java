package com.example.tracktrigger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Update_quantity_desc_field extends AppCompatActivity {
private EditText title,desc;
private ImageView imageUpdate;
private Button UpdateQuantity_descField,DeleteQuantity_descField;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String s,a,oldID,g,OLD_ID;
    private StorageReference reference= FirebaseStorage.getInstance().getReference();
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_quantity_desc_field);

        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=auth.getCurrentUser();
        String userId=currentUser.getUid();
        Intent intent=getIntent();
        //String s=intent1.getStringExtra("Field Name Update");
        s=intent.getStringExtra("Title");
        a=intent.getStringExtra("Description");
        g=intent.getStringExtra("Image URL");
        OLD_ID=s;
        oldID="Title Name "+s;
        imageUpdate=findViewById(R.id.imageUpdate);
        title = findViewById(R.id.Title);
        UpdateQuantity_descField = findViewById(R.id.UpdateQuantity_descField);
        DeleteQuantity_descField = findViewById(R.id.DeleteQuantity_descField);
        desc = findViewById(R.id.Quantity_desc);
        desc.setText(a);
        title.setText(s);
        Glide.with(this).load(g).into(imageUpdate);
        //String id ="Title Name "+s;

        imageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallerayIntent = new Intent();
                gallerayIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallerayIntent.setType("image/*");
                startActivityForResult(gallerayIntent,2);
            }
        });


        DeleteQuantity_descField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteField(userId,OLD_ID,intent,oldID);
            }
        });
        UpdateQuantity_descField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s=title.getText().toString();
                a=desc.getText().toString();
                String id ="Title Name "+s;



                //savetoFirestore(s,a,id,userId);
                if(imageUri!=null)
                uploadToStorage(imageUri,s,a,id,userId,oldID,intent,OLD_ID);
                else
                    savetoFirestore(s,a,id,userId,g,oldID,intent);

            }
        });
    }

    private void deleteField(String userId,String OLD_ID,Intent intent,String oldID){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference desertRef = storageRef.child(userId+" "+OLD_ID);

        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
        db.collection(userId+" "+intent.getStringExtra("Field Name")).document(oldID).delete();
        Intent intent1 =new Intent(Update_quantity_desc_field.this,Quantity_desc_field.class);
        intent1.putExtra("Dashboard Field Name",intent.getStringExtra("Field Name"));
        startActivity(intent1);
        finish();

    }


    private void uploadToStorage(Uri uri,String Title,String Quantity_desc,String id,String userId,String oldID,Intent intent,String OLD_ID){

            String imageID=intent.getStringExtra("Image URL");
            savetoFirestore(Title, Quantity_desc, id, userId, imageID, oldID, intent);


            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

// Create a reference to the file to delete
            StorageReference desertRef = storageRef.child(userId + " " + OLD_ID);

// Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });

            StorageReference fileref = reference.child(userId + " " + Title);
            fileref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageID = uri.toString();

                            savetoFirestore(Title, Quantity_desc, id, userId, imageID, oldID, intent);
                            //Toast.makeText(AddQuantityField.this, "Uploaded Successfully ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Update_quantity_desc_field.this, "Failed to upload Image", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void savetoFirestore(String Title,String Quantity_desc,String id,String userId,String imageID,String oldID,Intent intent){
        if(!Title.isEmpty() && !Quantity_desc.isEmpty()){
            Map<String,Object> map = new HashMap<>();
            map.put("Title",Title);
            map.put("Quantity or Description",Quantity_desc);
            map.put("Image",imageID);
            //Intent intent=getIntent();
            DocumentReference docref =db.collection(userId+" "+intent.getStringExtra("Field Name")).document(id);
            if(id.equals(oldID)) {
                docref.update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent1 = new Intent(Update_quantity_desc_field.this, Quantity_desc_field.class);
                                intent1.putExtra("Dashboard Field Name", intent.getStringExtra("Field Name"));
                                startActivity(intent1);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(Update_quantity_desc_field.this, "Failed to Update Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //photoRef.delete();
            else {
                docref.set(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //start back the activity
                                    Intent intent1 = new Intent(Update_quantity_desc_field.this, Quantity_desc_field.class);
                                    intent1.putExtra("Dashboard Field Name", intent.getStringExtra("Field Name"));
                                    startActivity(intent1);
                                    db.collection(userId + " " + intent.getStringExtra("Field Name")).document(oldID).delete();
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Update_quantity_desc_field.this, "Failed to Add", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


   /* private void savetoFirestore(String Title,String Quantity_desc,String id,String userId){
        if(!Title.isEmpty() && !Quantity_desc.isEmpty()){
            Map<String,Object> map = new HashMap<>();
            map.put("Title",Title);
            map.put("Quantity or Description",Quantity_desc);
            Intent intent=getIntent();
            DocumentReference docref =db.collection(userId+" "+intent.getStringExtra("Field Name")).document(id);
                    /*docref.update(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent1 =new Intent(Update_quantity_desc_field.this,Quantity_desc_field.class);
                                    intent1.putExtra("Dashboard Field Name",intent.getStringExtra("Field Name"));
                                    startActivity(intent1);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Update_quantity_desc_field.this, "Failed to Update", Toast.LENGTH_SHORT).show();
                        }
                    });*/

                   /* docref.set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //start back the activity
                                Intent intent1 =new Intent(Update_quantity_desc_field.this,Quantity_desc_field.class);
                                intent1.putExtra("Dashboard Field Name",intent.getStringExtra("Field Name"));
                                startActivity(intent1);
                                db.collection(userId+" "+intent.getStringExtra("Field Name")).document(oldID).delete();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Update_quantity_desc_field.this, "Failed to Add", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            imageUpdate.setImageURI(imageUri);
        }
    }
}