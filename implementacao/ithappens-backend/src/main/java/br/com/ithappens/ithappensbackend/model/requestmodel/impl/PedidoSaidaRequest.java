package br.com.ithappens.ithappensbackend.model.requestmodel.impl;

import br.com.ithappens.ithappensbackend.model.*;
import br.com.ithappens.ithappensbackend.model.requestmodel.PedidoRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoSaidaRequest implements PedidoRequest {

    @NotNull(message = "Cliente é obrigatório")
    Long clienteId;

    @NotNull(message = "Filial é obrigatória")
    Long filialId;

    @NotNull(message = "Usuário é obrigatório")
    Long usuarioId;

    @NotBlank(message = "Observação é obrigatório")
    String observacao;

    public Pedido gerarPedido() {
        return Pedido.builder()
                .cliente(Cliente.builder().id(clienteId).build())
                .filial(Filial.builder().id(filialId).build())
                .usuario(Usuario.builder().id(usuarioId).build())
                .observacao(observacao)
                .tipoPedido(TipoPedido.builder().id(TipoPedido.SAIDA).build())
                .build();
    }
}
