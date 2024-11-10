package com.mz.nlpservice.model;

import java.util.List;

/**
 * Created by gaozhenan on 2016/9/5.
 */
public class MusicRes {
    public int status;
    public String message;
    public List<Album> album;

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

    public List<Album> getAlbum() {
        return album;
    }

    public void setAlbum(List<Album> album) {
        this.album = album;
    }
}
