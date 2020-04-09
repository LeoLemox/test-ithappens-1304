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
@Table(name = "it01_usuario")
@SequenceGenerator(
        name = "it01_usuario_seq",
        sequenceName = "it01_usuario_seq",
        allocationSize = 1
)
public class Usuario implements Serializable {

    private static final long serialVersionUID = 4634845613145043472L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it01_usuario_seq"
    )
    @Column(name = "it01_cod_usuario")
    private Long id;

    @NotBlank
    @Column(name = "it01_nome")
    private String nome;

    @NotBlank
    @Column(name = "it01_email")
    private String email;

    @NotBlank
    @Column(name = "it01_senha")
    private String senha;
}
