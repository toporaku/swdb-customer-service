package com.customer_service.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.customer_service.api.dto.DtoCustomerImageIn;
import com.customer_service.api.entity.CustomerImage;
import com.customer_service.api.repository.RepoCustomerImage;
import com.customer_service.exception.ApiException;
import com.customer_service.exception.DBAccessException;

import com.customer_service.api.repository.RepoCustomer;

@Service
public class SvcCustomerImageImp implements SvcCustomerImage {
	
	@Autowired
	RepoCustomerImage repo;

	@Autowired
	RepoCustomer repoCustomer;
	
	@Value("${app.upload.dir}")
	private String uploadDir; // uploads
	
	@Value("${app.upload.images}")
	private String uploadImages; // img

	@Override
	public void upload(DtoCustomerImageIn in) {
		try {
			
			// PASO -1: Validar que el cliente exista
			if(repoCustomer.findById(in.getCustomerId()).isEmpty())
				throw new ApiException(HttpStatus.NOT_FOUND, 
						"El id del cliente no existe");
			
			// PASO 0: Validar prefijo del String de Base64
			
			// Eliminar el prefijo "data:image/png;base64," si existe
			if (in.getImage().startsWith("data:image")) {
				int commaIndex = in.getImage().indexOf(",");
				if (commaIndex != -1) {
					in.setImage(in.getImage().substring(commaIndex + 1));
				}
			}

			
			// PASO 1: Convertir el Base64 en un File
			
			// Decodifica la cadena Base64 a bytes
			byte[] imageBytes = Base64.getDecoder().decode(in.getImage());

			// Genera un nombre único para la imagen (se asume extensión PNG)
			String fileName = UUID.randomUUID().toString() + ".png";

			// Construye la ruta completa donde se guardará la imagen
			Path imagePath = Paths.get(uploadDir, uploadImages, "customer", fileName);
			
			
			// PASO 2: Guardar el File en el sistema de archivos
			
			// Asegurarse de que el directorio exista
			Files.createDirectories(imagePath.getParent());

			// Escribir el archivo en el sistema de archivos
			Files.write(imagePath, imageBytes);
			
			
			// PASO 3: Guardar la ruta en la base de datos
			
			// Crear la entidad CustomerImage y guardar la URL en la base de datos
			CustomerImage customerImage = repo.findByCustomerId(in.getCustomerId());
			if (customerImage == null) {
				customerImage = new CustomerImage();
				customerImage.setCustomerId(in.getCustomerId());
				customerImage.setStatus(1);
			}
			customerImage.setImage("/customer/" + fileName);

			// Actualizamos la ruta de la imagen
			repo.save(customerImage);
			
		} catch (DataAccessException e) {
			throw new DBAccessException();
		} catch (IOException e) {
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, 
					"Error al guardar el archivo");
		}

	}

}
