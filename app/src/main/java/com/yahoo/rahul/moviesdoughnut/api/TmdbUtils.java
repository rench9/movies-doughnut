package com.yahoo.rahul.moviesdoughnut.api;

public class TmdbUtils {
    public static String POSTER_W_45 = "w45";
    public static String POSTER_W_92 = "w92";
    public static String POSTER_W_154 = "w154";
    public static String POSTER_W_185 = "w185";
    public static String POSTER_W_300 = "w300";
    public static String POSTER_W_500 = "w500";
    public static String POSTER_W_ORIGINAL = "original";

    public static String BACKDROP_W_300 = "w300";
    public static String BACKDROP_W_780 = "w780";
    public static String BACKDROP_W_1280 = "w1280";
    public static String BACKDROP_W_ORIGINAL = "original";

    public static String PROFILE_W_45 = "w45";
    public static String PROFILE_W_185 = "w185";
    public static String PROFILE_W_632 = "h632";
    public static String PROFILE_W_ORIGINAL = "original";

    public static String generateImageUrl(String imagePath, String imageSize) {
        return String.format("https://image.tmdb.org/t/p/%s%s", imageSize, imagePath);
    }
}
