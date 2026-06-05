package com.customer_service.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class DtoCustomerIn {

	@JsonProperty("name")
	@Pattern(regexp="[a-zA-ZÁÉÍÓÚáéíóúñÑ\s]*", message="El name contiene caracteres inválidos")
	@NotNull(message="El name es obligatorio")
	private String name;

	@JsonProperty("surname")
	@Pattern(regexp="[a-zA-ZÁÉÍÓÚáéíóúñÑ\s]*", message="El surname contiene caracteres inválidos")
	@NotNull(message="El surname es obligatorio")
	private String surname;
	
	@JsonProperty("rfc")
	@Pattern(regexp = "^(?i)[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$", message = "El rfc tiene un formato inválido")
	@NotNull(message="El rfc es obligatorio")
	private String rfc;

	@JsonProperty("mail")
	@Pattern(regexp="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="El mail tiene un formato inválido")
	@NotNull(message="El mail es obligatorio")
	private String mail;

	@JsonProperty("phone_number")
	@Pattern(regexp = "^\\+?\\d{10,15}$", message = "El phone_number tiene un formato inválido")
	@NotNull(message="El phone_number es obligatorio")
	private String phone_number;

	@JsonProperty("address")
	@NotNull(message="El address es obligatorio")
	private String address;

	@JsonProperty("user_id")
	@NotNull(message="El user_id es obligatorio")
	private Integer user_id;

	@JsonProperty("region_id")
	@NotNull(message="El region_id es obligatorio")
	private Integer region_id;

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
	
	
}
