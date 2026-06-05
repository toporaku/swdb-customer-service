package com.customer_service.api.service;

import java.util.List;

import com.customer_service.api.dto.DtoCustomerIn;
import com.customer_service.api.dto.DtoCustomerListOut;
import com.customer_service.api.dto.DtoCustomerOut;

public interface SvcCustomer {

	public List<DtoCustomerListOut> findAll();
	public DtoCustomerOut findById(Integer id);
	
	public void create(DtoCustomerIn in);
	public void update(Integer id, DtoCustomerIn in);
	
	public void enable(Integer id);
	public void disable(Integer id);
}
