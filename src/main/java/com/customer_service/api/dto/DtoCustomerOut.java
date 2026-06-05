package com.customer_service.api.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DtoCustomerOut {

	@Id
	private Integer customer_id;
	
	private String name;
	
	private String surname;
	
	private String rfc;
	
	private String mail;
	
	private String phone_number;
	
	private String address;
	
	private String region;
	
	private String image;

	public Integer getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "DtoCustomerOut [customer_id=" + customer_id + ", name=" + name + ", surname=" + surname + ", rfc=" + rfc
				+ ", mail=" + mail + ", phone_number=" + phone_number + ", address=" + address + ", region=" + region
				+ ", image=" + image + "]";
	}
	
	
}
