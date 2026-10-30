package com.cemi.papelaria.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProdutoResponse {

	private Long idProduto;
	private String nomeProduto;
	private String descricao;
	private Long idCategoriaProduto;
	private String nomeCategoria;

	private BigDecimal preco;
	private Integer estoque;
	private java.time.LocalDate dataValidade;
}
