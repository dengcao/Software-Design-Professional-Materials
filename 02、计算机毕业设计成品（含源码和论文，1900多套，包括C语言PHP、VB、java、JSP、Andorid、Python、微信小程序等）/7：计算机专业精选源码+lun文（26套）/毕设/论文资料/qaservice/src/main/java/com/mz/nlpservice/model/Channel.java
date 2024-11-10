package com.mz.nlpservice.model;

import java.util.List;

public class Channel {

	//for db
	Integer _id;
	//for return
	Integer id;
	String name;
	String type;
	String picture;
	Long date;
	String extData;

	public String getExtData() {
		return extData;
	}

	public void setExtData(String extData) {
		this.extData = extData;
	}

	List<Integer> albums;

	
	public Integer get_id() {
		return _id;
	}

	public void set_id(Integer _id) {
		this._id = _id;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public List<Integer> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Integer> albums) {
		this.albums = albums;
	}

	public String getName() {
		return name;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public void setName(String name) {
		this.name = name;
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

}
