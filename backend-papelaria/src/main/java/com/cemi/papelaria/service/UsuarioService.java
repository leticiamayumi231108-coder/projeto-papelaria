package com.cemi.papelaria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cemi.papelaria.domain.Usuario;
import com.cemi.papelaria.domain.enums.AcessoUsuario;
import com.cemi.papelaria.dto.request.LoginRequest;
import com.cemi.papelaria.dto.request.UsuarioRequest;
import com.cemi.papelaria.dto.response.UsuarioResponse;
import com.cemi.papelaria.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public UsuarioResponse login(LoginRequest request) {
		Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos!"));

		if (!request.getSenha().equals(usuario.getSenha())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos!");
		}

		return toResponse(usuario);
	}

	public UsuarioResponse adicionar(UsuarioRequest request) {
		Usuario usuario = toEntity(request);
		usuario = usuarioRepository.save(usuario);
		return toResponse(usuario);
	}

	public UsuarioResponse buscarPorId(Long id) {
		Usuario usuario = findEntityById(id);
		return toResponse(usuario);
	}

	public List<UsuarioResponse> buscarTodos() {
		return usuarioRepository.findAll().stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	public UsuarioResponse alterar(Long id, UsuarioRequest request) {
		Usuario usuario = findEntityById(id);
		atualizarDados(usuario, request);
		usuario = usuarioRepository.save(usuario);
		return toResponse(usuario);
	}

	public void excluir(Long id) {
		findEntityById(id);
		usuarioRepository.deleteById(id);
	}

	private void atualizarDados(Usuario usuario, UsuarioRequest request) {
		usuario.setNome(request.getNome());
		usuario.setEmail(request.getEmail());
		if (request.getSenha() != null && !request.getSenha().isBlank()) {
			usuario.setSenha(request.getSenha());
		}

		if (request.getAcesso() != null && !request.getAcesso().isBlank()) {
			usuario.setAcesso(AcessoUsuario.valueOf(request.getAcesso()));
		}
	}

	//Método que converte o DTO request em uma entidade 
	private Usuario toEntity(UsuarioRequest request) {
		Usuario usuario = new Usuario();
		usuario.setNome(request.getNome());
		usuario.setEmail(request.getEmail());
		usuario.setSenha(request.getSenha());

		usuario.setAcesso(AcessoUsuario.valueOf(request.getAcesso()));
		return usuario;
	}

	//Método que converte o DTO response em uma entidade 
	private UsuarioResponse toResponse(Usuario usuario) {
		UsuarioResponse response = new UsuarioResponse();
		response.setIdUsuario(usuario.getIdUsuario());
		response.setNome(usuario.getNome());
		response.setEmail(usuario.getEmail());
		response.setAcesso(usuario.getAcesso().name());

		return response;
	}

	private Usuario findEntityById(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado! Id: " + id));
	}
}
