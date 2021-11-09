package com.example.psmsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Admin_Recipes_Adapter extends RecyclerView.Adapter<Admin_Recipes_Adapter.MyViewHolder> {

    private Activity activity;
    private ArrayList<Recipes> dataSet;
    private ArrayList<String> key;
    private Location Location_cur;
    private int show_button = 1;
    private FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private DatabaseReference ref= firebaseDatabase.getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private String currentUserID;
    private ValueEventListener a;
    int row_index=-1;



    public static class MyViewHolder extends RecyclerView.ViewHolder {



        TextView recipes;
        TextView calories;
        TextView procedures;
        TextView serve;
        ConstraintLayout relative;
        ImageView recipes_image;
        Button delete;








        public MyViewHolder(View itemView) {
            super(itemView);

            this.recipes = (TextView) itemView.findViewById(R.id.recipes_name);
            this.calories = (TextView) itemView.findViewById(R.id.calories);
            this.serve = (TextView) itemView.findViewById(R.id.serve);
            this.relative = (ConstraintLayout) itemView.findViewById(R.id.relative);
            this.recipes_image = (ImageView) itemView.findViewById(R.id.recipes_image);
            this.delete = (Button) itemView.findViewById(R.id.delete);



        }
    }

    public Admin_Recipes_Adapter(Activity activity,ArrayList<Recipes> data,int showbutton, ArrayList<String> key) {
        this.activity = activity;
        show_button = showbutton;
        this.dataSet = data;
        this.key=key;

    }

    @Override
    public Admin_Recipes_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_admin__recipes__adapter, parent, false);

        Admin_Recipes_Adapter.MyViewHolder myViewHolder = new Admin_Recipes_Adapter.MyViewHolder(view);
        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(Admin_Recipes_Adapter.MyViewHolder holder, final int listPosition) {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();


        TextView recipes = holder.recipes;
        TextView calories = holder.calories;
        TextView serve = holder.serve;
        ConstraintLayout relative = holder.relative;
        ImageView recipes_image = holder.recipes_image;
        Button delete = holder.delete;



        //set value into textview
        recipes.setText(dataSet.get(listPosition).getName());
        calories.setText(" Calories:" +String.valueOf(dataSet.get(listPosition).getCalories()) + "kCal");
        serve.setText(" Serving: " +String.valueOf(dataSet.get(listPosition).getServe()));





        //set imageview
        recipes_image.setImageResource(R.drawable.ayam_goreng);
        if(!String.valueOf(dataSet.get(listPosition).getUrl_image()).equals("")){
            if(dataSet.get(listPosition).getBitmap() != null) {
                recipes_image.setImageBitmap(dataSet.get(listPosition).getBitmap());
            } else {
                new getAndSetImage(String.valueOf(dataSet.get(listPosition).getUrl_image()), holder, dataSet.get(listPosition)).execute();
            }
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                builder.setMessage("Are you sure you want to delete this recipes?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                ref.child("Recipes_List").child(key.get(listPosition)).removeValue();
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




        final Intent intent=new Intent(activity,Admin_Recipes.class);;

        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            OpenDialog(key.get(listPosition));


            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }



    class getAndSetImage extends AsyncTask<String,Void,Void> {

        Bitmap bmp;

        URL url;
        String urlString;
        ImageView recipes_image;

        Recipes recentHistory;

        public getAndSetImage(String url, final Admin_Recipes_Adapter.MyViewHolder holder, Recipes recentHistory){
            urlString = url;
            recipes_image=holder.recipes_image;
            this.recentHistory = recentHistory;
        }

        @Override
        protected Void doInBackground(String... voids) {


            //get image from url and set imageview
            try {
                url = new URL(urlString);
                recentHistory.setBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
            }catch(MalformedURLException e)
            {
                e.printStackTrace();

            }catch(IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Admin_Recipes_Adapter.this.notifyDataSetChanged();
            if(bmp!=null) {
                recipes_image.setImageBitmap(bmp);
            }else{
                recipes_image.setImageResource(R.drawable.ayam_goreng);
            }
        }
    }

    private void OpenDialog(String keys) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog a = builder.setItems(new String[]{"Add Procedure", "Add Ingredient"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {

                    Intent intent = new Intent(activity, Admin_Procedure.class);
                    intent.putExtra("keys",keys);
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(activity, Admin_Ingredients.class);
                    intent.putExtra("keys",keys);
                    activity.startActivity(intent);
                }

            }
        }).setTitle("Add Information").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }



}