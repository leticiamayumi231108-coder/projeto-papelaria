package com.cemi.papelaria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cemi.papelaria.domain.CategoriaProduto;
import com.cemi.papelaria.dto.response.CategoriaProdutoResponse;
import com.cemi.papelaria.repository.CategoriaProdutoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriaProdutoService {

	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;

	public CategoriaProdutoResponse buscarPorId(Long id) {
		CategoriaProduto categoria = findEntityById(id);
		return toResponse(categoria);
	}

	public List<CategoriaProdutoResponse> buscarTodos() {
		return categoriaProdutoRepository.findAll().stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	private CategoriaProdutoResponse toResponse(CategoriaProduto categoria) {
		CategoriaProdutoResponse response = new CategoriaProdutoResponse();
		response.setIdCategoriaProduto(categoria.getIdCategoriaProduto());
		response.setNomeCategoria(categoria.getNomeCategoria());
		return response;
	}

	private CategoriaProduto findEntityById(Long id) {
		return categoriaProdutoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada! Id: " + id));
	}
}
