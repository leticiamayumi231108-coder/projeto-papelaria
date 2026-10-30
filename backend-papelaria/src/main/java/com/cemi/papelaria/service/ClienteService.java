package com.cemi.papelaria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cemi.papelaria.domain.Cliente;
import com.cemi.papelaria.dto.request.ClienteRequest;
import com.cemi.papelaria.dto.response.ClienteResponse;
import com.cemi.papelaria.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public ClienteResponse adicionar(ClienteRequest request) {
		Cliente cliente = toEntity(request);
		cliente = clienteRepository.save(cliente);
		return toResponse(cliente);
	}

	public ClienteResponse buscarPorId(Long id) {
		Cliente cliente = findEntityById(id);
		return toResponse(cliente);
	}

	public List<ClienteResponse> buscarTodos() {
		return clienteRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
	}

	public ClienteResponse alterar(Long id, ClienteRequest request) {
		Cliente objAntigo = findEntityById(id);
		atualizarDados(objAntigo, request);
		objAntigo = clienteRepository.save(objAntigo);
		return toResponse(objAntigo);
	}

	public void excluir(Long id) {
		findEntityById(id);
		clienteRepository.deleteById(id);
	}

	private void atualizarDados(Cliente objAntigo, ClienteRequest request) {
		objAntigo.setNome(request.getNome());
		objAntigo.setCpf(request.getCpf());
		objAntigo.setTelefone(request.getTelefone());
		objAntigo.setEmail(request.getEmail());
	}

	//Método que converte o DTO request em uma entidade 
	private Cliente toEntity(ClienteRequest request) {

		Cliente cliente = new Cliente();
		cliente.setNome(request.getNome());
		cliente.setCpf(request.getCpf());
		cliente.setEmail(request.getEmail());
		cliente.setTelefone(request.getTelefone());

		return cliente;
	}

	//Método que converte o DTO response em uma entidade 
	private ClienteResponse toResponse(Cliente cliente) {

		ClienteResponse response = new ClienteResponse();
		response.setIdCliente(cliente.getIdCliente());
		response.setNome(cliente.getNome());
		response.setCpf(cliente.getCpf());
		response.setEmail(cliente.getEmail());
		response.setTelefone(cliente.getTelefone());

		return response;
	}

	private Cliente findEntityById(Long id) {
		return clienteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
	}

}
