package com.example.psmsystem.ui.groceries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.psmsystem.Ingredients;
import com.example.psmsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SummaryGroceriesAdapter extends RecyclerView.Adapter<SummaryGroceriesAdapter.MyViewHolder> {

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

    public  SummaryGroceriesAdapter(Activity activity, ArrayList<Ingredients> data, int showbutton, ArrayList<String> key) {
        this.activity = activity;
        show_button = showbutton;
        this.dataSet = data;
        this.key = key;

    }

    @Override
    public  SummaryGroceriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_summary_groceries_adapter, parent, false);

        SummaryGroceriesAdapter.MyViewHolder myViewHolder = new  SummaryGroceriesAdapter.MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(SummaryGroceriesAdapter.MyViewHolder holder, final int listPosition) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();


        TextView ingredients_name = holder.ingredients_name;

        CheckBox checkBox=holder.checkBox;
        checkBox.setChecked(true);
        if (Summary_Groceries.summary_groceries != null) {
            Summary_Groceries.summary_groceries.addKey(key.get(listPosition));
        }
        //set value into textview
        ingredients_name.setText(dataSet.get(listPosition).getQuantity() + " " + dataSet.get(listPosition).getIngredient_unit() + " " + dataSet.get(listPosition).getIngredient_name());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    if (Summary_Groceries.summary_groceries != null) {
                        Summary_Groceries.summary_groceries.addKey(key.get(listPosition));
                    }
                } else {
                    if (Summary_Groceries.summary_groceries != null) {
                        Summary_Groceries.summary_groceries.removeKey(key.get(listPosition));
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