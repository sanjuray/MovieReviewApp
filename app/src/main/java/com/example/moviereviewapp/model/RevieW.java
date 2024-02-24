package com.example.moviereviewapp.model;

import com.google.firebase.Timestamp;

public class RevieW {
    private String imageUrl;
    private String Title;
    private String review;

    private String userId;
    private Timestamp timeAdded;
    private String userName;

    public RevieW() {
    }

    public RevieW(String title,String imageUrl, String review, Timestamp timeAdded,String userId, String userName) {
        this.imageUrl = imageUrl;
        this.Title = title;
        this.review = review;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
            this.userName = userName;
    }
}

