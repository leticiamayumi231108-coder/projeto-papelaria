package com.cemi.papelaria.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cemi.papelaria.domain.ItemVenda;
import com.cemi.papelaria.domain.Produto;
import com.cemi.papelaria.domain.Venda;
import com.cemi.papelaria.dto.response.ItemVendaResponse;
import com.cemi.papelaria.repository.ItemVendaRepository;
import com.cemi.papelaria.repository.ProdutoRepository;
import com.cemi.papelaria.repository.VendaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ItemVendaService {

	@Autowired
	private ItemVendaRepository itemVendaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private VendaRepository vendaRepository;

	@Transactional(readOnly = true)
	public ItemVendaResponse buscarPorId(Long id) {
		ItemVenda item = findEntityById(id);
		return toResponse(item);
	}

	@Transactional(readOnly = true)
	public List<ItemVendaResponse> buscarTodos() {
		return itemVendaRepository.findAll().stream()
				.map(this::toResponse)
				.collect(Collectors.toList());
	}

	@Transactional
	public void excluir(Long id) {
		ItemVenda item = findEntityById(id);

		Produto produto = item.getProduto();
		if (produto != null) {
			produto.setEstoque(produto.getEstoque() + item.getQuantidade());
			produtoRepository.save(produto);
		}

		Venda venda = item.getVenda();
		if (venda != null && venda.getItens() != null) {
			BigDecimal total = venda.getItens().stream()
					.filter(i -> !i.getIdItemVenda().equals(id))
					.map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			venda.setValorTotal(total);
			vendaRepository.save(venda);
		}

		itemVendaRepository.deleteById(id);
	}

	private ItemVendaResponse toResponse(ItemVenda item) {
		ItemVendaResponse response = new ItemVendaResponse();
		response.setIdItemVenda(item.getIdItemVenda());
		response.setQuantidade(item.getQuantidade());
		response.setPrecoUnitario(item.getPrecoUnitario());

		if (item.getProduto() != null) {
			response.setIdProduto(item.getProduto().getIdProduto());
			response.setNomeProduto(item.getProduto().getNomeProduto());
		}
		if (item.getVenda() != null) {
			response.setIdVenda(item.getVenda().getIdVenda());
		}
		return response;
	}

	private ItemVenda findEntityById(Long id) {
		return itemVendaRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Item de venda não encontrado! Id: " + id));
	}
}
