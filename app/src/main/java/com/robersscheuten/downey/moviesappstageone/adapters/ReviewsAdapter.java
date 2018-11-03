package com.robersscheuten.downey.moviesappstageone.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robersscheuten.downey.moviesappstageone.R;
import com.robersscheuten.downey.moviesappstageone.models.Review;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private final List<Review> reviews;
    private final Context context;

    public ReviewsAdapter(List<Review> reviews, Context context){
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         return new ReviewViewHolder(
                 LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_item,parent,false)
         );
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        //holder.tvTitle.setText(review.getContent());
        holder.tvAuthor.setText("Author: " + review.getAuthor());
        holder.tvContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        //UI elements
        //private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvContent;


        public ReviewViewHolder(View itemView) {
            super(itemView);

            //set UI
            //tvTitle = itemView.findViewById(R.id.tv_review_title);
            tvAuthor = itemView.findViewById(R.id.tv_review_author);
            tvContent = itemView.findViewById(R.id.tv_review_content);
        }
    }
}
