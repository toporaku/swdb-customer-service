package com.customer_service.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer_service.api.dto.DtoCustomerIn;
import com.customer_service.api.dto.DtoCustomerListOut;
import com.customer_service.api.dto.DtoCustomerOut;
import com.customer_service.api.service.SvcCustomer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer")
@Tag(name = "Customer", description = "Administración de clientes")
public class CtrlCustomer {

	@Autowired
	SvcCustomer svc;
	
	@GetMapping
	// @spec CUST-PROFILE-001
	@Operation(summary = "Consultar clientes", description = "Lista los clientes registrados en el sistema")
	public ResponseEntity<List<DtoCustomerListOut>> findAll() {
		return ResponseEntity.ok(svc.findAll());
	}
	
	@GetMapping("/{id}")
	// @spec CUST-PROFILE-002
	@Operation(summary = "Consultar cliente", description = "Consulta el detalle de un cliente")
	public ResponseEntity<DtoCustomerOut> getCustomer(@PathVariable Integer id){
		return ResponseEntity.ok(svc.findById(id));
	}
	
	@PostMapping
	// @spec CUST-PROFILE-003
	@Operation(summary = "Registrar cliente", description = "Registra un nuevo cliente")
	public ResponseEntity<Void> create(@Valid @RequestBody DtoCustomerIn in){
		svc.create(in);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{id}")
	// @spec CUST-PROFILE-004
	@Operation(summary = "Actualizar cliente", description = "Actualiza un cliente existente")
	public ResponseEntity<Void> update(@Valid @RequestBody DtoCustomerIn in, 
			@PathVariable Integer id){
		svc.update(id, in);
		return ResponseEntity.ok().build();
	}
 
	@PatchMapping("/{id}/enable")
	// @spec CUST-PROFILE-005
	@Operation(summary = "Activar cliente", description = "Cambia el estatus a activado de un cliente existente")
	public ResponseEntity<Void> enable(@PathVariable Integer id){
		svc.enable(id);
		return ResponseEntity.ok().build();
	}
 
	@PatchMapping("/{id}/disable")
	// @spec CUST-PROFILE-006
	@Operation(summary = "Desactivar cliente", description = "Cambia el estatus a desactivado de un cliente existente")
	public ResponseEntity<Void> disable(@PathVariable Integer id){
		svc.disable(id);
		return ResponseEntity.ok().build();
	}
}
