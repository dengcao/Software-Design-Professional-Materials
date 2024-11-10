package com.mz.nlpservice.model;

public class License {
	private String _id;
	private Integer type;
	private String secret;
	
	public static enum TypeCode {
		CHECK_CLIENT(0), 
		ALLOW_ALL(1);
		
		public final int val;
		 
		TypeCode(int val) {
			 this.val = val;
		 }
	}
	
	
	public String getId() {
		return _id;
	}
	public void setId(String _id) {
		this._id = _id;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	

}
