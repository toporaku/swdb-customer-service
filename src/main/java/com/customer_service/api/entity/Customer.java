package com.customer_service.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Integer customer_id;

	@Column(name = "name")
	private String name;

	@Column(name = "surname")
	private String surname;

	@Column(name = "rfc")
	private String rfc;

	@Column(name = "mail")
	private String mail;

	@Column(name = "phone_number")
	private String phone_number;

	@Column(name = "address")
	private String address;

	@Column(name = "user_id")
	private Integer user_id;

	@Column(name = "region_id")
	private Integer region_id;

	@Column(name = "status")	
	private Integer status;
	
	public Customer() {
		
	}

	public Customer(Integer customer_id, String name, String surname, String rfc, String mail, String phone_number,
			String address, Integer user_id, Integer region_id, Integer status) {
		super();
		this.customer_id = customer_id;
		this.name = name;
		this.surname = surname;
		this.rfc = rfc;
		this.mail = mail;
		this.phone_number = phone_number;
		this.address = address;
		this.user_id = user_id;
		this.region_id = region_id;
		this.status = status;
	}

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

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
