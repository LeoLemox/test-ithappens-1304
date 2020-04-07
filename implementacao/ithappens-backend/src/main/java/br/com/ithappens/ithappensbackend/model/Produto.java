package br.com.ithappens.ithappensbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "it04_produto")
@SequenceGenerator(
        name = "it04_produto_seq",
        sequenceName = "it04_produto_seq",
        allocationSize = 1
)
public class Produto implements Serializable {

    private static final long serialVersionUID = 7046192177437530960L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it04_produto_seq"
    )
    @Column(name = "it04_cod_produto")
    private Long id;

    @Column(name = "it04_descricao")
    private String descricao;

    @Column(name = "it04_codigo_barras")
    private String codigoBarras;

    @Column(name = "it04_valor")
    private BigDecimal valor;
}
