package com.cemi.papelaria.dto.response;

import lombok.Data;

@Data
public class UsuarioResponse {

	private Long idUsuario;
	private String nome;
	private String email;
	private String acesso;

}
