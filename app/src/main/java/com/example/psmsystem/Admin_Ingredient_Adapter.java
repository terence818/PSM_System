package com.example.psmsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Ingredient_Adapter extends RecyclerView.Adapter<Admin_Ingredient_Adapter.MyViewHolder> {

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




        public MyViewHolder(View itemView) {
            super(itemView);

            this.ingredients_name = (TextView) itemView.findViewById(R.id.ingredients_name);



        }
    }

    public Admin_Ingredient_Adapter(Activity activity, ArrayList<Ingredients> data, int showbutton, ArrayList<String> key) {
        this.activity = activity;
        show_button = showbutton;
        this.dataSet = data;
        this.key = key;

    }

    @Override
    public Admin_Ingredient_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_admin__ingredient__adapter, parent, false);

        Admin_Ingredient_Adapter.MyViewHolder myViewHolder = new Admin_Ingredient_Adapter.MyViewHolder(view);
        return myViewHolder;
    }




    @Override
    public void onBindViewHolder(Admin_Ingredient_Adapter.MyViewHolder holder, final int listPosition) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();


        TextView ingredients_name = holder.ingredients_name;



        //set value into textview
        ingredients_name.setText(dataSet.get(listPosition).getQuantity() + " " + dataSet.get(listPosition).getIngredient_unit() + " "+ dataSet.get(listPosition).getIngredient_name());


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}