package com.cemi.papelaria.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.cemi.papelaria.domain.enums.FormaPagamento;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="venda")
@SuppressWarnings("serial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Venda implements Serializable{

	@Id
	@Column(name="id_venda", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aux_venda")
	@SequenceGenerator(allocationSize = 1, name = "seq_aux_venda", sequenceName = "seq_aux_venda")
	private Long idVenda;

	@CreationTimestamp
	@Column(name="data_venda", updatable = false)
	private LocalDate dataVenda;

	@Column(name="valor_total", nullable = false)
	private BigDecimal valorTotal;

	@Column(name="forma_pagamento", nullable = false)
	@Enumerated(EnumType.STRING)
	private FormaPagamento formaPagamento;
	
	//Relacionamento entre Venda e Cliente
    //Cardinalidade 1 -> N 
    //A tabela "venda" irá gravar id_cliente, a FK do relacionamento
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cliente", nullable = false)
	private Cliente cliente;

	//Relacionamento entre Venda e ItemVenda
    //Cardinalidade 1 -> N
    //A tabela "item_venda" irá gravar id_venda, a FK do relacionamento
	@OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenda> itens = new ArrayList<>();
}
