package com.customer_service.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.customer_service.api.dto.DtoCustomerIn;
import com.customer_service.api.dto.DtoCustomerListOut;
import com.customer_service.api.dto.DtoCustomerOut;
import com.customer_service.api.entity.Customer;
import com.customer_service.api.entity.CustomerImage;
import com.customer_service.api.repository.RepoCustomer;
import com.customer_service.api.repository.RepoCustomerImage;
import com.customer_service.api.entity.Region;
import com.customer_service.api.repository.RepoRegion;
import com.customer_service.commons.mapper.MapperCustomer;
import com.customer_service.exception.ApiException;
import com.customer_service.exception.DBAccessException;

@Service
public class SvcCustomerImp implements SvcCustomer {
	
	@Autowired
	RepoCustomer repo;
	
	@Autowired
	RepoCustomerImage repoCustomerImage;

	@Autowired
	RepoRegion repoRegion;
	
	@Autowired
	MapperCustomer mapper;
	
	@Value("${app.upload.dir}")
	private String uploadDir;
	
	@Value("${app.upload.images}")
	private String uploadImages;

	@Override
	public List<DtoCustomerListOut> findAll() {
		try {
			List<Customer> customers = repo.findAll();
			return mapper.toListOut(customers);
		}catch (DataAccessException e) {
			throw new DBAccessException(e);
		}
	}

	@Override
	public DtoCustomerOut findById(Integer id) {
		try {
			
			// 1. Consultar los datos del cliente de la DB
			
			DtoCustomerOut customer = repo.getCustomer(id);
			
			if(customer == null)
				throw new ApiException(HttpStatus.NOT_FOUND, "El id del cliente no existe");
			
			// 2. Leer la imagen del sistema de archivos
			
			String imageUrl = customer.getImage();
			
			// Si la URL comienza con "/" la eliminamos para obtener la ruta relativa
		  	 if (imageUrl.startsWith("/")) {
		       	    imageUrl = imageUrl.substring(1);
		   	}
		  
		  	 // Construir el Path
		  	 Path imagePath = Paths.get(uploadDir, uploadImages, imageUrl);
		  
		  	 // Verifica que el archivo exista
		   	if (!Files.exists(imagePath)) {
		   		customer.setImage(null);
		   	    return customer;
		   	}
		   	
		   	// 3. Convertir la imagen a Base64
		  
			// Leer los bytes de la imagen y codificarlos a Base64
			byte[] imageBytes = Files.readAllBytes(imagePath);
			customer.setImage(Base64.getEncoder().encodeToString(imageBytes));
			
			// 4. Devolvemos todos los datos del cliente
			
			return customer;
			
		} catch (DataAccessException e) {
			throw new DBAccessException();
		} catch (IOException e) {
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al leer el archivo");
	    }

	}

	@Override
	public void create(DtoCustomerIn in) {
		try {
			// Validate region exists and is active
			Region region = repoRegion.findById(in.getRegion_id()).orElseThrow(() -> 
				new ApiException(HttpStatus.NOT_FOUND, "El id de región no existe")
			);
			if (region.getStatus() != 1) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "La región no está activa");
			}

			// 1. Mapear el dto a entity
			Customer customer = mapper.fromDtoIn(in);

			// 2. Save
			customer = repo.save(customer);
			
			CustomerImage customerImage = new CustomerImage();
			customerImage.setCustomerId(customer.getCustomer_id());
			customerImage.setImage("");
			customerImage.setStatus(1);
			repoCustomerImage.save(customerImage);
			
		} catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_customer_rfc"))
				throw new ApiException(HttpStatus.CONFLICT, "El rfc del cliente ya está registrado");
			if (e.getLocalizedMessage().contains("ux_customer_mail"))
				throw new ApiException(HttpStatus.CONFLICT, "El mail del cliente ya está registrado");
			if (e.getLocalizedMessage().contains("fk_customer_region"))
				throw new ApiException(HttpStatus.NOT_FOUND, "El id de región no existe");
			
			throw new DBAccessException();
		}
	}

	@Override
	public void update(Integer id, DtoCustomerIn in) {
		try {
			validateId(id);

			// Validate region exists and is active
			Region region = repoRegion.findById(in.getRegion_id()).orElseThrow(() -> 
				new ApiException(HttpStatus.NOT_FOUND, "El id de región no existe")
			);
			if (region.getStatus() != 1) {
				throw new ApiException(HttpStatus.BAD_REQUEST, "La región no está activa");
			}

			Customer customer = mapper.fromDtoIn(id, in);
			repo.save(customer);
		} catch (DataAccessException e) {
			if (e.getLocalizedMessage().contains("ux_customer_rfc"))
				throw new ApiException(HttpStatus.CONFLICT, "El rfc del cliente ya está registrado");
			if (e.getLocalizedMessage().contains("ux_customer_mail"))
				throw new ApiException(HttpStatus.CONFLICT, "El mail del cliente ya está registrado");
			if (e.getLocalizedMessage().contains("fk_customer_region"))
				throw new ApiException(HttpStatus.NOT_FOUND, "El id de región no existe");
			
			throw new DBAccessException();
		}
	}

	@Override
	public void enable(Integer id) {
		try {
			validateId(id);
			Customer customer = repo.findById(id).get();
			customer.setStatus(1);
			repo.save(customer);
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}

	@Override
	public void disable(Integer id) {
		try {
			validateId(id);
			Customer customer = repo.findById(id).get();
			customer.setStatus(0);
			repo.save(customer);
		} catch (DataAccessException e) {
			throw new DBAccessException();
		}
	}
	
	private void validateId(Integer id) { 
		if(repo.findById(id).isEmpty())
			throw new ApiException(HttpStatus.NOT_FOUND, 
					"El id del cliente no existe");
	}

}
