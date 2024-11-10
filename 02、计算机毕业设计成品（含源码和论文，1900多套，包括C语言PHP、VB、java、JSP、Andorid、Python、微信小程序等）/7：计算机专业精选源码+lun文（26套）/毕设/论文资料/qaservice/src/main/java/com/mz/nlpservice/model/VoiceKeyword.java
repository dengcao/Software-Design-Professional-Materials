package com.mz.nlpservice.model;

/**
 * Created by gaozhenan on 2016/8/10.
 */
public class VoiceKeyword {
    private Integer _id;
    private Integer id;
    private String type;
    private String content;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
