package com.mz.nlpservice.model;

public class Device {
	private Integer _id;
	private String license;
	private String device;
	@Override
	public int hashCode() {
		return (this.license + "_" + this.device).hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		Device obj = (Device) o;
		return this.license.equals(obj.license) &&
				this.device.equals(obj.device);
	}
	
}
