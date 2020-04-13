package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("select p from Produto p where trim(p.codigoBarras) = trim(:codigoBarras)")
    Optional<Produto> findByCodigoBarras(String codigoBarras);

    @Query("select p from Produto p where (:id is null or p.id = :id) " +
            "and (:descricao is null or trim(lower(p.descricao)) like concat('%', trim(lower(:descricao)), '%')) " +
            "and (:codigoBarra is null or trim(lower(p.codigoBarras)) like concat('%', trim(lower(:codigoBarra)), '%'))")
    Page<Produto> search(Long id, String descricao, String codigoBarra, Pageable pageable);
}
