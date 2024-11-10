package com.mz.nlpservice.model;

import com.mz.nlpservice.module.NlpResult;

/**
 * Created by meizu on 2016/7/19.
 */
public class ResObj implements Cloneable{
    public int status = Status.SUCCESS;
    public String domain;
    public String message;
    public NlpResult nlpResult;
    public Object data;


    public ResObj() {

    }
    public ResObj(int status, String msg, Object data) {
        this.status = status;
        this.message = msg;
        this.data = data;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getDomain() {
        return domain;
    }

    public NlpResult getNlpResult() {
        return nlpResult;
    }

    public void setNlpResult(NlpResult nlpResult) {
        this.nlpResult = nlpResult;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
