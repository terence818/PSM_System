package com.example.psmsystem.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.psmsystem.Admin_Recipes;
import com.example.psmsystem.Admin_Recipes_Adapter;
import com.example.psmsystem.R;
import com.example.psmsystem.Recipes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private Activity activity;
    private ArrayList<Recipes> dataSet;
    private ArrayList<String> key;
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






        public MyViewHolder(View itemView) {
            super(itemView);

            this.recipes = (TextView) itemView.findViewById(R.id.recipes_name);
            this.calories = (TextView) itemView.findViewById(R.id.calories);
            this.serve = (TextView) itemView.findViewById(R.id.serve);
            this.relative = (ConstraintLayout) itemView.findViewById(R.id.relative);
            this.recipes_image = (ImageView) itemView.findViewById(R.id.recipes_image);



        }
    }

    public HomeAdapter(Activity activity,ArrayList<Recipes> data,int showbutton, ArrayList<String> key) {
        this.activity = activity;
        show_button = showbutton;
        this.dataSet = data;
        this.key=key;

    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_home_adapter, parent, false);

        HomeAdapter.MyViewHolder myViewHolder = new HomeAdapter.MyViewHolder(view);
        return myViewHolder;
    }



    @Override
    public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int listPosition) {

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
                new HomeAdapter.getAndSetImage(String.valueOf(dataSet.get(listPosition).getUrl_image()), holder, dataSet.get(listPosition)).execute();
            }
        }




        final Intent intent=new Intent(activity, RecipesDetails.class);;

        relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                intent.putExtra("recipes", dataSet.get(listPosition).getName());
                intent.putExtra("calories", dataSet.get(listPosition).getCalories());
                intent.putExtra("serve", dataSet.get(listPosition).getServe());
                intent.putExtra("image", dataSet.get(listPosition).getUrl_image());
                intent.putExtra("categories", dataSet.get(listPosition).getCategories());
                intent.putExtra("key",key.get(listPosition));
                activity.startActivity(intent);



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

        public getAndSetImage(String url, final HomeAdapter.MyViewHolder holder, Recipes recentHistory){
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
            HomeAdapter.this.notifyDataSetChanged();
            if(bmp!=null) {
                recipes_image.setImageBitmap(bmp);
            }else{
                recipes_image.setImageResource(R.drawable.ayam_goreng);
            }
        }
    }



}