package com.cemi.papelaria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cemi.papelaria.domain.CategoriaProduto;
import com.cemi.papelaria.domain.Produto;
import com.cemi.papelaria.dto.request.ProdutoRequest;
import com.cemi.papelaria.dto.response.ProdutoResponse;
import com.cemi.papelaria.repository.CategoriaProdutoRepository;
import com.cemi.papelaria.repository.ProdutoRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;

	public ProdutoResponse adicionar(ProdutoRequest request) {
		Produto produto = toEntity(request);
		produto = produtoRepository.save(produto);
		return toResponse(produto);
	}

	public ProdutoResponse buscarPorId(Long id) {
		Produto produto = findEntityById(id);
		return toResponse(produto);
	}

	public List<ProdutoResponse> buscarTodos() {
		return produtoRepository.findAll().stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	public ProdutoResponse alterar(Long id, ProdutoRequest request) {
		Produto objAntigo = findEntityById(id);
		atualizarDados(objAntigo, request);
		objAntigo = produtoRepository.save(objAntigo);
		return toResponse(objAntigo);
	}

	public void excluir(Long id) {
		findEntityById(id);
		produtoRepository.deleteById(id);
	}

	private void atualizarDados(Produto objAntigo, ProdutoRequest request) {
		objAntigo.setNomeProduto(request.getNomeProduto());
		objAntigo.setDescricao(request.getDescricao());

		CategoriaProduto categoria = categoriaProdutoRepository.findById(request.getIdCategoriaProduto())
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada! Id: " + request.getIdCategoriaProduto()));
		objAntigo.setCategoria(categoria);

		objAntigo.setPreco(request.getPreco());
		objAntigo.setEstoque(request.getEstoque());
		objAntigo.setDataValidade(request.getDataValidade());
	}

	//Método que converte o DTO request em uma entidade 
	private Produto toEntity(ProdutoRequest request) {
		Produto produto = new Produto();
		produto.setNomeProduto(request.getNomeProduto());
		produto.setDescricao(request.getDescricao());

		CategoriaProduto categoria = categoriaProdutoRepository.findById(request.getIdCategoriaProduto())
				.orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada! Id: " + request.getIdCategoriaProduto()));
		produto.setCategoria(categoria);

		produto.setPreco(request.getPreco());
		produto.setEstoque(request.getEstoque());
		produto.setDataValidade(request.getDataValidade());

		return produto;
	}

	//Método que converte o DTO response em uma entidade 
	private ProdutoResponse toResponse(Produto produto) {
		ProdutoResponse response = new ProdutoResponse();
		response.setIdProduto(produto.getIdProduto());
		response.setNomeProduto(produto.getNomeProduto());
		response.setDescricao(produto.getDescricao());
		response.setIdCategoriaProduto(produto.getCategoria().getIdCategoriaProduto());
		response.setNomeCategoria(produto.getCategoria().getNomeCategoria());
		response.setPreco(produto.getPreco());
		response.setEstoque(produto.getEstoque());
		response.setDataValidade(produto.getDataValidade());

		return response;
	}

	private Produto findEntityById(Long id) {
		return produtoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado! Id: " + id));
	}
}
