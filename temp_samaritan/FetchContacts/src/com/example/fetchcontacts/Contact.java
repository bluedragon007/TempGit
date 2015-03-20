package com.example.fetchcontacts;

public class Contact {

	String contact_name = "";

	String contact_number = "";
	String contact_pic_uri = "";
	String location = "";

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Contact() {
		// TODO Auto-generated constructor stub
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getContact_pic_uri() {
		return contact_pic_uri;
	}

	public void setContact_pic_uri(String contact_pic_uri) {
		this.contact_pic_uri = contact_pic_uri;
	}

	public Contact(String contact_name, String contact_number, String contact_pic_uri, String location) {
		super();
		this.contact_name = contact_name;
		this.contact_number = contact_number;
		this.contact_pic_uri = contact_pic_uri;
		this.location = location;
	}
}
