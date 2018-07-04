package com.yahoo.rahul.moviesdoughnut.api.response;

import com.yahoo.rahul.moviesdoughnut.api.response.model.Cast;
import com.yahoo.rahul.moviesdoughnut.api.response.model.Crew;

public class MovieCreditsResponse extends Response {
    int id;
    Cast[] cast;
    Crew[] crew;

    public int getId() {
        return id;
    }

    public Cast[] getCast() {
        return cast;
    }

    public Crew[] getCrew() {
        return crew;
    }
}
