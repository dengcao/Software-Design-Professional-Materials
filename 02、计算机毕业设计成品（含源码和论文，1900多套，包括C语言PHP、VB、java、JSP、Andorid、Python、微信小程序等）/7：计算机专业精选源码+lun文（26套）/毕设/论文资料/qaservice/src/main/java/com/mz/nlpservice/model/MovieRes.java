package com.mz.nlpservice.model;

import java.util.List;

/**
 * Created by gaozhenan on 2016/9/5.
 */
public class MovieRes {
    public int status;
    public String message;
    public List<Movie> movies;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
