package com.customer_service.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.customer_service.api.dto.DtoRegionIn;
import com.customer_service.api.entity.Region;
import com.customer_service.api.repository.RepoRegion;
import com.customer_service.exception.ApiException;
import com.customer_service.exception.DBAccessException;

@Service
public class SvcRegionImp implements SvcRegion {

	@Autowired
	RepoRegion repo;

	@Override
	public List<Region> findAll() {
		try {
			return repo.findAll();
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public List<Region> findActive() {
		try {
			return repo.findActive();
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public void create(DtoRegionIn in) {
		try {
			repo.create(in.getRegion(), in.getTag());
		} catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_region"))
				throw new ApiException(HttpStatus.CONFLICT, 
						"El nombre de la región ya está registrado");
			
			if (e.getLocalizedMessage().contains("ux_tag"))
				throw new ApiException(HttpStatus.CONFLICT, 
						"El tag de la región ya está registrado");

			throw new DBAccessException();
		}
	}

	@Override
	public void update(DtoRegionIn in, Integer id) {
		try {
			validateId(id);
			repo.update(id, in.getRegion(), in.getTag());
		} catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_region"))
				throw new ApiException(HttpStatus.CONFLICT, 
						"El nombre de la región ya está registrado");
			
			if (e.getLocalizedMessage().contains("ux_tag"))
				throw new ApiException(HttpStatus.CONFLICT, 
						"El tag de la región ya está registrado");

			throw new DBAccessException();
		}
	}

	@Override
	public void enable(Integer id) {
		try {
			validateId(id);
			repo.enable(id);
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public void disable(Integer id) {
		try {
			validateId(id);
			repo.disable(id);
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public void switchStatus(Integer id, Integer status) {
		try {
			validateId(id);
			repo.switchStatus(id, status);
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}
	
	private void validateId(Integer id) { 
		if(repo.findById(id).isEmpty())
			throw new ApiException(HttpStatus.NOT_FOUND, 
					"El id de la región no existe");
	}

}
