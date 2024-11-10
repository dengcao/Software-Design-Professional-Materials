package com.mz.nlpservice.model;

import java.io.InputStream;

/**
 * Created by meizu on 2016/7/19.
 */
public class CommonParam {

    public String partnerID;
    public String deviceID;
    public String sourceType;
    public String content;
    public InputStream file;

    @Override
    public String toString() {
        return "\npartnerid:" + partnerID
                + "\ndeviceid:" + deviceID
                + "\nsourceType:" + sourceType
                + "\ncontent:" + content
                + "\nfile:" + file;
    }
}
