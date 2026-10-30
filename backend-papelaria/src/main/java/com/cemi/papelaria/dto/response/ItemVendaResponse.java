package com.cemi.papelaria.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ItemVendaResponse {

    private Long idItemVenda;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private Long idProduto;
    private String nomeProduto;
    private Long idVenda;
}
