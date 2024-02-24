package com.example.moviereviewapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moviereviewapp.model.RevieW;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class AddReviewActivity extends AppCompatActivity {

    private Button post;
    private EditText post_desc, post_title;
    private ProgressBar post_progressbar;
    private ImageView postImageView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ref = db.collection("Reviews");

    StorageReference storageReference;

    private String currentUserId;
    private String currentUserName;
    private FirebaseAuth firebaserAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    ActivityResultLauncher<String> mTakePhoto;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        post_progressbar = findViewById(R.id.post_progressBar);
        post_progressbar.setVisibility(View.INVISIBLE);
        post_desc = findViewById(R.id.post_desc);
        post_title = findViewById(R.id.post_title);
        post = findViewById(R.id.post);
        postImageView = findViewById(R.id.postImageView);

        storageReference = FirebaseStorage.getInstance()
                .getReference();

        firebaserAuth = FirebaseAuth.getInstance();

        if(user != null){
            currentUserId = user.getUid();
            currentUserName = user.getDisplayName();
        }

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        postImageView.setImageURI(o);
                        imageUri = o;
                    }
                }
        );
        
        post.setOnClickListener(v->{
            postReview();
        });

        postImageView.setOnClickListener(v->{
            mTakePhoto.launch("image/*");
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaserAuth.getCurrentUser();
    }

    private void postReview() {
        String title = post_title.getText().toString().trim();
        String desc = post_desc.getText().toString().trim();
        post_progressbar.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(desc) && imageUri != null){
            final StorageReference filePath = storageReference
                    .child("review_images")
                    .child("my_image_"+ Timestamp.now().getSeconds());
            filePath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUri = uri.toString();
                                    RevieW r = new RevieW();
                                    r.setTitle(title);
                                    r.setImageUrl(imageUri);
                                    r.setReview(desc);;
                                    r.setTimeAdded(new Timestamp(new Date()));
                                    r.setUserName(firebaserAuth.getCurrentUser().getEmail());
                                    r.setUserId(firebaserAuth.getUid());
                                    ref.add(r)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    post_progressbar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(AddReviewActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(AddReviewActivity.this,ReviewsActivity.class));
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    post_progressbar.setVisibility(View.INVISIBLE);
                                                    Toast.makeText(AddReviewActivity.this, "Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    });
        }else{
            post_progressbar.setVisibility(View.INVISIBLE);
        }
    }
}