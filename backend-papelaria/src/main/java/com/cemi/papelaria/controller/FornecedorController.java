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

import com.cemi.papelaria.dto.request.FornecedorRequest;
import com.cemi.papelaria.dto.response.FornecedorResponse;
import com.cemi.papelaria.service.FornecedorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorController {

	@Autowired
	private FornecedorService fornecedorService;

	@PostMapping
	public ResponseEntity<FornecedorResponse> adicionar(@Valid @RequestBody FornecedorRequest request) {
		FornecedorResponse obj = fornecedorService.adicionar(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(obj);
	}

	@GetMapping("/{id}")
	public ResponseEntity<FornecedorResponse> buscarPorId(@PathVariable Long id) {
		FornecedorResponse obj = fornecedorService.buscarPorId(id);
		return ResponseEntity.ok(obj);
	}

	@GetMapping
	public ResponseEntity<List<FornecedorResponse>> buscarTodos() {
		List<FornecedorResponse> list = fornecedorService.buscarTodos();
		return ResponseEntity.ok(list);
	}

	@PutMapping("/{id}")
	public ResponseEntity<FornecedorResponse> alterar(@PathVariable Long id, @Valid @RequestBody FornecedorRequest requestNovo) {
		FornecedorResponse obj = fornecedorService.alterar(id, requestNovo);
		return ResponseEntity.ok(obj);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		fornecedorService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
