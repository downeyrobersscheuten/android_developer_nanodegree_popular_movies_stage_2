package com.robersscheuten.downey.moviesappstageone.network;

import com.robersscheuten.downey.moviesappstageone.models.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Retrofit Service class.
 */
public interface MoviesService {

    @GET("{type}?api_key=")
    Call<MoviesResponse> getMovies(@Path("type") String type);

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
