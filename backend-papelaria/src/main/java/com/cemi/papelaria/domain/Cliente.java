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
@Table(name="usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class Cliente implements Serializable{

	@Id
	@Column(name="id_cliente", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aux_cliente")
	@SequenceGenerator(allocationSize = 1, name = "seq_aux_cliente", sequenceName = "seq_aux_cliente")
	private Long idCliente;

	@Column(name="nome_cliente", nullable = false, length = 100)
	private String nome;

	@Column(name="cpf_cliente", nullable = false)
	private String cpf;

	@Column(name="telefone_cliente")
	private String telefone;

	@Column(name="email_cliente")
	private String email;


}
