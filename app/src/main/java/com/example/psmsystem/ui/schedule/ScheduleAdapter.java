package com.example.psmsystem.ui.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.psmsystem.R;
import com.example.psmsystem.Recipes;
import com.example.psmsystem.ui.home.HomeAdapter;
import com.example.psmsystem.ui.home.RecipesDetails;
import com.example.psmsystem.ui.home.Schedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>{

private Activity activity;
private ArrayList<Schedule> dataSet;
private ArrayList<String> key;
private int show_button=1;
private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
private DatabaseReference ref=firebaseDatabase.getReference();
private FirebaseAuth auth=FirebaseAuth.getInstance();
private FirebaseUser user=auth.getCurrentUser();
private String currentUserID;
private ValueEventListener a;
        int row_index=-1;


public static class MyViewHolder extends RecyclerView.ViewHolder {


    TextView recipes;
    TextView day;
    TextView datetime;
    TextView serve;
    TextView calories;
    ConstraintLayout relative;
    Button delete;


    public MyViewHolder(View itemView) {
        super(itemView);

        this.recipes = (TextView) itemView.findViewById(R.id.recipes_name);
        this.datetime = (TextView) itemView.findViewById(R.id.schedule_date);
        this.day = (TextView) itemView.findViewById(R.id.schedule_day);
        this.serve = (TextView) itemView.findViewById(R.id.serving);
        this.calories = (TextView) itemView.findViewById(R.id.calories);
        this.relative = (ConstraintLayout) itemView.findViewById(R.id.relative);
        this.delete = (Button) itemView.findViewById(R.id.delete);


    }

}

    public ScheduleAdapter(Activity activity, ArrayList<Schedule> data, int showbutton, ArrayList<String> key) {
        this.activity = activity;
        show_button = showbutton;
        this.dataSet = data;
        this.key = key;

    }

    @Override
    public ScheduleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_schedule_adapter, parent, false);

        ScheduleAdapter.MyViewHolder myViewHolder = new ScheduleAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(ScheduleAdapter.MyViewHolder holder, final int listPosition) {
        Log.d("max","oyggggggyo");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();


        TextView recipes = holder.recipes;
        TextView datetime = holder.datetime;
        TextView day = holder.day;
        TextView serve = holder.serve;
        TextView calories = holder.calories;
        ConstraintLayout relative = holder.relative;
        Button delete = holder.delete;


        //set value into textview
        recipes.setText(dataSet.get(listPosition).getName());
        datetime.setText(" Schedule Date: " + String.valueOf(dataSet.get(listPosition).getDate()));
        day.setText(" Day: " + String.valueOf(dataSet.get(listPosition).getDay()));
        serve.setText(" Serve: " + String.valueOf(dataSet.get(listPosition).getServe()));
        calories.setText("Calories: " + String.valueOf(dataSet.get(listPosition).getCalories()) + "kCal");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete this schedule?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ref.child(user.getUid()).child("Groceries_List").orderByChild("key").equalTo(key.get(listPosition)).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.exists()) {

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if(snapshot!=null){
                                                    ref.child(user.getUid()).child("Groceries_List").child(Objects.requireNonNull(snapshot.getKey())).removeValue();

                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });

                                ref.child(user.getUid()).child("Schedule_List").child(key.get(listPosition)).removeValue();
                            }

                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }

            ;
        });



        final Intent intent = new Intent(activity, ScheduleDetails.class);


        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                intent.putExtra("recipes", dataSet.get(listPosition).getName());
                intent.putExtra("date", dataSet.get(listPosition).getDate());
                intent.putExtra("day", dataSet.get(listPosition).getDay());
                intent.putExtra("serve", dataSet.get(listPosition).getServe());
                intent.putExtra("calories", dataSet.get(listPosition).getCalories());
                intent.putExtra("key", key.get(listPosition));
                activity.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}