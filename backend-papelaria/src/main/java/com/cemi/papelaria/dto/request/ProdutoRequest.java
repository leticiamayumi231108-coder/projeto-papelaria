package com.cemi.papelaria.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProdutoRequest {

	@NotNull(message="Nome é obrigatório")
	private String nomeProduto;

	private String descricao;

	@NotNull(message="Categoria é obrigatória")
	private Long idCategoriaProduto;

	@NotNull(message="Preço é obrigatório")

	private BigDecimal preco;

	@NotNull(message="Estoque é obrigatório")

	@PositiveOrZero(message="Estoque deve ser zero ou positivo")
	private Integer estoque;

	private LocalDate dataValidade;
}
