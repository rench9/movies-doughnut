package com.yahoo.rahul.moviesdoughnut.api.response;

import com.yahoo.rahul.moviesdoughnut.api.response.model.Movie;

public class PopularMovieResponse extends Response {
    int page;
    int total_results;
    int total_pages;
    Movie[] results;

    public int getPage() {
        return page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public Movie[] getResults() {
        return results;
    }
}

