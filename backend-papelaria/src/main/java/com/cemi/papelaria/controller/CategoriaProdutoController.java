package com.cemi.papelaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cemi.papelaria.dto.response.CategoriaProdutoResponse;
import com.cemi.papelaria.service.CategoriaProdutoService;

@RestController
@RequestMapping("/categorias-produto")
public class CategoriaProdutoController {

	@Autowired
	private CategoriaProdutoService categoriaProdutoService;

	@GetMapping("/{id}")
	public ResponseEntity<CategoriaProdutoResponse> buscarPorId(@PathVariable Long id) {
		CategoriaProdutoResponse obj = categoriaProdutoService.buscarPorId(id);
		return ResponseEntity.ok(obj);
	}

	@GetMapping
	public ResponseEntity<List<CategoriaProdutoResponse>> buscarTodos() {
		List<CategoriaProdutoResponse> list = categoriaProdutoService.buscarTodos();
		return ResponseEntity.ok(list);
	}
}
