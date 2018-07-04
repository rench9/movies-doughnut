package com.yahoo.rahul.moviesdoughnut.api.response;

import com.yahoo.rahul.moviesdoughnut.api.response.model.Movie;

public class RecommendedMoviesResponse extends Response {
    int page;
    int total_pages;
    int total_results;
    Movie[] results;

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public Movie[] getResults() {
        return results;
    }
}
