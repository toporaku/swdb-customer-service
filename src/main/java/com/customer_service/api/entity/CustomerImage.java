package com.customer_service.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_image")
public class CustomerImage {

	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   @Column(name = "customer_image_id")
	   private Integer customerImageId;

	   @Column(name = "customer_id")
	   private Integer customerId;

	   @Column(name = "image")
	   private String image;

	   @Column(name = "status")
	   private Integer status;
	   
	   public CustomerImage() {
		   
	   }

	public CustomerImage(Integer customerImageId, Integer customerId, String image, Integer status) {
		super();
		this.customerImageId = customerImageId;
		this.customerId = customerId;
		this.image = image;
		this.status = status;
	}



	public Integer getCustomerImageId() {
		return customerImageId;
	}

	public void setCustomerImageId(Integer customerImageId) {
		this.customerImageId = customerImageId;
	}

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	   
	   
}
