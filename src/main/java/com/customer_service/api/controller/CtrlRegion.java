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

import com.customer_service.api.dto.DtoRegionIn;
import com.customer_service.api.entity.Region;
import com.customer_service.api.service.SvcRegion;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/region")
@Tag(name = "Region", description = "Catálogo de regiones")
public class CtrlRegion {
	
	@Autowired
	SvcRegion svc;
	
	@GetMapping
	// @spec CUST-REG-001
	@Operation(summary = "Consultar regiones", description = "Lista las regiones registradas en el sistema")
	public ResponseEntity<List<Region>> findAll() {
		return ResponseEntity.ok(svc.findAll());
	}
	
	@GetMapping("/active")
	// @spec CUST-REG-002
	@Operation(summary = "Consultar regiones activas", description = "Lista las regiones activas en el sistema")
	public ResponseEntity<List<Region>> findActive() {
		return ResponseEntity.ok(svc.findActive());
	}
	
	@PostMapping
	// @spec CUST-REG-003
	@Operation(summary = "Registrar región", description = "Registra una nueva región")
	public ResponseEntity<Void> create(@Valid @RequestBody DtoRegionIn in){
		svc.create(in);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{id}")
	// @spec CUST-REG-004
	@Operation(summary = "Actualizar región", description = "Actualiza una región existente")
	public ResponseEntity<Void> update(@Valid @RequestBody DtoRegionIn in, 
			@PathVariable Integer id){
		svc.update(in, id);
		return ResponseEntity.ok().build();
	}
 
	@PatchMapping("/{id}/enable")
	// @spec CUST-REG-005
	@Operation(summary = "Activar región", description = "Cambia el estatus a activada de una región existente")
	public ResponseEntity<Void> enable(@PathVariable Integer id){
		svc.enable(id);
		return ResponseEntity.ok().build();
	}
 
	@PatchMapping("/{id}/disable")
	// @spec CUST-REG-006
	@Operation(summary = "Desactivar", description = "Cambia el estatus a desactivada de una región existente")
	public ResponseEntity<Void> disable(@PathVariable Integer id){
		svc.disable(id);
		return ResponseEntity.ok().build();
	}
	
}
