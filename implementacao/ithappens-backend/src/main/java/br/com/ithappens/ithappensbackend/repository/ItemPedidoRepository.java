package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.ItemPedido;
import br.com.ithappens.ithappensbackend.model.dto.ItemPedidoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    @Query("select ip from ItemPedido ip " +
            "join ip.pedido p " +
            "where p.id = :pedidoId")
    List<ItemPedido> findByPedido(Long pedidoId);

    @Query("select new br.com.ithappens.ithappensbackend.model.dto.ItemPedidoDto" +
            "(ip.id, pe.id, pr.descricao, s.descricao, pr.valor, ip.quantidade, (ip.quantidade * ip.valorUnitario)) " +
            "from ItemPedido ip " +
            "join ip.pedido pe " +
            "join ip.produto pr " +
            "join ip.statusItem s " +
            "where ip.id = :id")
    Optional<ItemPedidoDto> findDtoById(Long id);

    @Query("select new br.com.ithappens.ithappensbackend.model.dto.ItemPedidoDto" +
            "(ip.id, pe.id, pr.descricao, s.descricao, pr.valor, ip.quantidade, (ip.quantidade * ip.valorUnitario)) " +
            "from ItemPedido ip " +
            "join ip.pedido pe " +
            "join ip.produto pr " +
            "join ip.statusItem s")
    List<ItemPedidoDto> findAllDto();

    @Query("select new br.com.ithappens.ithappensbackend.model.dto.ItemPedidoDto" +
            "(ip.id, pe.id, pr.descricao, s.descricao, pr.valor, ip.quantidade, (ip.quantidade * ip.valorUnitario)) " +
            "from ItemPedido ip " +
            "join ip.pedido pe " +
            "join ip.produto pr " +
            "join ip.statusItem s " +
            "where pe.id = :pedidoId")
    List<ItemPedidoDto> findDtoByPedido(Long pedidoId);
}
