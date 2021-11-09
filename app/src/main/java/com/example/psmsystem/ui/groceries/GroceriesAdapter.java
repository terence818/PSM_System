package com.example.psmsystem.ui.groceries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.preference.TwoStatePreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CheckBox;

import com.example.psmsystem.Admin_Ingredient_Adapter;
import com.example.psmsystem.Ingredients;
import com.example.psmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroceriesAdapter extends RecyclerView.Adapter<GroceriesAdapter.MyViewHolder> {

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
        CheckBox checkBox;


        public MyViewHolder(View itemView) {
            super(itemView);

            this.ingredients_name = (TextView) itemView.findViewById(R.id.ingredients_name);
            checkBox = itemView.findViewById(R.id.checkBox_select);


        }
    }

    public GroceriesAdapter(Activity activity, ArrayList<Ingredients> data, int showbutton, ArrayList<String> key) {
        this.activity = activity;
        show_button = showbutton;
        this.dataSet = data;
        this.key = key;

    }

    @Override
    public GroceriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_groceries_adapter, parent, false);

        GroceriesAdapter.MyViewHolder myViewHolder = new GroceriesAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(GroceriesAdapter.MyViewHolder holder, final int listPosition) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();


        TextView ingredients_name = holder.ingredients_name;
        holder.checkBox.setChecked(ingredients_name.isSelected());

        //set value into textview
        ingredients_name.setText(dataSet.get(listPosition).getQuantity() + " " + dataSet.get(listPosition).getIngredient_unit() + " " + dataSet.get(listPosition).getIngredient_name());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    if (GroceriesFragment.groceriesFragment != null) {
                        GroceriesFragment.groceriesFragment.addKey(key.get(listPosition));
                    }
                } else {
                    if (GroceriesFragment.groceriesFragment != null) {
                        GroceriesFragment.groceriesFragment.removeKey(key.get(listPosition));
                    }
                    //case 2

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}