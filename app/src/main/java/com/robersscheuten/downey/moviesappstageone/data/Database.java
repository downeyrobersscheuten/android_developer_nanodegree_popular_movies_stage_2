package com.robersscheuten.downey.moviesappstageone.data;

import android.arch.persistence.room.Room;
import android.content.Context;

public class Database {
    private static FavoriteDatabase instance;

    private Database() {
    }

    public static FavoriteDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {

                    instance = Room.databaseBuilder(
                            context,
                            FavoriteDatabase.class,
                            "favorites-database")
                            .fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return instance;
    }
}
