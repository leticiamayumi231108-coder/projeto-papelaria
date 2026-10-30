package com.cemi.papelaria.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cemi.papelaria.domain.Cliente;
import com.cemi.papelaria.domain.ItemVenda;
import com.cemi.papelaria.domain.Produto;
import com.cemi.papelaria.domain.Venda;
import com.cemi.papelaria.dto.request.ItemVendaRequest;
import com.cemi.papelaria.dto.request.VendaRequest;
import com.cemi.papelaria.dto.response.ItemVendaResponse;
import com.cemi.papelaria.dto.response.VendaResponse;
import com.cemi.papelaria.repository.ClienteRepository;
import com.cemi.papelaria.repository.ProdutoRepository;
import com.cemi.papelaria.repository.VendaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service // Indica que esta classe é um serviço gerenciado pelo Spring
public class VendaService {

	@Autowired // Injeta automaticamente o repositório
	private VendaRepository vendaRepository; // Acesso ao banco (tabela venda)

	@Autowired // Injeta automaticamente o repositório
	private ClienteRepository clienteRepository; // Acesso ao banco (tabela cliente)

	@Autowired // Injeta automaticamente o repositório
	private ProdutoRepository produtoRepository; // Acesso ao banco (tabela produto)

	/**
	 * Adiciona uma nova venda e atualiza o estoque dos produtos.
	 */
	@Transactional // Agrupa todas as operações numa única transação (tudo ou nada)
	public VendaResponse adicionar(VendaRequest request) {
		Venda venda = toEntity(request); // Converte o request em uma entidade Venda

		BigDecimal total = BigDecimal.ZERO; // Inicia o total da venda em zero

		for (ItemVenda item : venda.getItens()) { // Percorre cada item da venda
			Produto produto = item.getProduto(); // Obtém o produto do item

			if (produto.getEstoque() < item.getQuantidade()) { // Se não tem estoque suficiente
				throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNomeProduto() +
						". Disponível: " + produto.getEstoque() + ", Solicitado: " + item.getQuantidade()); // Lança erro informando o problema
			}

			produto.setEstoque(produto.getEstoque() - item.getQuantidade()); // Diminui a quantidade do estoque
			produtoRepository.save(produto); // Salva o produto com o estoque atualizado

			total = total.add(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()))); // Soma o valor do item ao total
		}

		venda.setValorTotal(total); // Define o total calculado na venda
		venda = vendaRepository.saveAndFlush(venda); // Salva a venda e seus itens no banco
		return toResponse(venda); // Converte a venda em DTO de resposta e retorna
	}

	/**
	 * Busca uma venda pelo ID.
	 */
	@Transactional(readOnly = true) // Transação somente leitura para carregar os itens
	public VendaResponse buscarPorId(Long id) {
		Venda venda = findEntityById(id); // Busca a venda pelo ID
		return toResponse(venda); // Converte em DTO de resposta e retorna
	}

	/**
	 * Busca todas as vendas.
	 */
	@Transactional(readOnly = true) // Transação somente leitura
	public List<VendaResponse> buscarTodos() {
		return vendaRepository.findAll().stream() // Busca todas as vendas e vira um stream
				.map(this::toResponse) // Converte cada venda em DTO de resposta
				.collect(Collectors.toList()); // Junta tudo numa lista
	}

	/**
	 * Altera os dados de uma venda existente.
	 */
	@Transactional // Agrupa todas as operações numa única transação
	public VendaResponse alterar(Long id, VendaRequest request) {
		Venda objAntigo = findEntityById(id); // Busca a venda existente pelo ID

		for (ItemVenda itemAntigo : objAntigo.getItens()) { // Percorre os itens antigos
			Produto produto = itemAntigo.getProduto(); // Obtém o produto do item antigo
			produto.setEstoque(produto.getEstoque() + itemAntigo.getQuantidade()); // Devolve a quantidade ao estoque
			produtoRepository.save(produto); // Salva o produto com o estoque restaurado
		}

		Cliente cliente = clienteRepository.findById(request.getIdCliente()) // Busca o cliente pelo ID do request
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + request.getIdCliente())); // Lança erro se não achar
		objAntigo.setCliente(cliente); // Define o novo cliente na venda
		objAntigo.setFormaPagamento(request.getFormaPagamento()); // Define a nova forma de pagamento

		objAntigo.getItens().clear(); // Remove os itens antigos da lista
		BigDecimal total = BigDecimal.ZERO; // Inicia o novo total em zero

		for (ItemVendaRequest itemReq : request.getItens()) { // Percorre os novos itens do request
			ItemVenda item = toItemEntity(itemReq, objAntigo); // Converte o request em ItemVenda

			Produto produto = item.getProduto(); // Obtém o produto do novo item
			if (produto.getEstoque() < item.getQuantidade()) { // Se não tem estoque suficiente
				throw new IllegalArgumentException("Estoque insuficiente para o produto: " + produto.getNomeProduto() +
						". Disponível: " + produto.getEstoque() + ", Solicitado: " + item.getQuantidade()); // Lança erro informando o problema
			}
			produto.setEstoque(produto.getEstoque() - item.getQuantidade()); // Diminui o estoque do produto
			produtoRepository.save(produto); // Salva o produto atualizado

			objAntigo.getItens().add(item); // Adiciona o novo item à venda
			total = total.add(item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()))); // Soma o valor do item ao total
		}

		objAntigo.setValorTotal(total); // Define o novo total na venda

		objAntigo = vendaRepository.save(objAntigo); // Salva a venda alterada no banco
		return toResponse(objAntigo); // Converte em DTO de resposta e retorna
	}

	/**
	 * Exclui uma venda pelo ID e restaura o estoque dos produtos.
	 */
	@Transactional // Agrupa todas as operações numa única transação
	public void excluir(Long id) {
		Venda venda = findEntityById(id); // Busca a venda pelo ID

		for (ItemVenda item : venda.getItens()) { // Percorre os itens da venda
			Produto produto = item.getProduto(); // Obtém o produto do item
			produto.setEstoque(produto.getEstoque() + item.getQuantidade()); // Devolve a quantidade ao estoque
			produtoRepository.save(produto); // Salva o produto com o estoque restaurado
		}

		vendaRepository.deleteById(id); // Deleta a venda do banco
	}

	private Venda toEntity(VendaRequest request) {
		Venda venda = new Venda(); // Cria uma nova entidade Venda

		Cliente cliente = clienteRepository.findById(request.getIdCliente()) // Busca o cliente pelo ID do request
				.orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + request.getIdCliente())); // Lança erro se não achar
		venda.setCliente(cliente); // Define o cliente na venda
		venda.setFormaPagamento(request.getFormaPagamento()); // Define a forma de pagamento

		List<ItemVenda> itens = new ArrayList<>(); // Cria uma lista de itens
		for (ItemVendaRequest itemReq : request.getItens()) { // Percorre os itens do request
			itens.add(toItemEntity(itemReq, venda)); // Converte cada item e adiciona à lista
		}
		venda.setItens(itens); // Define a lista de itens na venda

		return venda; // Retorna a venda montada
	}

	private ItemVenda toItemEntity(ItemVendaRequest request, Venda venda) {
		Produto produto = produtoRepository.findById(request.getIdProduto()) // Busca o produto pelo ID do request
				.orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com ID: " + request.getIdProduto())); // Lança erro se não achar

		ItemVenda item = new ItemVenda(); // Cria um novo ItemVenda
		item.setProduto(produto); // Define o produto no item
		item.setQuantidade(request.getQuantidade()); // Define a quantidade do item
		item.setPrecoUnitario(produto.getPreco()); // Usa o preço atual do produto
		item.setVenda(venda); // Liga o item à venda

		return item; // Retorna o item montado
	}

	private VendaResponse toResponse(Venda venda) {
		VendaResponse response = new VendaResponse(); // Cria um DTO de resposta
		response.setIdVenda(venda.getIdVenda()); // Copia o ID da venda
		response.setDataVenda(venda.getDataVenda()); // Copia a data da venda
		response.setValorTotal(venda.getValorTotal()); // Copia o valor total
		response.setFormaPagamento(venda.getFormaPagamento()); // Copia a forma de pagamento
		response.setIdCliente(venda.getCliente().getIdCliente()); // Copia o ID do cliente
		response.setNomeCliente(venda.getCliente().getNome()); // Copia o nome do cliente

		if (venda.getItens() != null) { // Se a venda tem itens
			response.setItens(venda.getItens().stream() // Copia os itens
					.map(this::toItemResponse) // Converte cada item em DTO de resposta
					.collect(Collectors.toList())); // Junta numa lista
		} else {
			response.setItens(new ArrayList<>()); // Se não tem, usa uma lista vazia
		}

		return response; // Retorna o DTO de resposta
	}

	private ItemVendaResponse toItemResponse(ItemVenda item) {
		ItemVendaResponse response = new ItemVendaResponse(); // Cria um DTO de resposta
		response.setIdItemVenda(item.getIdItemVenda()); // Copia o ID do item
		response.setQuantidade(item.getQuantidade()); // Copia a quantidade
		response.setPrecoUnitario(item.getPrecoUnitario()); // Copia o preço unitário
		if (item.getProduto() != null) { // Se o item tem produto
			response.setIdProduto(item.getProduto().getIdProduto()); // Copia o ID do produto
			response.setNomeProduto(item.getProduto().getNomeProduto()); // Copia o nome do produto
		}
		if (item.getVenda() != null) { // Se o item tem venda
			response.setIdVenda(item.getVenda().getIdVenda()); // Copia o ID da venda
		}
		return response; // Retorna o DTO de resposta
	}

	private Venda findEntityById(Long id) {
		return vendaRepository.findById(id) // Busca a venda pelo ID
				.orElseThrow(() -> new EntityNotFoundException("Venda não encontrada! Id: " + id)); // Lança erro se não achar
	}
}
