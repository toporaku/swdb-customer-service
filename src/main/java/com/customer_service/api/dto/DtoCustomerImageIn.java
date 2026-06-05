package com.customer_service.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

public class DtoCustomerImageIn {

	@JsonProperty("customer_id")
	@NotNull(message="El customerId es obligatorio")
	private Integer customerId;


	@JsonProperty("image")
	@NotNull(message="El image es obligatorio")
	private String image;


	public Integer getCustomerId() {
		return customerId;
	}


	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}
	
	
}
