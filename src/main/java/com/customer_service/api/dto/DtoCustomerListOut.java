package com.customer_service.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DtoCustomerListOut {

	@JsonProperty("customer_id")
	private Integer customer_id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("surname")
	private String surname;

	@JsonProperty("rfc")
	private String rfc;

	@JsonProperty("status")
	private Integer status;
	
	

	public DtoCustomerListOut(Integer customer_id, String name, String surname, String rfc, Integer status) {
		super();
		this.customer_id = customer_id;
		this.name = name;
		this.surname = surname;
		this.rfc = rfc;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
