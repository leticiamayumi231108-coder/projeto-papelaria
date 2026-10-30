package com.cemi.papelaria.dto.request;

import java.util.List;
import com.cemi.papelaria.domain.enums.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VendaRequest {

    @NotNull(message = "Cliente é obrigatório")
    private Long idCliente;

    @NotNull(message = "Forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;

    @NotEmpty(message = "A venda deve ter pelo menos um item")
    @Valid
    private List<ItemVendaRequest> itens;
}
