package com.mz.nlpservice.module;

/**
 * Created by gaozhenan on 2016/12/21.
 */
public class NlpResult {
    private String domain;
    private String sentence;


    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
