package com.mz.nlpservice.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Album {
	Long _id;
	String id;
	String source;
	Integer type;
	String sourceID;
	String name;
	String cover;
	String tag;
	@SerializedName(value = "artistID", alternate = {"artistIDS"})
	List<Integer> artistID;
	@SerializedName(value = "artistName", alternate = {"artistNames"})
	List<String> artistName;
	List<String> songs;
	List<Song> songDetail;
	Long date;

	public List<Song> getSongDetail() {
		return songDetail;
	}

	public void setSongDetail(List<Song> songDetail) {
		this.songDetail = songDetail;
	}

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public List<Integer> getArtistID() {
		return artistID;
	}

	public void setArtistID(List<Integer> artistID) {
		this.artistID = artistID;
	}

	public List<String> getArtistName() {
		return artistName;
	}

	public void setArtistName(List<String> artistName) {
		this.artistName = artistName;
	}

	public List<String> getSongs() {
		return songs;
	}

	public void setSongs(List<String> songs) {
		this.songs = songs;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}
}
