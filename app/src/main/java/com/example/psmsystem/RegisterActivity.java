package com.example.psmsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextpassword, editTextemail, editTextusername, editTextfullname;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private FirebaseUser user;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        editTextpassword = findViewById(R.id.password);
        editTextemail = findViewById(R.id.Email);
        editTextusername = findViewById(R.id.username);
        editTextfullname = findViewById(R.id.Name);
        auth = FirebaseAuth.getInstance();




        findViewById(R.id.submit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                final String email = editTextemail.getText().toString().trim();
                final String password = editTextpassword.getText().toString().trim();
                final String username = editTextusername.getText().toString().trim();
                final String fullName = editTextfullname.getText().toString().trim();




                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(fullName)) {
                    Toast.makeText(getApplicationContext(), "Username and Full Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Email address cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create used
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    Toast.makeText(RegisterActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                } else {
                                    auth = FirebaseAuth.getInstance();
                                    user = auth.getCurrentUser();
                                    User updated_user = new User();
                                    updated_user.setUsername(username);
                                    updated_user.setEmail(email);
                                    updated_user.setFullname(fullName);
                                    updated_user.setType("User");

                                    ref.child(user.getUid()).child("User_Information").setValue(updated_user, new DatabaseReference.CompletionListener() {
                                        public void onComplete(DatabaseError error, @NonNull DatabaseReference ref) {

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                }
                            }
                        });
                break;
//            case R.id.sign_in:
////                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
////                startActivity(intent);
//                finish();
//                break;
        }
    }
}