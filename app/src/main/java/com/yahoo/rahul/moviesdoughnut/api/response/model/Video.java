package com.yahoo.rahul.moviesdoughnut.api.response.model;

public class Video {
    String id;
    String iso_639_1;
    String iso_3166_1;
    String key;
    String name;
    String site;
    int size; // Allowed Values: 360, 480, 720, 1080
    String type; // Allowed Values: Trailer, Teaser, Clip, Featurette

    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}

