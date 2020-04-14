package br.com.ithappens.ithappensbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ItemPedidoDto {

    private Long id;
    private Long pedidoId;
    private String produto;
    private String status;
    private BigDecimal valorUnitario;
    private Integer quantidade;
    private BigDecimal valorTotal;
}
