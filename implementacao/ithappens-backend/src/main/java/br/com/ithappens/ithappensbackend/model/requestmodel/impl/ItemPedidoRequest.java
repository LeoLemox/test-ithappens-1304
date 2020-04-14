package br.com.ithappens.ithappensbackend.model.requestmodel.impl;

import br.com.ithappens.ithappensbackend.model.ItemPedido;
import br.com.ithappens.ithappensbackend.model.Pedido;
import br.com.ithappens.ithappensbackend.model.Produto;
import br.com.ithappens.ithappensbackend.model.StatusItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoRequest {

    @NotNull(message = "Pedido é obrigatório")
    private Long pedidoId;

    @NotNull(message = "Produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade de ser maior que zero")
    private Integer quantidade;

    public ItemPedido gerarItem() {
        return ItemPedido.builder()
                .quantidade(quantidade)
                .pedido(Pedido.builder().id(pedidoId).build())
                .produto(Produto.builder().id(produtoId).build())
                .statusItem(StatusItem.builder().id(StatusItem.ATIVO).build())
                .build();
    }
}
