package com.robersscheuten.downey.moviesappstageone.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.robersscheuten.downey.moviesappstageone.R;
import com.robersscheuten.downey.moviesappstageone.models.Movie;
import com.robersscheuten.downey.moviesappstageone.utils.BlurBuilder;
import com.robersscheuten.downey.moviesappstageone.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie movie = null;
    private final String TAG = "details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //get data
        if (getIntent().getExtras() != null) {
            Log.d(TAG, "not null");
            movie = getIntent().getParcelableExtra("movie");
            Log.d(TAG, movie.title);
        }

        //fix statusbar.
        final ConstraintLayout constraintLayout = findViewById(R.id.content_movie_detail_contraint_layout);
        constraintLayout.setPadding(0, Utils.getStatusBarHeight(this), 0, 0);
        Log.d(TAG,"" + Utils.getStatusBarHeight(this));

        //set background
        Picasso.with(this)
                .load(Utils.getFullPosterURL(movie.posterPath, Utils.sizeBackground))
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bitmap = BlurBuilder.blur(getBaseContext(), bitmap);
                        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                        bitmapDrawable.setAlpha(180);
                        constraintLayout.setBackground(bitmapDrawable);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        //set data
        Log.d(TAG,movie.getTitle() + " - " + movie.getReleaseDate());

        ((TextView)findViewById(R.id.tv_title)).setText(movie.getTitle() + " - " + movie.getReleaseDate().split("-")[0]);

        ((TextView)findViewById(R.id.tv_overview)).setText(movie.getOverview());
        ((TextView)findViewById(R.id.tv_release_date)).setText(movie.getReleaseDate());
        ((TextView)findViewById(R.id.tv_user_rating)).setText(movie.getVoteAverage() + " IMDB");
    }

}
