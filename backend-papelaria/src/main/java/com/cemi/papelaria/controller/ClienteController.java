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

import com.cemi.papelaria.dto.request.ClienteRequest;
import com.cemi.papelaria.dto.response.ClienteResponse;
import com.cemi.papelaria.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@PostMapping
	public ResponseEntity<ClienteResponse> adicionar(@Valid @RequestBody ClienteRequest cliente) {
		ClienteResponse obj = clienteService.adicionar(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(obj);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
		ClienteResponse obj = clienteService.buscarPorId(id);
		return ResponseEntity.ok(obj);
	}

	@GetMapping
	public ResponseEntity<List<ClienteResponse>> buscarTodos() {
		List<ClienteResponse> list = clienteService.buscarTodos();
		return ResponseEntity.ok(list);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteResponse> alterar(@PathVariable Long id, @Valid @RequestBody ClienteRequest objNovo) {
		ClienteResponse obj = clienteService.alterar(id, objNovo);
		return ResponseEntity.ok(obj);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		clienteService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
