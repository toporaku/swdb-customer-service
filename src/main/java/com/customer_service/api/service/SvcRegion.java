package com.customer_service.api.service;

import java.util.List;

import com.customer_service.api.dto.DtoRegionIn;
import com.customer_service.api.entity.Region;

public interface SvcRegion {

	public List<Region> findAll();
	public List<Region> findActive();
	
	public void create(DtoRegionIn in);
	public void update(DtoRegionIn in, Integer id);
	
	public void enable(Integer id);
	public void disable(Integer id);
	
	// opcional
	public void switchStatus(Integer id, Integer status);
}

