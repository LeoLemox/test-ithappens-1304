package br.com.ithappens.ithappensbackend.model.requestmodel.impl;

import br.com.ithappens.ithappensbackend.model.Filial;
import br.com.ithappens.ithappensbackend.model.Pedido;
import br.com.ithappens.ithappensbackend.model.TipoPedido;
import br.com.ithappens.ithappensbackend.model.Usuario;
import br.com.ithappens.ithappensbackend.model.requestmodel.PedidoRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEntradaRequest implements PedidoRequest {

    @NotNull(message = "Filial é obrigatória")
    Long filialId;

    @NotNull(message = "Usuário é obrigatório")
    Long usuarioId;

    @Override
    public Pedido gerarPedido() {
        return Pedido.builder()
                .filial(Filial.builder().id(filialId).build())
                .usuario(Usuario.builder().id(usuarioId).build())
                .tipoPedido(TipoPedido.builder().id(TipoPedido.ENTRADA).build())
                .build();
    }
}
