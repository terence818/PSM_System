package com.example.psmsystem.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psmsystem.Ingredients;
import com.example.psmsystem.Procedure;
import com.example.psmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddSchedule extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    private Spinner dropdown_starttime;


    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener date;

    private TextView calenderT;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseUser user;
    private ValueEventListener a;


    private ArrayList<Ingredients> igr_list;
    String day, name, serve, calories, key;

    Ingredients igr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        if (savedInstanceState == null) {
            Intent extras = this.getIntent();
            if (extras == null) {
                name = null;
                serve = null;
                key = null;
                calories = null;

            } else {
                name = extras.getStringExtra("recipes");
                serve = extras.getStringExtra("serve");
                calories = extras.getStringExtra("calories");
                key = extras.getStringExtra("key");


            }
        }


        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        user = auth.getCurrentUser();

        igr_list = new ArrayList<>();

        calenderT = findViewById(R.id.calender);

        ref.child("Recipes_List").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("Ingredients").exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.child("Ingredients").getChildren()) {

                            Ingredients igr = snapshot.getValue(Ingredients.class);
                            igr_list.add(igr);

                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };


        findViewById(R.id.calender).setOnClickListener(this);
        findViewById(R.id.confirm).setOnClickListener(this);


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());


        day = (String) DateFormat.format("EEEE", myCalendar.getTime());

        calenderT.setText(sdf.format(myCalendar.getTime()));
    }

    //function to check if the date is overlap
    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

    //add hours to date and return in date formtat
    public Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                final String dates = calenderT.getText().toString().trim();

//                 check if any field is empty
                if (TextUtils.isEmpty(dates)) {
                    Toast.makeText(getApplicationContext(), "Please select date.", Toast.LENGTH_SHORT).show();
                    return;
                }


                Schedule schedule = new Schedule();

                schedule.setDate(dates);
                schedule.setDay(day);
                schedule.setName(name);
                schedule.setServe(serve);
                schedule.setCalories(calories);


                ref.child(user.getUid()).child("Schedule_List").push().setValue(schedule, new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError error, @NonNull DatabaseReference ref_local) {
//                        Toast.makeText(getApplicationContext(), "Update schedule successfully.", Toast.LENGTH_SHORT).show();

                        for (Ingredients ingred : igr_list) {
                            ingred.setKey(ref_local.getKey());
                            ref.child(user.getUid()).child("Groceries_List").push().setValue(ingred, new DatabaseReference.CompletionListener() {
                                public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(getApplicationContext(), "Update successfully.", Toast.LENGTH_SHORT).show();

                                }


                            });
                        }
//                        finish();
                    }


                });
                break;
            case R.id.calender:
                DatePickerDialog a = new DatePickerDialog(AddSchedule.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                Calendar b = Calendar.getInstance();
                b.add(Calendar.DAY_OF_MONTH, 1);
                b.set(Calendar.HOUR_OF_DAY, 8);
                b.set(Calendar.MINUTE, 0);
                a.getDatePicker().setMinDate(b.getTimeInMillis());
                b.add(Calendar.DAY_OF_MONTH, 90);
                a.getDatePicker().setMaxDate(b.getTimeInMillis());
                a.show();
                break;

        }


    }
}