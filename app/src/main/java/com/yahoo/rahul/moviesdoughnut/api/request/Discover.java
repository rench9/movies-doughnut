package com.yahoo.rahul.moviesdoughnut.api.request;

import com.yahoo.rahul.moviesdoughnut.api.Endpoints;
import com.yahoo.rahul.moviesdoughnut.api.response.DiscoverMovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.LatestMovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieCreditsResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieExternalIdsResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieImagesResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieReviewsResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieVideosResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.NowPlayingMovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.PopularMovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.RecommendedMoviesResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.TopRatedMovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.UpcomingMovieResponse;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Discover {
    private Endpoints endpoints;

    public Discover(Endpoints e) {
        endpoints = e;
    }

    public Maybe<DiscoverMovieResponse> discoverMovies() {
        return endpoints.discoverMovies(1).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<DiscoverMovieResponse> discoverMovies(int page) {
        return endpoints.discoverMovies(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * String sortBy
     * Allowed Values: , popularity.asc, popularity.desc, release_date.asc, release_date.desc, revenue.asc, revenue.desc, primary_release_date.asc, primary_release_date.desc, original_title.asc, original_title.desc, vote_average.asc, vote_average.desc, vote_count.asc, vote_count.desc
     * Default: popularity.desc
     */
    public Maybe<DiscoverMovieResponse> discoverMovies(String sortBy, int page) {
        return endpoints.discoverMovies(sortBy, page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<MovieResponse> getMovieById(int movieId) {
        return endpoints.getMovieById(movieId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<LatestMovieResponse> getLatestMovie() {
        return endpoints.getLatestMovie().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<NowPlayingMovieResponse> getNowPlayingMovies(int page) {
        return endpoints.getNowPlayingMovies(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<NowPlayingMovieResponse> getNowPlayingMovies(int page, String region) {
        return endpoints.getNowPlayingMovies(page, region).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<PopularMovieResponse> getPopularMovies(int page) {
        return endpoints.getPopularMovies(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<PopularMovieResponse> getPopularMovies(int page, String region) {
        return endpoints.getPopularMovies(page, region).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<TopRatedMovieResponse> getTopRatedMovies(int page) {
        return endpoints.getTopRatedMovies(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<TopRatedMovieResponse> getTopRatedMovies(int page, String region) {
        return endpoints.getTopRatedMovies(page, region).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<UpcomingMovieResponse> getUpcomingMovies(int page) {
        return endpoints.getUpcomingMovies(page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<UpcomingMovieResponse> getUpcomingMovies(int page, String region) {
        return endpoints.getUpcomingMovies(page, region).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<RecommendedMoviesResponse> getSimilarMovies(int movieId, int page) {
        return endpoints.getSimilarMovies(movieId, page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<RecommendedMoviesResponse> getRecommendedMovies(int movieId, int page) {
        return endpoints.getRecommendedMovies(movieId, page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<MovieReviewsResponse> getMovieReviews(int movieId, int page) {
        return endpoints.getMovieReviews(movieId, page).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<MovieImagesResponse> getMovieImages(int movieId) {
        return endpoints.getMovieImages(movieId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<MovieVideosResponse> getMovieVideos(int movieId) {
        return endpoints.getMovieVideos(movieId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<MovieCreditsResponse> getMovieCredits(int movieId) {
        return endpoints.getMovieCredits(movieId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<MovieExternalIdsResponse> getMovieExternalIds(int movieId) {
        return endpoints.getMovieExternalIds(movieId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
