package br.com.ithappens.ithappensbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @NotBlank
    @Column(name = "it04_descricao")
    private String descricao;

    @NotBlank
    @Pattern(regexp = "^[0-9]+$", message = "O Código de barras deve conter apenas dígitos")
    @Size(min = 3, max = 13, message = "O Código de barras deve possuir de 3 a 13 dígitos")
    @Column(name = "it04_codigo_barras")
    private String codigoBarras;

    @NotNull
    @PositiveOrZero
    @Digits(integer = 6, fraction = 2)
    @Column(name = "it04_valor")
    private BigDecimal valor;
}
