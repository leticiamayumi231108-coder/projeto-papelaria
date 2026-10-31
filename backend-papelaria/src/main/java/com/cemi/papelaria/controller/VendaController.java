package com.cemi.papelaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cemi.papelaria.dto.request.VendaRequest;
import com.cemi.papelaria.dto.response.VendaResponse;
import com.cemi.papelaria.service.VendaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vendas")
public class VendaController {

	@Autowired
	private VendaService vendaService;

	@PostMapping
	public ResponseEntity<VendaResponse> adicionar(@Valid @RequestBody VendaRequest venda) {
		VendaResponse obj = vendaService.adicionar(venda);

		return ResponseEntity.status(HttpStatus.CREATED).body(obj);
	}

	@GetMapping("/{id}")
	public ResponseEntity<VendaResponse> buscarPorId(@PathVariable Long id) {
		VendaResponse obj = vendaService.buscarPorId(id);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping
	public ResponseEntity<List<VendaResponse>> buscarTodos() {
		List<VendaResponse> list = vendaService.buscarTodos();
		return ResponseEntity.ok().body(list);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VendaResponse> alterar(@PathVariable Long id, @Valid @RequestBody VendaRequest objNovo) {
		VendaResponse obj = vendaService.alterar(id, objNovo);
		return ResponseEntity.ok().body(obj);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		vendaService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
