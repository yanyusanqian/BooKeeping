package com.wyk.bookeeping.bean;


public class User {
	private int user_id;
	private String user_phone;
	private String user_password;
	private String user_nikename;
	private String user_image;
	
	public User() {
		super();
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_nikename() {
		return user_nikename;
	}
	public void setUser_nikename(String user_nikename) {
		this.user_nikename = user_nikename;
	}
	public String getUser_image() {
		return user_image;
	}
	public void setUser_image(String user_image) {
		this.user_image = user_image;
	}
	
}
