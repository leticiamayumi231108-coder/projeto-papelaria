package com.cemi.papelaria.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.cemi.papelaria.domain.enums.FormaPagamento;
import lombok.Data;

@Data
public class VendaResponse {

    private Long idVenda;
    private LocalDate dataVenda;

    private BigDecimal valorTotal;
    private FormaPagamento formaPagamento;
    private Long idCliente;
    private String nomeCliente;
    private List<ItemVendaResponse> itens;
}
