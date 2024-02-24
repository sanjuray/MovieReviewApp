package com.example.moviereviewapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    EditText newEmail,username,newPassword;
    Button signup;

    private FirebaseFirestore db;
    private CollectionReference ref;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        newEmail = findViewById(R.id.newEmail);
        username = findViewById(R.id.username);
        newPassword = findViewById(R.id.newPassword);
        signup = findViewById(R.id.signup);

        db = FirebaseFirestore.getInstance();
        ref = db.collection("Users");

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user already logged in
                }else{
                    //user signed out
                }
            }
        };

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = newEmail.getText().toString().trim();
                String p = newPassword.getText().toString().trim();
                String u = username.getText().toString().trim();
                if(!TextUtils.isEmpty(e) &&
                        !TextUtils.isEmpty(p) &&
                        !TextUtils.isEmpty(u))
                    createUserEmailAccount(e,p,u);
                else Toast.makeText(SignUpActivity.this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show();
            }
        });

    }
    
    private void createUserEmailAccount(String email, String pass, String username){
        if(!TextUtils.isEmpty(email) && 
        !TextUtils.isEmpty(pass) &&
        !TextUtils.isEmpty(username)){
            firebaseAuth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //user created
                                Toast.makeText(SignUpActivity.this, "user created!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this,ReviewsActivity.class));;
                            }
                        }
                    });
            
        }
    }
}