package com.cemi.papelaria.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="fornecedor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class Fornecedor implements Serializable{

	@Id
	@Column(name="id_fornecedor", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aux_fornecedor")
	@SequenceGenerator(allocationSize = 1, name = "seq_aux_fornecedor", sequenceName = "seq_aux_fornecedor")
	private Long idFornecedor;

	@Column(name="nome_fornecedor", nullable = false, length = 100)
	private String nomeFornecedor;

	@Column(name="cnpj", nullable = false, unique = true, length = 14)
	private String cnpj;

	@Column(name="telefone_fornecedor", length = 20)
	private String telefoneFornecedor;

	@Column(name="email_fornecedor", length = 100)
	private String emailFornecedor;

}
