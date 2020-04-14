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
@Table(name = "it07_status_item")
@SequenceGenerator(
        name = "it07_status_item_seq",
        sequenceName = "it07_status_item_seq",
        allocationSize = 1
)
public class StatusItem implements Serializable {

    private static final long serialVersionUID = -4992101709763981935L;
    public static long ATIVO = 1L;
    public static long CANCELADO = 2L;
    public static long PROCESSADO = 3L;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "it07_status_item_seq"
    )
    @Column(name = "it07_cod_status_item")
    private Long id;

    @Column(name = "it07_descricao")
    private String descricao;
}
