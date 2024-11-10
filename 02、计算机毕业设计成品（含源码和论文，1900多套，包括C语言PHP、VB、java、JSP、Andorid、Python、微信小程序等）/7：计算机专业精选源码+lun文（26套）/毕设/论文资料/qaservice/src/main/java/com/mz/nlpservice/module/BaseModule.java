package com.mz.nlpservice.module;

/**
 * Created by gaozhenan on 2016/9/7.
 */
public interface BaseModule {

    class Result {
        public NlpResult nlpResutl;
        public String data;
    }

    String getDomain();

    Result doService(String deviceID, String keyWord);

    boolean check(String deviceID, String sentence);
}
