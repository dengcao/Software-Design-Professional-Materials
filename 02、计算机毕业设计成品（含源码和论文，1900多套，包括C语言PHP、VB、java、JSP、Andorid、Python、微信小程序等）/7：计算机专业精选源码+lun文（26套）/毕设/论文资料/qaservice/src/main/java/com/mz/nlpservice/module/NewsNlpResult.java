package com.mz.nlpservice.module;

/**
 * Created by gaozhenan on 2016/12/21.
 */

public class NewsNlpResult extends NlpResult {
    private String channel;
    private String keyword;
    private String searchWord;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
