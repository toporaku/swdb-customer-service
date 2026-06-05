package com.customer_service.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.customer_service.api.entity.CustomerImage;

@Repository
public interface RepoCustomerImage extends JpaRepository<CustomerImage, Integer> {

	CustomerImage findByCustomerId(Integer customer_id);
}
