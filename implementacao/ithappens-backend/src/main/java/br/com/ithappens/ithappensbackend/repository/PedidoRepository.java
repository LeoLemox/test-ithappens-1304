package br.com.ithappens.ithappensbackend.repository;

import br.com.ithappens.ithappensbackend.model.Pedido;
import br.com.ithappens.ithappensbackend.model.dto.PedidoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("select new br.com.ithappens.ithappensbackend.model.dto.PedidoDto" +
            "(p.id, sum(ip.quantidade * ip.valorUnitario), sum(ip.quantidade), c.nome, f.nome, fp.descricao, " +
            "tp.descricao, u.nome, p.observacao) " +
            "from Pedido p " +
            "join p.filial f " +
            "join p.tipoPedido tp " +
            "join p.usuario u " +
            "left join p.cliente c " +
            "left join p.formaPagamento fp " +
            "left join ItemPedido ip " +
            "on (ip.pedido.id = p.id and ip.statusItem.id <> 2) " +
            "group by p.id, c.nome, f.nome, fp.descricao, tp.descricao, u.nome, p.observacao")
    List<PedidoDto> findAllDto();

    @Query("select new br.com.ithappens.ithappensbackend.model.dto.PedidoDto" +
            "(p.id, sum(ip.quantidade * ip.valorUnitario), sum(ip.quantidade), c.nome, f.nome, fp.descricao, " +
            "tp.descricao, u.nome, p.observacao) " +
            "from Pedido p " +
            "join p.filial f " +
            "join p.tipoPedido tp " +
            "join p.usuario u " +
            "left join p.cliente c " +
            "left join p.formaPagamento fp " +
            "left join ItemPedido ip " +
            "on (ip.pedido.id = p.id and ip.statusItem.id <> 2) " +
            "where p.id = :id " +
            "group by p.id, c.nome, f.nome, fp.descricao, tp.descricao, u.nome, p.observacao")
    Optional<PedidoDto> findDtoById(Long id);

    @Query("select new br.com.ithappens.ithappensbackend.model.dto.PedidoDto" +
            "(p.id, sum(ip.quantidade * ip.valorUnitario), sum(ip.quantidade), c.nome, f.nome, fp.descricao, " +
            "tp.descricao, u.nome, p.observacao) " +
            "from Pedido p " +
            "join p.filial f " +
            "join p.tipoPedido tp " +
            "join p.usuario u " +
            "left join p.cliente c " +
            "left join p.formaPagamento fp " +
            "left join ItemPedido ip " +
            "on (ip.pedido.id = p.id and ip.statusItem.id <> 2) " +
            "where (:tipoPedido is null or tp.id = :tipoPedido) " +
            "and (:formaPagamento is null or fp.id = :formaPagamento) " +
            "and (:cliente is null or trim(lower(c.nome)) like :cliente) " +
            "and (:filial is null or trim(lower(f.nome)) like :filial) " +
            "and (:usuario is null or trim(lower(u.nome)) like :usuario) " +
            "group by p.id, c.nome, f.nome, fp.descricao, tp.descricao, u.nome, p.observacao")
    Page<PedidoDto> search(Long tipoPedido, Long formaPagamento, String cliente, String filial, String usuario, Pageable pageable);
}
