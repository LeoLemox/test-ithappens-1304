package br.com.ithappens.ithappensbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "it10_item_pedido")
@SequenceGenerator(
        name = "it10_item_pedido_seq",
        sequenceName = "it10_item_pedido_seq",
        allocationSize = 1
)
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 5854095978805556043L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it10_item_pedido_seq"
    )
    @Column(name = "it10_cod_item_pedido")
    private Long id;

    @Column(name = "it10_valor_unitario")
    private BigDecimal valorUnitario;

    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    @Column(name = "it10_quantidade")
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "fkit10it09_cod_pedido")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "fkit10it04_cod_produto")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "fkit10it07_cod_status_item")
    private StatusItem statusItem;
}
