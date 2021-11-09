package com.example.psmsystem.ui.schedule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.psmsystem.Admin_Ingredient_Adapter;
import com.example.psmsystem.Ingredients;
import com.example.psmsystem.Procedure;
import com.example.psmsystem.R;
import com.example.psmsystem.Recipes;
import com.example.psmsystem.ui.home.AddSchedule;
import com.example.psmsystem.ui.home.RecipesDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScheduleDetails extends AppCompatActivity {

    private DatabaseReference logReference;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String currentUserID;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Ingredients> data, pending;
    private static ArrayList<String> keys;

    ImageView imageView;
    TextView itemRecipes, itemCalories,itemCategories, itemProcedures;
    Button add;



    String recipes,calories,serve,categories,imageUrl,procedure,key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);

        Intent intent = getIntent();


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(ScheduleDetails.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<Ingredients>();
        keys=  new ArrayList<String>();
//        pending = new ArrayList<Event>();

//        data.add(new Plan("s","s","s","s"));
        adapter = new Admin_Ingredient_Adapter(this,data,3,keys);

        recyclerView.setAdapter(adapter);




        recipes = intent.getStringExtra("recipes");


        imageView = findViewById(R.id.image);
        itemRecipes = findViewById(R.id.name);
        itemCalories= findViewById(R.id.calories);
        itemCategories = findViewById(R.id.categories);
        itemProcedures = findViewById(R.id.procedure);





        itemRecipes.setText(recipes);
        itemCalories.setText("Calories: "+calories + "kCal");
        itemCategories.setText(categories);



        ref.child("Recipes_List").orderByChild("name").equalTo(recipes).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot :dataSnapshot.getChildren()){
                            Recipes recipes=snapshot.getValue(Recipes.class);
                            itemCalories.setText(recipes.getCalories());
                            itemCategories.setText(recipes.getCategories());
                            Glide.with(getApplicationContext()).load(recipes.getUrl_image()).into(imageView);
//                            itemCalories(recipes.getCalories();
//                            itemCalories(recipes.getCalories();
                            Procedure procedure=snapshot.child("Procedure").getValue(Procedure.class);
                            itemProcedures.setText(procedure.getProcedure());
                            for(DataSnapshot snapshot2:snapshot.child("Ingredients").getChildren()){

                                Ingredients key_Detail = snapshot2.getValue(Ingredients.class);
                                keys.add(snapshot.getKey());
                                data.add(key_Detail);

                            }
                        }

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });





    }
}