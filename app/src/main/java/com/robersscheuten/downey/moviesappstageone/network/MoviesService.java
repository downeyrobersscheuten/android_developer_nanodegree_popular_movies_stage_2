package com.robersscheuten.downey.moviesappstageone.network;

import com.robersscheuten.downey.moviesappstageone.models.MoviesResponse;
import com.robersscheuten.downey.moviesappstageone.models.Review;
import com.robersscheuten.downey.moviesappstageone.models.Trailer;
import com.robersscheuten.downey.moviesappstageone.utils.Contracts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit Service class.
 */
public interface MoviesService {

    @GET("{type}?api_key=" + Contracts.API_KEY)
    Call<MoviesResponse> getMovies(@Path("type") String type);

    @GET("{id}/reviews?api_key=" + Contracts.API_KEY)
    Call<Review> getReviews(@Path("id") String id);

    @GET("{id}/videos?api_key=" + Contracts.API_KEY)
    Call<Trailer> getTrailers(@Path("id") String id);

    /**
     * Search types that can be used
     */
    enum SearchType {
        POPULAR_MOVIES("popular"),
        TOPRATED_MOVIES("top_rated");

        private final String val;

        SearchType(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }
}
