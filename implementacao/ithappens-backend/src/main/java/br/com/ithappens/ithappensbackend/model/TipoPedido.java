package br.com.ithappens.ithappensbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "it06_tipo_pedido")
@SequenceGenerator(
        name = "it06_tipo_pedido_seq",
        sequenceName = "it06_tipo_pedido_seq",
        allocationSize = 1
)
public class TipoPedido implements Serializable {

    private static final long serialVersionUID = -1116474893524238275L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it06_tipo_pedido_seq"
    )
    @Column(name = "it06_cod_tipo_pedido")
    private Long id;

    @Column(name = "it06_descricao")
    private String descricao;
}
