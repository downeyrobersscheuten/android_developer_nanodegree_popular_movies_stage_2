package com.robersscheuten.downey.moviesappstageone.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.robersscheuten.downey.moviesappstageone.R;
import com.robersscheuten.downey.moviesappstageone.adapters.MoviesAdapter;
import com.robersscheuten.downey.moviesappstageone.data.Database;
import com.robersscheuten.downey.moviesappstageone.models.Movie;
import com.robersscheuten.downey.moviesappstageone.models.MoviesResponse;
import com.robersscheuten.downey.moviesappstageone.network.MoviesService;
import com.robersscheuten.downey.moviesappstageone.utils.Contracts;
import com.robersscheuten.downey.moviesappstageone.utils.Utils;

import java.util.ArrayList;

import io.reactivex.internal.schedulers.NewThreadWorker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * MainActivity is the first Activity the user will interact with.
 */
public class MainActivity extends AppCompatActivity implements Utils.onMovieClickListener {

    private RecyclerView moviesRecyclerView;
    private MoviesService.SearchType defaultFilterSetting;
    private TextView tv_top_rated;
    private TextView tv_popular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set favorites button
        FloatingActionButton fab = findViewById(R.id.fab_favorites);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MovieFavoritesActivity.class);
                startActivity(intent);
            }
        });

        tv_top_rated = findViewById(R.id.btn_top_rated);
        tv_popular = findViewById(R.id.btn_popular);
        SharedPreferences file = getPreferences(MODE_PRIVATE);
        defaultFilterSetting = MoviesService.SearchType.valueOf(
                file.getString(Contracts.DEFAULT_TYPE, "POPULAR_MOVIES")
        );


        if (Utils.checkNetworkAvailable(this)) {
            setInitialState();
        } else {
            View include = findViewById(R.id.include_main_content);
            TextView textView = findViewById(R.id.tv_error_message);

            textView.setSystemUiVisibility(View.VISIBLE);
            include.setSystemUiVisibility(View.GONE);

            String errorMessage = "No network available. Without a network we can't" +
                    " retrieve latest popular movies";
            String errorTitle = "No network available.";
            Utils.showDialogBox(errorMessage, errorTitle, this);
        }


        //setAdapter
        moviesRecyclerView = findViewById(R.id.rv_moviesrecylcerview);
        moviesRecyclerView.setAdapter(new MoviesAdapter(new ArrayList<Movie>(), this));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
        moviesRecyclerView.setHasFixedSize(true);
        moviesRecyclerView.setItemViewCacheSize(20);
        moviesRecyclerView.setDrawingCacheEnabled(true);
        moviesRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //fix statusbar.
        ConstraintLayout constraintLayout = findViewById(R.id.constraint_layout_buttons);
        constraintLayout.setPadding(
                0, Utils.getStatusBarHeight(this),
                0,
                0
        );

        //setGestures
        itemTouchHelper itemTouchHelper = new itemTouchHelper(
                0,
                ItemTouchHelper.RIGHT + ItemTouchHelper.LEFT
        );
        ItemTouchHelper helper = new ItemTouchHelper(itemTouchHelper);
        helper.attachToRecyclerView(moviesRecyclerView);

    }

    private void setInitialState() {
        if (defaultFilterSetting == MoviesService.SearchType.POPULAR_MOVIES)
            selectPopular(null);
        else
            selectTopRated(null);
    }


    /**
     * Method which handles the dataRetrieval of movies based on type.
     *
     * @param type is of enum SearchType which states which kind of list to retrieve
     */
    public void getMovies(MoviesService.SearchType type,final Context context) {
        //create retrofitBuilder & build a retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contracts.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //create service
        MoviesService moviesService = retrofit.create(MoviesService.class);

        //get the movies async + do the callback.
        moviesService.getMovies(type.getVal()).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                moviesRecyclerView.setAdapter(new MoviesAdapter(response.body().results, context));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                System.out.println("error: " + t.getMessage());
            }
        });

    }

    /**
     * Checks if Popular mode has been loaded, if not set it to default, save it & load data.
     *
     * @param view required for onClick in xml files, not used
     */
    public void selectPopular(View view) {
        if (defaultFilterSetting != MoviesService.SearchType.POPULAR_MOVIES || view == null) {
            saveDefaultMode(MoviesService.SearchType.POPULAR_MOVIES);
            getMovies(MoviesService.SearchType.POPULAR_MOVIES,this);

            tv_popular.setTextColor(getResources().getColor(R.color.white));
            tv_top_rated.setTextColor(getResources().getColor(R.color.grey));
        }

    }

    /**
     * Checks if TopRated mode has been loaded, if not set it to default, save it & load data.
     *
     * @param view required for onClick in xml files, not used
     */
    public void selectTopRated(View view) {
        if (defaultFilterSetting != MoviesService.SearchType.TOPRATED_MOVIES || view == null) {
            saveDefaultMode(MoviesService.SearchType.TOPRATED_MOVIES);
            getMovies(MoviesService.SearchType.TOPRATED_MOVIES,this);


            tv_top_rated.setTextColor(getResources().getColor(R.color.white));
            tv_popular.setTextColor(getResources().getColor(R.color.grey));
        }

    }


    /**
     * Saves the default filter mode to SharedPreferences.
     *
     * @param defaultFilterMode default mode to be saved
     */
    private void saveDefaultMode(MoviesService.SearchType defaultFilterMode) {
        this.defaultFilterSetting = defaultFilterMode;
        SharedPreferences file = getPreferences(MODE_PRIVATE);
        file.edit()
                .putString(Contracts.DEFAULT_TYPE, defaultFilterMode.toString())
                .apply();
    }

    /**
     * Callback which handles the selection of a movie.
     *
     * @param movie is the selected movie
     */
    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this,MovieDetailActivity.class);
        intent.putExtra("movie",movie);
        this.startActivity(intent);
    }


    /**
     * Class to handle the left and right swipes.
     */
    private class itemTouchHelper extends ItemTouchHelper.SimpleCallback {
        RecyclerView recyclerView;


        public itemTouchHelper(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target)
        {
            this.recyclerView = recyclerView;
            clearView(recyclerView, viewHolder);
            return false;
        }

        @Override

        public void onChildDraw(
                Canvas c,
                RecyclerView recyclerView,
                RecyclerView.ViewHolder viewHolder,
                float dX,
                float dY,
                int actionState,
                boolean isCurrentlyActive){ }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


            switch (direction) {
                case ItemTouchHelper.RIGHT:
                    selectPopular(new View(MainActivity.this));
                    clearView(recyclerView, viewHolder);
                    break;
                case ItemTouchHelper.LEFT:
                    selectTopRated(new View(MainActivity.this));
                    clearView(recyclerView, viewHolder);
                    break;
                default:
                    break;
            }
        }
    }
}
