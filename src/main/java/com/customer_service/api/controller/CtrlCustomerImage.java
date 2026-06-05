package com.customer_service.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer_service.api.dto.DtoCustomerImageIn;
import com.customer_service.api.service.SvcCustomerImage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer-image")
@Tag(name = "Customer Image", description = "Carga de imagenes de clientes")
public class CtrlCustomerImage {
	
	@Autowired
    SvcCustomerImage svc;
	
	@PostMapping
	// @spec CUST-IMAGE-001
	@Operation(summary = "Agrega imagen de cliente", description = "Registra una nueva imagen de cliente")
	public ResponseEntity<Void> createCustomerImage(
			@Valid @RequestBody DtoCustomerImageIn in){
		svc.upload(in);
		return ResponseEntity.ok().build();
	}


}
