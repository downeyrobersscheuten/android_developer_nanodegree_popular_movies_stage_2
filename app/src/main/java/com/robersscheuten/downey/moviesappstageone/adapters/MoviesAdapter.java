package com.robersscheuten.downey.moviesappstageone.adapters;


import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.robersscheuten.downey.moviesappstageone.R;
import com.robersscheuten.downey.moviesappstageone.models.Movie;
import com.robersscheuten.downey.moviesappstageone.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private final List<Movie> movies;
    private Context context;
    private int width;
    private int height;

    public MoviesAdapter(List<Movie> movies,Context context) {
        this.movies = movies;
        this.context = context;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        width = (Resources.getSystem().getDisplayMetrics().widthPixels)/2;
        height = (int)(width * 1.5);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        String fullPosterPath = Utils.getFullPosterURL(movie.posterPath,Utils.sizeTile);

        Picasso.with(context)
                .load(fullPosterPath)
                .resize(width,height)
                .centerCrop()
                .into(holder.imageView);

        holder.movie = movie;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Utils.onMovieClickListener)context).onClick(movie);
                }
            });
        }

    }
}
