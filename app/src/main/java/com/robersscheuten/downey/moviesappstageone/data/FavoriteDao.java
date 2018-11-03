package com.robersscheuten.downey.moviesappstageone.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.robersscheuten.downey.moviesappstageone.models.Movie;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("Select * FROM movie")
    List<Movie> getMovies();

    @Query("Select * FROM movie WHERE id = :id")
    Movie getMovie(int id);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);
}
