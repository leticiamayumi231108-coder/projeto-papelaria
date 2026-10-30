package com.cemi.papelaria.dto.response;

import lombok.Data;

@Data
public class ClienteResponse {

	private Long idCliente;
	private String nome;
	private String cpf;
	private String telefone;
	private String email;
}
