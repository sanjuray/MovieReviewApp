package com.example.moviereviewapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser user;

    EditText email,password;
    Button newAcc, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        newAcc = findViewById(R.id.newAcc);
        login = findViewById(R.id.login);


        login.setOnClickListener(v->{
//            Toast.makeText(this, "went it!", Toast.LENGTH_SHORT).show();
            logEmailPassUser(
                    email.getText().toString().trim(),
                    password.getText().toString().trim()
            );
        });


        newAcc.setOnClickListener(v->{
            startActivity(new Intent(this, SignUpActivity.class));
        });
    }

    private void logEmailPassUser(String email, String pwd) {
        if(!TextUtils.isEmpty(email) &&
        !TextUtils.isEmpty(pwd)){
            firebaseAuth.signInWithEmailAndPassword(
                    email,
                    pwd
            ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    startActivity(new Intent(MainActivity.this, ReviewsActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "User not registered/ incorrect credentials!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}