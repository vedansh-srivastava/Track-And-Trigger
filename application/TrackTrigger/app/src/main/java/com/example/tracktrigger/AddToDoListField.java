package com.example.tracktrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddToDoListField extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    EditText title;
    TextView timeDisplay;
    private FirebaseAuth auth;
    Button AddAlarm,saveOnclick;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_list_field);

        title=findViewById(R.id.titleToDoList);
        timeDisplay=findViewById(R.id.timeDisplay);
        AddAlarm=findViewById(R.id.AddAlarm);

        dpd= DatePickerDialog.newInstance(
                AddToDoListField.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        tpd= TimePickerDialog.newInstance(
                AddToDoListField.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );

        AddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(),"Datepickerdialog");
            }
        });

    }

    @Override
    public void onDateSet (DatePickerDialog view,int year, int monthOfYear,int dayOfMonth){
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tpd.show(getFragmentManager(),"Timepickerdialog");
    }
    @Override
    public void onTimeSet(TimePickerDialog view,int hourOfDay,int minute,int second){
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);

        //String s= DateFormat.getDateInstance(DateFormat.FULL).format(now.getTime());
        String t=DateFormat.getDateTimeInstance().format(now.getTime());
        timeDisplay.setText(t);



        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title("New Task Upcoming")
                .content(title.getText().toString())
                .color(255,0,0,255)
                .led_color(255,255,255,255)
                .time(now)
                .key(title.getText().toString())
                .addAction(new Intent(),"Done")
                .large_icon(R.drawable.logo)
                .build();
        String mtitle=title.getText().toString();
        saveOnclick=findViewById(R.id.saveOnclick);
        saveOnclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTofireStore(t,mtitle);
            }
        });


    }

    private void saveTofireStore(String t,String mtitle){
        if(!t.isEmpty() && !mtitle.isEmpty()){
            HashMap<String,String> map = new HashMap<>();
            map.put("Title",mtitle);
            map.put("Deadline Time",t);

            auth= FirebaseAuth.getInstance();
            FirebaseUser currentUser=auth.getCurrentUser();
            String userId=currentUser.getUid();

            db=FirebaseFirestore.getInstance();
            db.collection(userId+" To-Do List").document("To-Do List "+mtitle).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent= new Intent(AddToDoListField.this,ToDoListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddToDoListField.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}