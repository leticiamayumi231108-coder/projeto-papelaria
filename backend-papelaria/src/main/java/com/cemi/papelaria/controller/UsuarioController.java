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

import com.cemi.papelaria.dto.request.LoginRequest;
import com.cemi.papelaria.dto.request.UsuarioRequest;
import com.cemi.papelaria.dto.response.UsuarioResponse;
import com.cemi.papelaria.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping("/login")
	public ResponseEntity<UsuarioResponse> login(@Valid @RequestBody LoginRequest request) {
		UsuarioResponse obj = usuarioService.login(request);
		return ResponseEntity.ok(obj);
	}

	@PostMapping
	public ResponseEntity<UsuarioResponse> adicionar(@Valid @RequestBody UsuarioRequest request) {
		UsuarioResponse obj = usuarioService.adicionar(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(obj);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
		UsuarioResponse obj = usuarioService.buscarPorId(id);
		return ResponseEntity.ok(obj);
	}

	@GetMapping
	public ResponseEntity<List<UsuarioResponse>> buscarTodos() {
		List<UsuarioResponse> list = usuarioService.buscarTodos();
		return ResponseEntity.ok(list);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UsuarioResponse> alterar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest requestNovo) {
		UsuarioResponse obj = usuarioService.alterar(id, requestNovo);
		return ResponseEntity.ok(obj);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		usuarioService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
