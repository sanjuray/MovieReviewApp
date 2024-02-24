package com.example.moviereviewapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.moviereviewapp.clicker.ShareButtonListener;
import com.example.moviereviewapp.model.RevieW;
import com.example.moviereviewapp.view.MyAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ReviewsActivity extends AppCompatActivity implements ShareButtonListener {

    private FirebaseFirestore db;
    private CollectionReference ref;
    private  FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    private FirebaseUser user;
    private StorageReference storageReference;
    static private ArrayList<RevieW> reviewList;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        db = FirebaseFirestore.getInstance();
        ref = db.collection("Reviews");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reviewList = new ArrayList<>();
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReviewsActivity.this,AddReviewActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
//        Toast.makeText(this, R.id.action_signout+"  &&  "+itemId, Toast.LENGTH_LONG).show();
        if (itemId == R.id.action_add) {
            if (user != null && firebaseAuth != null) {
                startActivity(new Intent(ReviewsActivity.this, AddReviewActivity.class));
            }}
        if (itemId == R.id.action_signout) {
            if (user != null && firebaseAuth != null) {
                Toast.makeText(this, "Signed out?", Toast.LENGTH_SHORT).show();
                firebaseAuth.signOut();
                startActivity(new Intent(ReviewsActivity.this, MainActivity.class));
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        reviewList = new ArrayList<RevieW>();
        ref.get().addOnSuccessListener(queryDocumentSnapshots -> {
           for(QueryDocumentSnapshot q: queryDocumentSnapshots){
               //deserializing
//               Toast.makeText(this, "Checkpt: 1", Toast.LENGTH_SHORT).show();
//               Toast.makeText(this, "Checkpt: 2"+q.toObject(RevieW.class).getTitle(), Toast.LENGTH_SHORT).show();
               RevieW r = q.toObject(RevieW.class);
//                Toast.makeText(this,r.getTitle()+"",Toast.LENGTH_SHORT).show();
                reviewList.add(r);
//               if(reviewList ==null || reviewList.isEmpty()) Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show();
//               Toast.makeText(this, "Checkpt: 3", Toast.LENGTH_SHORT).show();
           }
//            Toast.makeText(this, "Checkpt: 3+", Toast.LENGTH_SHORT).show();
            myAdapter = new MyAdapter(ReviewsActivity.this, reviewList);
            recyclerView.setAdapter(myAdapter);
            myAdapter.setShareButtonListener(this);
            myAdapter.notifyDataSetChanged();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReviewsActivity.this, "Somethings Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void shareIsCare(int p){
        RevieW r = reviewList.get(p);
        Toast.makeText(ReviewsActivity.this,r.getTitle()+"",Toast.LENGTH_LONG).show();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String s = r.getTitle()+"\n";
        s += r.getReview();
        sendIntent.putExtra(Intent.EXTRA_TEXT,s);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent,null);
        startActivity(shareIntent);
    }
}