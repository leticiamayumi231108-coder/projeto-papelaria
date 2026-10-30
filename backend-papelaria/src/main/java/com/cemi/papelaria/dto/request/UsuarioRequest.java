package com.cemi.papelaria.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequest {

	@NotBlank(message="Nome é obrigatório")
	@Size(min=3)

	@Pattern(
		    regexp = "[A-ZÀÁÂÃÉÊÍÓÔÕÚÇ][A-Za-zÀ-ú\\s]+",
		    message = "Nome deve começar com letra maiúscula e conter apenas letras"
		    )
	private String nome;

	@NotBlank(message="E-mail é obrigatório")
	@Email
	private String email;

	@NotBlank(message="Senha é obrigatória")
	private String senha;

	@NotBlank(message="Nível de acesso é obrigatório")
	private String acesso;
}
