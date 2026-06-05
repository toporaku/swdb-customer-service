package com.customer_service.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.customer_service.api.dto.DtoCustomerOut;
import com.customer_service.api.entity.Customer;

@Repository
public interface RepoCustomer extends JpaRepository<Customer, Integer> {

	@Query(value = "SELECT c.customer_id, c.name, c.surname, c.rfc, c.mail, c.phone_number, c. address, r.region, ci.image "
			+ "FROM customer c " 
			+ "INNER JOIN region r ON r.region_id = c.region_id "
			+ "INNER JOIN customer_image ci ON c.customer_id = ci.customer_id "
			+ "WHERE c.customer_id = :customer_id AND ci.status = 1;", 
		nativeQuery = true)
	DtoCustomerOut getCustomer(Integer customer_id);
}
