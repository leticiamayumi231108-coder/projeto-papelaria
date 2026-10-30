package com.cemi.papelaria.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteRequest {

	@NotBlank(message="Nome é obrigatório")
	@Size(min=3)

	@Pattern(
		    regexp = "[A-ZÀÁÂÃÉÊÍÓÔÕÚÇ][A-Za-zÀ-ú\\s]+",
		    message = "Nome deve começar com letra maiúscula e conter apenas letras"
		    )
	private String nome;

	@NotBlank(message="CPF é obrigatório")
	@Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
	private String cpf;

	@NotBlank(message="Telefone é obrigatório")
	private String telefone;

	@NotBlank(message="E-mail é obrigatório")
	@Email
	private String email;

}
