package com.yahoo.rahul.moviesdoughnut.api.response;

public class Response {
    boolean success;
    String status_message;
    int status_code;

    public boolean isSuccess() {
        return success;
    }

    public String getStatus_message() {
        return status_message;
    }

    public int getStatus_code() {
        return status_code;
    }
}
