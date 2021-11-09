package com.example.psmsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Procedure extends AppCompatActivity {

    private Intent intent;
    private DatabaseReference logReference;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private FirebaseUser user;
    private String currentUserID;
    private TextView procedure_name,procedure;
    private String keys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__procedure);

        procedure_name=findViewById(R.id.procedure_name);
        procedure=findViewById(R.id.procedure);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            currentUserID = auth.getCurrentUser().getUid();
        } else {
            currentUserID = null;
        }
        ref = FirebaseDatabase.getInstance().getReference();

        if (savedInstanceState == null) {
            Intent extras = this.getIntent();
            if (extras == null) {
                keys = null;

            } else {
                keys = extras.getStringExtra("keys");

            }
        }

        ref.child("Recipes_List").child(keys).child("Procedure").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.child("procedure").exists()) {
                        procedure.setText(dataSnapshot.child("procedure").getValue(String.class));

                    }

                    if (dataSnapshot.child("procedure_name").exists()) {
                        procedure_name.setText(dataSnapshot.child("procedure_name").getValue(String.class));
                    }




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        FloatingActionButton fab = findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Admin_Procedure.this,Admin_Procedure_Add.class);
                intent.putExtra("keys", keys);
                startActivity(intent);
            }
        });
    }
}