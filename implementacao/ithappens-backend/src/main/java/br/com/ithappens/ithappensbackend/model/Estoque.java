package br.com.ithappens.ithappensbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "it08_estoque")
@SequenceGenerator(
        name = "it08_estoque_seq",
        sequenceName = "it08_estoque_seq",
        allocationSize = 1
)
public class Estoque implements Serializable {

    private static final long serialVersionUID = 3883319727879211472L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it08_estoque_seq"
    )
    @Column(name = "it08_cod_estoque")
    private Long id;

    @Column(name = "it08_quantidade")
    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "fkit08it03_cod_filial")
    private Filial filial;

    @ManyToOne
    @JoinColumn(name = "fkit08it04_cod_produto")
    private Produto produto;
}
