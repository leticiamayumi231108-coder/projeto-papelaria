package com.cemi.papelaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cemi.papelaria.dto.response.ItemVendaResponse;
import com.cemi.papelaria.service.ItemVendaService;

@RestController
@RequestMapping("/itens-venda")
public class ItemVendaController {

	@Autowired
	private ItemVendaService itemVendaService;

	@GetMapping("/{id}")
	public ResponseEntity<ItemVendaResponse> buscarPorId(@PathVariable Long id) {
		ItemVendaResponse obj = itemVendaService.buscarPorId(id);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping
	public ResponseEntity<List<ItemVendaResponse>> buscarTodos() {
		List<ItemVendaResponse> list = itemVendaService.buscarTodos();
		return ResponseEntity.ok().body(list);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		itemVendaService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
