package com.mz.nlpservice.module;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaozhenan on 2016/12/21.
 */
public class MusicNlpResult extends NlpResult{
    private List<String> artistOrTags;
    private List<String> songNames;
    private boolean tagFlag = false;
    private boolean artistFlag = false;
    private boolean songNameFlag = false;

    public boolean isTagFlag() {
        return tagFlag;
    }

    public void setTagFlag(boolean tagFlag) {
        this.tagFlag = tagFlag;
    }

    public boolean isArtistFlag() {
        return artistFlag;
    }

    public void setArtistFlag(boolean artistFlag) {
        this.artistFlag = artistFlag;
    }

    public boolean isSongNameFlag() {
        return songNameFlag;
    }

    public void setSongNameFlag(boolean songNameFlag) {
        this.songNameFlag = songNameFlag;
    }

    public List<String> getArtistOrTags() {
        return artistOrTags;
    }

    public void setArtistOrTags(List<String> artistOrTags) {
        this.artistOrTags = artistOrTags;
    }

    public List<String> getSongNames() {
        return songNames;
    }

    public void setSongNames(List<String> songNames) {
        this.songNames = songNames;
    }
}
