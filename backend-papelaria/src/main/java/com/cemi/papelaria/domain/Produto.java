package com.cemi.papelaria.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="produto")
@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produto implements Serializable{

	@Id
	@Column(name="id_produto", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aux_produto")
	@SequenceGenerator(allocationSize = 1, name = "seq_aux_produto", sequenceName = "seq_aux_produto")
	private Long idProduto;

	@Column(name="nome_produto", nullable = false)
	private String nomeProduto;

	@Column(name="descricao_produto", nullable = false)
	private String descricao;
	
	//Relacionamento entre CategoriaProduto e Produto
    //Cardinalidade 1 -> N 
    //A tabela "produto" irá gravar id_categoria_produto, a FK do relacionamento
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_categoria_produto", nullable = false)
	private CategoriaProduto categoria;

	@Column(name="preco_produto", nullable = false)
	private BigDecimal preco;

	@Column(name="estoque_produto", nullable = false)
	private Integer estoque;

	@Column(name="data_validade")
	private LocalDate dataValidade;
	
}
