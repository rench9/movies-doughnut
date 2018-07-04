package com.yahoo.rahul.moviesdoughnut.api.response;

import com.yahoo.rahul.moviesdoughnut.api.response.model.Review;

public class MovieReviewsResponse extends Response {
    int id;
    int page;
    int total_pages;
    int total_results;
    Review[] results;

    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public Review[] getResults() {
        return results;
    }
}
