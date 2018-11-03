package com.robersscheuten.downey.moviesappstageone.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Review {


    @SerializedName("results")
    private List<Review> reviews;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
