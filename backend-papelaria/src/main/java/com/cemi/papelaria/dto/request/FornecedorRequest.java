package com.cemi.papelaria.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FornecedorRequest {

	@NotBlank(message="Nome do fornecedor é obrigatório")
	@Size(min=3, max=100)
	private String nomeFornecedor;

	@NotBlank(message="CNPJ é obrigatório")
	@Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos")
	private String cnpj;

	@NotBlank(message="Telefone é obrigatório")
	private String telefoneFornecedor;

	@NotBlank(message="E-mail é obrigatório")
	@Email(message="E-mail inválido")
	private String emailFornecedor;

}
