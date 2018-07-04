package com.yahoo.rahul.moviesdoughnut.api.response;

import com.yahoo.rahul.moviesdoughnut.api.response.model.Item;
import com.yahoo.rahul.moviesdoughnut.api.response.model.ProductionCompanies;
import com.yahoo.rahul.moviesdoughnut.api.response.model.ProductionCountries;
import com.yahoo.rahul.moviesdoughnut.api.response.model.SpokenLanguages;

public class MovieResponse extends Response {
    boolean adult;
    String backdrop_path;
    Object belongs_to_collection;
    int budget;
    Item[] genres;
    String homepage;
    int id;
    String imdb_id; // minLength: 9, maxLength: 9, pattern: ^tt[0-9]{7}
    String original_language;
    String original_title;
    String overview;
    double popularity;
    String poster_path;
    ProductionCompanies[] production_companies;
    ProductionCountries[] production_countries;
    String release_date;
    int revenue;
    int runtime;
    SpokenLanguages[] spoken_languages;
    String status; //Rumored, Planned, In Production, Post Production, Released, Canceled
    String tagline;
    String title;
    boolean video;
    double vote_average;
    int vote_count;

    public boolean isAdult() {
        return adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public Object getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public int getBudget() {
        return budget;
    }

    public Item[] getGenres() {
        return genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public ProductionCompanies[] getProduction_companies() {
        return production_companies;
    }

    public ProductionCountries[] getProduction_countries() {
        return production_countries;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public SpokenLanguages[] getSpoken_languages() {
        return spoken_languages;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }
}
