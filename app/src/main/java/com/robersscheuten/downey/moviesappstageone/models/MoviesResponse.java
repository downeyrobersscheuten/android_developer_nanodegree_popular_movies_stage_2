package com.robersscheuten.downey.moviesappstageone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Downey Robersscheuten.
 */

public class MoviesResponse {
    @SerializedName("results")
    @Expose
    public List<Movie> results = null;
}
