package com.cemi.papelaria.dto.response;

import lombok.Data;

@Data
public class FornecedorResponse {

	private Long idFornecedor;
	private String nomeFornecedor;
	private String cnpj;
	private String telefoneFornecedor;
	private String emailFornecedor;

}
