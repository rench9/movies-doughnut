package com.yahoo.rahul.moviesdoughnut.api.response;

public class MovieExternalIdsResponse extends Response {
    int id;
    String imdb_id;
    String facebook_id;
    String instagram_id;
    String twitter_id;

    public int getId() {
        return id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public String getInstagram_id() {
        return instagram_id;
    }

    public String getTwitter_id() {
        return twitter_id;
    }
}
