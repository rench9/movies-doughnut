package com.yahoo.rahul.moviesdoughnut.api.response.model;

public class Poster {
    double aspect_ratio;
    String file_path;
    int height;
    String iso_639_1;
    double vote_average;
    int vote_count;
    int width;

    public double getAspect_ratio() {
        return aspect_ratio;
    }

    public String getFile_path() {
        return file_path;
    }

    public int getHeight() {
        return height;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public int getWidth() {
        return width;
    }
}

