package br.com.ithappens.ithappensbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
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

    @Column(name = "it01_nome")
    private String nome;

    @Column(name = "it01_email")
    private String email;

    @Column(name = "it01_senha")
    private String senha;
}
