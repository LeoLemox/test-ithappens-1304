package br.com.ithappens.ithappensbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "it09_pedido")
@SequenceGenerator(
        name = "it09_pedido_seq",
        sequenceName = "it09_pedido_seq",
        allocationSize = 1
)
public class Pedido implements Serializable {

    private static final long serialVersionUID = 3883319727879211472L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it09_pedido_seq"
    )
    @Column(name = "it09_cod_pedido")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fkit09it02_cod_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "fkit09it03_cod_filial")
    private Filial filial;

    @ManyToOne
    @JoinColumn(name = "fkit09it05_cod_forma_pagamento")
    private FormaPagamento formaPagamento;

    @ManyToOne
    @JoinColumn(name = "fkit09it06_cod_tipo_pedido")
    private TipoPedido tipoPedido;

    @ManyToOne
    @JoinColumn(name = "fkit09it01_cod_usuario")
    private Usuario usuario;
}
