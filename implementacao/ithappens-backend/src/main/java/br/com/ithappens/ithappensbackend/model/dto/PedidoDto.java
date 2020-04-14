package br.com.ithappens.ithappensbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PedidoDto {

    private Long id;
    private BigDecimal valorTotal;
    private Long qtdItens;
    private String cliente;
    private String filial;
    private String formaPagamento;
    private String tipoPedido;
    private String usuario;
    private String observacao;
}
