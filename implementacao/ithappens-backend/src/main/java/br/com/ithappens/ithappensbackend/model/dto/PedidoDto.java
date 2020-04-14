package br.com.ithappens.ithappensbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PedidoDto {

    private Long id;
    private String cliente;
    private String filial;
    private String formaPagamento;
    private String tipoPedido;
    private String usuario;
    private String observacao;
}
