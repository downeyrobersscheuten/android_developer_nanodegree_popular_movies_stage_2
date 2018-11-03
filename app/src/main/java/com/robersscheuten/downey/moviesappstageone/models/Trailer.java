package com.robersscheuten.downey.moviesappstageone.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailer {

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("results")
    private List<Trailer> trailers;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }
}
