package com.example.moviereviewapp.view;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.R;
import com.example.moviereviewapp.ReviewsActivity;
import com.example.moviereviewapp.clicker.ShareButtonListener;
import com.example.moviereviewapp.model.RevieW;


import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context context;
    ArrayList<RevieW> reviewsList;
    ShareButtonListener s;

    public void setShareButtonListener(ShareButtonListener s){
        this.s = s;
    }

    public MyAdapter(Context context, ArrayList<RevieW> reviewsList) {
        this.context = context;
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item,
                        parent,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RevieW r = reviewsList.get(position);

        holder.review_username.setText(r.getUserName());
        holder.review_title_list.setText(r.getTitle());
        holder.review_desc.setText(r.getReview());

        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(
                r.getTimeAdded().getSeconds()*1000
        );
        holder.review_timeAdded.setText(timeAgo);

        String imageUrl = r.getImageUrl();
        Glide.with(context)
                .load(imageUrl)
                .fitCenter()
                .into(holder.review_image);

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView review_username, review_title_list, review_desc, review_timeAdded;
        public ImageView review_image;
        public ImageButton review_share;
        public String userId, username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            review_username = itemView.findViewById(R.id.review_username);
            review_title_list = itemView.findViewById(R.id.review_title_list);
            review_desc = itemView.findViewById(R.id.review_desc);
            review_timeAdded = itemView.findViewById(R.id.review_timeAdded);
            review_image = itemView.findViewById(R.id.review_image);
            review_share = itemView.findViewById(R.id.review_share);
            review_share.setOnClickListener(v->{
                s.shareIsCare(getAdapterPosition());
            });
        }
    }
}
