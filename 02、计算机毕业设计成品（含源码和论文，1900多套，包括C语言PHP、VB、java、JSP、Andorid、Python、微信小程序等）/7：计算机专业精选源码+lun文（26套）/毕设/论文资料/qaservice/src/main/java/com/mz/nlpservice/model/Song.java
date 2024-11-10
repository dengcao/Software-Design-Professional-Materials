package com.mz.nlpservice.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Song {
	Integer _id;
	String id;
	String source;
	String sourceID;
	String name;
	String cover;
	String tag;
	String playurl;
	Long date;
	@SerializedName(value = "artistID", alternate = {"artistIDS"})
	List<String> artistID;
	@SerializedName(value = "artistName", alternate = {"artistNames"})
	List<String> artistName;

	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPlayurl() {
		return playurl;
	}

	public void setPlayurl(String playurl) {
		this.playurl = playurl;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public List<String> getArtistID() {
		return artistID;
	}

	public void setArtistID(List<String> artistID) {
		this.artistID = artistID;
	}

	public List<String> getArtistName() {
		return artistName;
	}

	public void setArtistName(List<String> artistName) {
		this.artistName = artistName;
	}

}
