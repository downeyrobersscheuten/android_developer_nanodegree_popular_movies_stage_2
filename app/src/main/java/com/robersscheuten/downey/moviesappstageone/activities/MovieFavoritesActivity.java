package com.robersscheuten.downey.moviesappstageone.activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.robersscheuten.downey.moviesappstageone.R;
import com.robersscheuten.downey.moviesappstageone.adapters.FavoritesAdapter;
import com.robersscheuten.downey.moviesappstageone.data.Database;
import com.robersscheuten.downey.moviesappstageone.models.Movie;
import com.robersscheuten.downey.moviesappstageone.utils.Utils;

import java.util.List;

public class MovieFavoritesActivity extends AppCompatActivity implements Utils.onMovieClickListener {

    private List<Movie> movies;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_favorites);

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
           int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            findViewById(R.id.cl_favorites).setPadding(0,2* actionBarHeight,0,0);
        }

        rv = findViewById(R.id.rv_favorites);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setHasFixedSize(true);
        rv.setAdapter(new FavoritesAdapter(movies,getApplicationContext()));

        new Thread(new Runnable() {
            @Override
            public void run() {
               movies = Database
                       .getInstance(getApplicationContext())
                       .favoriteDao()
                       .getMovies();

               setData();


            }
        }).start();
    }

    private void setData() {
        rv.setAdapter(new FavoritesAdapter(movies,this));
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra("movie",movie);
        this.startActivity(intent);
    }
}
