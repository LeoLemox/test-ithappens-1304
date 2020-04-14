package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    @Query("select e from Estoque e " +
            "join e.filial f " +
            "where f.id = :filialId")
    List<Estoque> findByFilial(Long filialId);

    @Query("select e from Estoque e " +
            "join e.filial f " +
            "join e.produto p " +
            "where f.id = :filialId " +
            "and p.id = :pedidoId")
    Optional<Estoque> findByFilialAndProduto(Long filialId, Long pedidoId);
}
