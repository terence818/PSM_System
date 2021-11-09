package com.example.psmsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_Ingredient_Add extends AppCompatActivity {

    private Spinner dropdown_ingredients;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressBar progressBar;


    Button add;


    private EditText edit_quantity,  edit_ingredients_unit;

    String ingredient_name,ingredient_unit,ingredient_categories,keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__ingredient__add);

        if (savedInstanceState == null) {
            Intent extras = this.getIntent();
            if (extras == null) {
                keys = null;

            } else {
                keys = extras.getStringExtra("keys");

            }
        }


        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
         List<Ingredients> ingredients_name = new ArrayList<Ingredients>();

        dropdown_ingredients = findViewById(R.id.ingredients_spinner);
        ArrayAdapter<Ingredients> adapter = new ArrayAdapter<Ingredients>(this,
                android.R.layout.simple_spinner_item, ingredients_name);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_ingredients.setAdapter(adapter);

        progressBar = findViewById(R.id.login_progress);
        progressBar.bringToFront();

        edit_quantity = findViewById(R.id.et_Unit);
        edit_ingredients_unit= findViewById(R.id.et_Units);

        add = (Button)findViewById(R.id.add_button);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setIngredient();
            }
        });



//        ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(Admin_Ingredient_Add.this, android.R.layout.simple_spinner_item, ingredients_name);
//        areaSpinner.setAdapter(addressAdapter);

        ref.child("Ingredients_List").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                if(dataSnapshot.exists()){


                    for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        Ingredients ingr=areaSnapshot.getValue(Ingredients.class);
                        ingredients_name.add(ingr);

                    }
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dropdown_ingredients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ingredient_name = ingredients_name.get(i).getIngredient_name();
                ingredient_categories = ingredients_name.get(i).getIngredient_categories();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });



    }

    public void setIngredient(){

        String ingredient_quantity = edit_quantity.getText().toString().trim();
        String ingredient_unit = edit_ingredients_unit.getText().toString().trim();

        if (TextUtils.isEmpty(ingredient_quantity)) {
            Toast.makeText(getApplicationContext(), "Quantity cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        //validation to check if user age textview is empty
        if (TextUtils.isEmpty(ingredient_unit)) {
            Toast.makeText(getApplicationContext(), "Ingredient unit cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }


        final Ingredients updated_ingredients = new Ingredients();
        updated_ingredients.setIngredient_name(ingredient_name);
        updated_ingredients.setIngredient_unit(ingredient_unit);
        updated_ingredients.setIngredient_categories(ingredient_categories);
        updated_ingredients.setQuantity(ingredient_quantity);
        updated_ingredients.setStatus("Pending");

        ref.child("Recipes_List").child(keys).child("Ingredients").push().setValue( updated_ingredients, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {

                Toast.makeText(getApplicationContext(), "Ingredients Added Successfully.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);


            }
        });

    }




}