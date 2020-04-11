package br.com.ithappens.ithappensbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "it02_cliente")
@SequenceGenerator(
        name = "it02_cliente_seq",
        sequenceName = "it02_cliente_seq",
        allocationSize = 1
)
public class Cliente implements Serializable {

    private static final long serialVersionUID = -3998264817707137892L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it02_cliente_seq"
    )
    @Column(name = "it02_cod_cliente")
    private Long id;

    @NotBlank
    @Column(name = "it02_nome")
    private String nome;

    @NotBlank
    @Column(name = "it02_cpf")
    private String cpf;
}
