package com.example.psmsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Raw_Add extends AppCompatActivity {

    private Spinner dropdown_categories;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressBar progressBar;

    private EditText edit_ingredients, edit_ingredients_unit;

    Button add;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__raw__add);

        //array to populate start time spinner
        String[] arraySpinner = new String[]{
                "Carbohydrate", "Protein", "Fats" , "Vegetables&Fruit"
        };

        dropdown_categories = findViewById(R.id.ingredients_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown_categories.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        progressBar = findViewById(R.id.login_progress);
        progressBar.bringToFront();

        edit_ingredients = findViewById(R.id.et_Name);

        add = (Button)findViewById(R.id.add_button);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setRaw();
            }
        });


    }

    public void setRaw() {



        String ingredient_name = edit_ingredients.getText().toString().trim();
        String ingredient_categories = dropdown_categories.getSelectedItem().toString();




        //validation to check if user age textview is empty
        if (TextUtils.isEmpty(ingredient_name)) {
            Toast.makeText(getApplicationContext(), "Ingredients name cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }




        final Ingredients updated_ingredients = new Ingredients();
        updated_ingredients.setIngredient_name(ingredient_name);
        updated_ingredients.setIngredient_categories(ingredient_categories);

        ref.child("Ingredients_List").push().setValue( updated_ingredients, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {

                Toast.makeText(getApplicationContext(), "Raw Materials Added Successfully.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                Intent intent = new Intent(Admin_Raw_Add.this, Admin_Raw.class);
                startActivity(intent);
                finish();
            }
        });

    }


}