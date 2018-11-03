package com.robersscheuten.downey.moviesappstageone.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.robersscheuten.downey.moviesappstageone.models.Movie;

@Database(entities = {Movie.class}, version = 2)
public abstract class FavoriteDatabase extends RoomDatabase {
    public abstract FavoriteDao favoriteDao();
}
