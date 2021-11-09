package com.example.psmsystem;

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

import com.example.psmsystem.ui.home.RecipesDetails;
import com.example.psmsystem.ui.home.Schedule;
import com.example.psmsystem.ui.schedule.ScheduleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Raw_Adapter extends RecyclerView.Adapter<Admin_Raw_Adapter.MyViewHolder> {

    private Activity activity;
    private ArrayList<Ingredients> dataSet;
    private ArrayList<String> key;
    private int show_button = 1;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference ref = firebaseDatabase.getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private String currentUserID;
    private ValueEventListener a;
    int row_index = -1;


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView ingredients_name;
        TextView ingredients_categories;

        Button delete;



        public MyViewHolder(View itemView) {
            super(itemView);

            this.ingredients_name = (TextView) itemView.findViewById(R.id.ingredients_name);
            this.ingredients_categories = (TextView) itemView.findViewById(R.id.categories);
            this.delete = (Button) itemView.findViewById(R.id.delete);




        }
    }

    public Admin_Raw_Adapter(Activity activity, ArrayList<Ingredients> data, int showbutton, ArrayList<String> key) {
        this.activity = activity;
        show_button = showbutton;
        this.dataSet = data;
        this.key = key;

    }

    @Override
    public Admin_Raw_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_admin__raw__adapter, parent, false);

        Admin_Raw_Adapter.MyViewHolder myViewHolder = new Admin_Raw_Adapter.MyViewHolder(view);
        return myViewHolder;
    }




    @Override
    public void onBindViewHolder(Admin_Raw_Adapter.MyViewHolder holder, final int listPosition) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();


        TextView ingredients_name = holder.ingredients_name;
        TextView ingredients_categories = holder.ingredients_categories;
        Button delete = holder.delete;



        //set value into textview
        ingredients_name.setText("Ingredient Name: " +dataSet.get(listPosition).getIngredient_name());
        ingredients_categories.setText(" Food Categories: " + String.valueOf(dataSet.get(listPosition).getIngredient_categories()));


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete this raw materials?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                ref.child("Ingredients_List").child(key.get(listPosition)).removeValue();
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


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}