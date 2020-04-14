package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.*;
import br.com.ithappens.ithappensbackend.model.dto.PedidoDto;
import br.com.ithappens.ithappensbackend.repository.EstoqueRepository;
import br.com.ithappens.ithappensbackend.repository.FormaPagamentoRepository;
import br.com.ithappens.ithappensbackend.repository.ItemPedidoRepository;
import br.com.ithappens.ithappensbackend.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.*;

@Service
@AllArgsConstructor
public class PedidoService {

    private EstoqueRepository estoqueRepository;
    private FormaPagamentoRepository formaPagamentoRepository;
    private ItemPedidoRepository itemPedidoRepository;
    private PedidoRepository pedidoRepository;

    @Transactional
    public Pedido finalizarPedido(Long id, Long formaPagamentoId) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    if (pedido.getTipoPedido().getId().equals(TipoPedido.SAIDA)) {
                        Optional.ofNullable(formaPagamentoId)
                                .map(fpId -> formaPagamentoRepository.findById(fpId)
                                        .orElseThrow(() -> new ServiceException("Forma de pagamento inexistente")))
                                .orElseThrow(() -> new ServiceException("É necessário informar a forma de pagamento"));
                    }

                    processarPedido(pedido);

                    return pedido;
                })
                .orElseThrow(() -> new ServiceException("Pedido inexistente"));
    }

    public Page<PedidoDto> buscaAvancada(Long tipoPedido, Long formaPagamento, String cliente, String filial,
                                         String usuario, int page, int size) {
        return pedidoRepository.search(tipoPedido, formaPagamento, format(cliente), format(filial), format(usuario),
                PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }

    private void processarPedido(Pedido pedido) {
        List<ItemPedido> itens = itemPedidoRepository.findAtivosByPedido(pedido.getId());

        if (itens.isEmpty()) throw new ServiceException("O pedido não possui itens a serem processados");

        if (pedido.getTipoPedido().getId().equals(TipoPedido.ENTRADA)) {
            itens.forEach(itemPedido -> {
                salvarEntradaEstoque(pedido.getFilial(), itemPedido.getProduto(), itemPedido.getQuantidade());
                processarItem(itemPedido);
            });
        } else {
            itens.forEach(itemPedido -> {
                salvarSaidaEstoque(pedido.getFilial(), itemPedido.getProduto(), itemPedido.getQuantidade());
                processarItem(itemPedido);
            });
        }
    }

    private Estoque salvarEntradaEstoque(Filial filial, Produto produto, Integer quantidade) {
        Optional<Estoque> estoqueOpt = estoqueRepository.findByFilialAndProduto(filial.getId(), produto.getId());

        if (estoqueOpt.isPresent()) {
            Estoque estoque = estoqueOpt.get();
            int novaQuantidadeEmEstoque = estoque.getQuantidade() + quantidade;
            estoque.setQuantidade(novaQuantidadeEmEstoque);
            return estoqueRepository.save(estoque);
        } else {
            return novoEstoque(filial, produto, quantidade);
        }
    }

    private Estoque salvarSaidaEstoque(Filial filial, Produto produto, Integer quantidade) {
        return estoqueRepository.findByFilialAndProduto(filial.getId(), produto.getId())
                .map(estoque -> {
                    if (estoque.getQuantidade() < quantidade) {
                        String mensagem = "Estoque para" + produto.getDescricao() + " não está mais disponível";
                        throw new ServiceException(mensagem);
                    }

                    int novaQuantidadeEmEstoque = estoque.getQuantidade() - quantidade;
                    estoque.setQuantidade(novaQuantidadeEmEstoque);
                    return estoqueRepository.save(estoque);
                })
                .orElseThrow(() -> new ServiceException("Estoque inexistente"));
    }

    private Estoque novoEstoque(Filial filial, Produto produto, Integer quantidade) {
        return estoqueRepository.save(Estoque.builder()
                .filial(filial)
                .produto(produto)
                .quantidade(quantidade)
                .build());
    }

    private void processarItem(ItemPedido itemPedido) {
        itemPedido.setStatusItem(StatusItem.builder().id(StatusItem.PROCESSADO).build());
        itemPedidoRepository.save(itemPedido);
    }

    private String format(String s) {
        return join('%', trim(lowerCase(s)), '%');
    }
}
