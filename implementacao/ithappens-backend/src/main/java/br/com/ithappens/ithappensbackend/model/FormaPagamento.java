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
@Table(name = "it05_forma_pagamento")
@SequenceGenerator(
        name = "it05_forma_pagamento_seq",
        sequenceName = "it05_forma_pagamento_seq",
        allocationSize = 1
)
public class FormaPagamento implements Serializable {

    private static final long serialVersionUID = -526833275231342781L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it05_forma_pagamento_seq"
    )
    @Column(name = "it05_cod_forma_pagamento")
    private Long id;

    @NotBlank
    @Column(name = "it05_descricao")
    private String descricao;
}
