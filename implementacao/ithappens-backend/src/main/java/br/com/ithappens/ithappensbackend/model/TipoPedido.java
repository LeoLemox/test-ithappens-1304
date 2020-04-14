package br.com.ithappens.ithappensbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "it06_tipo_pedido")
@SequenceGenerator(
        name = "it06_tipo_pedido_seq",
        sequenceName = "it06_tipo_pedido_seq",
        allocationSize = 1
)
public class TipoPedido implements Serializable {

    private static final long serialVersionUID = -1116474893524238275L;
    public static long ENTRADA = 1L;
    public static long SAIDA = 2L;

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
