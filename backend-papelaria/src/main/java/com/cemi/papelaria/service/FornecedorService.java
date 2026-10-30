package com.cemi.papelaria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cemi.papelaria.domain.Fornecedor;
import com.cemi.papelaria.dto.request.FornecedorRequest;
import com.cemi.papelaria.dto.response.FornecedorResponse;
import com.cemi.papelaria.repository.FornecedorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;

	public FornecedorResponse adicionar(FornecedorRequest request) {
		Fornecedor fornecedor = toEntity(request);
		fornecedor = fornecedorRepository.save(fornecedor);
		return toResponse(fornecedor);
	}

	public FornecedorResponse buscarPorId(Long id) {
		Fornecedor fornecedor = findEntityById(id);
		return toResponse(fornecedor);
	}

	public List<FornecedorResponse> buscarTodos() {
		return fornecedorRepository.findAll().stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	public FornecedorResponse alterar(Long id, FornecedorRequest request) {
		Fornecedor fornecedor = findEntityById(id);
		atualizarDados(fornecedor, request);
		fornecedor = fornecedorRepository.save(fornecedor);
		return toResponse(fornecedor);
	}

	public void excluir(Long id) {
		findEntityById(id);
		fornecedorRepository.deleteById(id);
	}

	private void atualizarDados(Fornecedor fornecedor, FornecedorRequest request) {
		fornecedor.setNomeFornecedor(request.getNomeFornecedor());
		fornecedor.setCnpj(request.getCnpj());
		fornecedor.setTelefoneFornecedor(request.getTelefoneFornecedor());
		fornecedor.setEmailFornecedor(request.getEmailFornecedor());
	}

	//Método que converte o DTO request em uma entidade 
	private Fornecedor toEntity(FornecedorRequest request) {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setNomeFornecedor(request.getNomeFornecedor());
		fornecedor.setCnpj(request.getCnpj());
		fornecedor.setTelefoneFornecedor(request.getTelefoneFornecedor());
		fornecedor.setEmailFornecedor(request.getEmailFornecedor());
		return fornecedor;
	}

	//Método que converte o DTO response em uma entidade 
	private FornecedorResponse toResponse(Fornecedor fornecedor) {
		FornecedorResponse response = new FornecedorResponse();
		response.setIdFornecedor(fornecedor.getIdFornecedor());
		response.setNomeFornecedor(fornecedor.getNomeFornecedor());
		response.setCnpj(fornecedor.getCnpj());
		response.setTelefoneFornecedor(fornecedor.getTelefoneFornecedor());
		response.setEmailFornecedor(fornecedor.getEmailFornecedor());
		return response;
	}

	private Fornecedor findEntityById(Long id) {
		return fornecedorRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com ID: " + id));
	}
}
