package com.cemi.papelaria.domain;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name="item_venda")
@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemVenda implements Serializable{

	@Id
	@Column(name="id_item_venda", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aux_item")
	@SequenceGenerator(allocationSize = 1, name = "seq_aux_item", sequenceName = "seq_aux_item")
	private Long idItemVenda;

	@Column(name = "quantidade", nullable = false)
	private Integer quantidade;

	@Column(name = "preco_unitario", nullable = false)
	private BigDecimal precoUnitario;
	
	//Relacionamento entre ItemVenda e Produto
    //Cardinalidade 1 -> N 
    //A tabela "item_venda" irá gravar id_produto, a FK do relacionamento
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_produto", nullable = false)
	private Produto produto;

	//Relacionamento entre ItemVenda e Venda
    //Cardinalidade 1 -> N 
    //A tabela "item_venda" irá gravar id_venda, a FK do relacionamento
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_venda", nullable = false)
	private Venda venda;

}
