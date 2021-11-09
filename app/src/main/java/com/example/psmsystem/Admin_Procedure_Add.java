package com.example.psmsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Procedure_Add extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressBar progressBar;

    private EditText edit_procedure_name, edit_procedure;

    Button add;

    String keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__procedure__add);

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


        progressBar = findViewById(R.id.login_progress);
        progressBar.bringToFront();


        edit_procedure_name = findViewById(R.id.et_Name);
        edit_procedure = findViewById(R.id.textArea);

        add = (Button)findViewById(R.id.add_button);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setProcedure();
            }
        });

    }

    public void setProcedure() {



        String procedure_name = edit_procedure_name.getText().toString().trim();
        String procedure = edit_procedure.getText().toString().trim();




        //validation to check if user age textview is empty
        if (TextUtils.isEmpty(procedure_name)) {
            Toast.makeText(getApplicationContext(), "Procedure Name cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        //validation to check if user age textview is empty
        if (TextUtils.isEmpty(procedure)) {
            Toast.makeText(getApplicationContext(), "Procedures cannot be empty", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }


        final Procedure updated_procedure = new Procedure();
        updated_procedure.setProcedure_name(procedure_name);
        updated_procedure.setProcedure(procedure);

        ref.child("Recipes_List").child(keys).child("Procedure").setValue( updated_procedure, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {

                Toast.makeText(getApplicationContext(), "Procedure Added Successfully.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);


                finish();
            }
        });

    }


}