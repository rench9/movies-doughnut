package com.yahoo.rahul.moviesdoughnut.api.response;

import com.yahoo.rahul.moviesdoughnut.api.response.model.Video;

public class MovieVideosResponse extends Response {
    int id;
    Video[] results;

    public int getId() {
        return id;
    }

    public Video[] getResults() {
        return results;
    }
}
