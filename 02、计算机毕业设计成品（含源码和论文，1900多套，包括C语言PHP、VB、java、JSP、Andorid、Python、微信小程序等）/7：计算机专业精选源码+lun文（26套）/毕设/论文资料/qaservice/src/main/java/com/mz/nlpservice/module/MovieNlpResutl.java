package com.mz.nlpservice.module;

/**
 * Created by gaozhenan on 2017/1/19.
 */
public class MovieNlpResutl extends NlpResult {
    String actor;
    String area;
    String actorOrArea;
    String cat;
    String name;
    int beginYear;
    int endYear;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getActorOrArea() {
        return actorOrArea;
    }

    public void setActorOrArea(String actorOrArea) {
        this.actorOrArea = actorOrArea;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBeginYear() {
        return beginYear;
    }

    public void setBeginYear(int beginYear) {
        this.beginYear = beginYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    @Override
    public String toString() {
        return "MovieNlpResutl{" +
                "actor='" + actor + '\'' +
                ", area='" + area + '\'' +
                ", actorOrArea='" + actorOrArea + '\'' +
                ", cat='" + cat + '\'' +
                ", name='" + name + '\'' +
                ", beginYear=" + beginYear +
                ", endYear=" + endYear +
                '}';
    }
}
