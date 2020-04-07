package br.com.ithappens.ithappensbackend.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "it03_filial")
@SequenceGenerator(
        name = "it03_filial_seq",
        sequenceName = "it03_filial_seq",
        allocationSize = 1
)
public class Filial implements Serializable {

    private static final long serialVersionUID = -734504082890114032L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it03_filial_seq"
    )
    @Column(name = "it03_cod_filial")
    private Long id;

    @Column(name = "it03_nome")
    private String nome;
}
