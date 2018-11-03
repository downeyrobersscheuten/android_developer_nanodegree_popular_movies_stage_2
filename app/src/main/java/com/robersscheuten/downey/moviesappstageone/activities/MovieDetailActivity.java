package com.robersscheuten.downey.moviesappstageone.activities;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.robersscheuten.downey.moviesappstageone.BuildConfig;
import com.robersscheuten.downey.moviesappstageone.R;
import com.robersscheuten.downey.moviesappstageone.adapters.ReviewsAdapter;
import com.robersscheuten.downey.moviesappstageone.data.Database;
import com.robersscheuten.downey.moviesappstageone.data.FavoriteDao;
import com.robersscheuten.downey.moviesappstageone.data.FavoriteDatabase;
import com.robersscheuten.downey.moviesappstageone.models.Movie;
import com.robersscheuten.downey.moviesappstageone.models.Review;
import com.robersscheuten.downey.moviesappstageone.models.Trailer;
import com.robersscheuten.downey.moviesappstageone.network.MoviesService;
import com.robersscheuten.downey.moviesappstageone.utils.BlurBuilder;
import com.robersscheuten.downey.moviesappstageone.utils.Contracts;
import com.robersscheuten.downey.moviesappstageone.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie movie = null;
    private final String TAG = "details";

    private RecyclerView rvReviews;
    private Button btnTrailer;
    private ImageButton btnFavorite;
    private Boolean favoriteState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        btnTrailer = findViewById(R.id.btn_trailer);
        btnFavorite = findViewById(R.id.btn_add_favorite);


        //get data
        if (getIntent().getExtras() != null) {
            Log.d(TAG, "not null");
            movie = getIntent().getParcelableExtra("movie");
            Log.d(TAG, movie.title);
            getReviews(movie.id.toString(),this);
            getTrailers(movie.id.toString(),this);
        }

        //check favorite
        new Thread(new Runnable() {
            @Override
            public void run() {
                Movie check = Database.getInstance(getApplicationContext()).favoriteDao().getMovie(movie.id);
                if (check != null) {
                    favoriteState = true;
                    btnFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                }
            }
        }).start();

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


        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favoriteState) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Database.getInstance(getApplicationContext()).favoriteDao().deleteMovie(movie);
                        }
                    }).start();

                    btnFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                    Toast.makeText(
                            getApplicationContext(),
                            movie.title + " removed from your favorites",
                            Toast.LENGTH_SHORT
                    ).show();
                    favoriteState = false;
                    return;
                }

                btnFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Database.getInstance(getApplicationContext()).favoriteDao().insertMovie(movie);
                    }
                }).start();

                Toast.makeText(
                        getApplicationContext(),
                        movie.title + " added to your favorites",
                        Toast.LENGTH_SHORT
                ).show();
                favoriteState = true;

            }
        });
    }

    /**
     * Method which handles the dataRetrieval for reviews
     *
     * @param id of the selected movie
     */
    public void getReviews(String id, final Context context){
        //create retrofitBuilder & build a retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contracts.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //create service
        MoviesService moviesService = retrofit.create(MoviesService.class);

        //get the movies async + do the callback.
        moviesService.getReviews(id).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {

                Log.d(TAG,response.body().getReviews().size() + "");

                rvReviews = findViewById(R.id.rv_reviews);
                rvReviews.setAdapter(new ReviewsAdapter(
                        /* reviews: */ response.body().getReviews(),
                        /* context */ context
                ));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                rvReviews.setLayoutManager(linearLayoutManager);
                rvReviews.setItemViewCacheSize(20);
                rvReviews.setDrawingCacheEnabled(true);
                rvReviews.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Utils.showDialogBox(
                        /* error message: */"Something went wrong",
                        /* title message: */"Error",
                        /* context: */ context
                );

                if(BuildConfig.DEBUG)
                    t.printStackTrace();
            }
        });

    }

    /**
     * Method which handles the dataRetrieval for trailers
     *
     * @param id of the selected movie
     */
    public void getTrailers(String id, final Context context){
        //create retrofitBuilder & build a retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contracts.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //create service
        MoviesService moviesService = retrofit.create(MoviesService.class);

        //get the movies async + do the callback.
        moviesService.getTrailers(id).enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(Call<Trailer> call, final Response<Trailer> response) {

                if (response.body() != null) {
                    btnTrailer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent appIntent = new Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("vnd.youtube:" +
                                            response.body()
                                                    .getTrailers()
                                                    .get(0)
                                                    .getKey())
                            );
                            startActivity(appIntent);
                        }
                    });
                    return;
                }
                btnTrailer.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Trailer> call, Throwable t) {
                btnTrailer.setVisibility(View.INVISIBLE);

            }
        });

    }
}
