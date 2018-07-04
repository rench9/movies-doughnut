package com.yahoo.rahul.moviesdoughnut.api.response;

import com.yahoo.rahul.moviesdoughnut.api.response.model.Backdrop;
import com.yahoo.rahul.moviesdoughnut.api.response.model.Poster;

public class MovieImagesResponse extends Response {
    int id;

    Backdrop[] backdrops;

    Poster[] posters;

    public int getId() {
        return id;
    }

    public Backdrop[] getBackdrops() {
        return backdrops;
    }

    public Poster[] getPosters() {
        return posters;
    }
}
