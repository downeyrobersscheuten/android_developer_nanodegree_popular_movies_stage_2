package com.robersscheuten.downey.moviesappstageone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robersscheuten.downey.moviesappstageone.R;
import com.robersscheuten.downey.moviesappstageone.models.Movie;
import com.robersscheuten.downey.moviesappstageone.utils.Utils;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {
    private List<Movie> movies;
    private Context context;

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_item,parent,false);
        return new FavoriteViewHolder(view);
    }

    public FavoritesAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.movie = movie;
        holder.tvDate.setText(movie.getReleaseDate().substring(0,4));
        holder.tvTitle.setText(movie.getTitle());

    }

    @Override
    public int getItemCount() {
        if(movies == null)
            return 0;
        return movies.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDate;
        private Movie movie;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_favorite_title);
            tvDate = itemView.findViewById(R.id.tv_favorite_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Utils.onMovieClickListener)context).onClick(movie);

                }
            });
        }
    }
}
