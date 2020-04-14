package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.ItemPedido;
import br.com.ithappens.ithappensbackend.model.StatusItem;
import br.com.ithappens.ithappensbackend.model.TipoPedido;
import br.com.ithappens.ithappensbackend.repository.EstoqueRepository;
import br.com.ithappens.ithappensbackend.repository.ItemPedidoRepository;
import br.com.ithappens.ithappensbackend.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ItemPedidoService {

    private EstoqueRepository estoqueRepository;
    private ItemPedidoRepository itemPedidoRepository;
    private PedidoRepository pedidoRepository;

    @Transactional
    public ItemPedido adicionar(ItemPedido itemPedido) {
        verificaItens(itemPedido);
        itemPedido = verificaEstoque(itemPedido);
        return itemPedidoRepository.save(itemPedido);
    }

    @Transactional
    public ItemPedido cancelar(Long id) {
        return itemPedidoRepository.findById(id)
                .map(ip -> {
                    ip.setStatusItem(StatusItem.builder().id(StatusItem.CANCELADO).build());
                    return itemPedidoRepository.save(ip);
                })
                .orElseThrow(() -> new ServiceException("Item inexistente"));
    }

    private ItemPedido verificaEstoque(ItemPedido itemPedido) {

        return pedidoRepository.findById(itemPedido.getPedido().getId())
                .map(pedido -> {
                    if (pedido.getTipoPedido().getId().equals(TipoPedido.ENTRADA)) return itemPedido;

                    return estoqueRepository.findByFilialAndProduto(pedido.getFilial().getId(), itemPedido.getProduto().getId())
                            .map(estoque -> {
                                if (estoque.getQuantidade() < itemPedido.getQuantidade()) {

                                    String mensagem = estoque.getQuantidade().equals(0)
                                            ? "Estoque indisponível"
                                            : "Estoque dispoível: " + estoque.getQuantidade();
                                    throw new ServiceException(mensagem);
                                }

                                itemPedido.setValorUnitario(estoque.getProduto().getValor());
                                return itemPedido;
                            })
                            .orElseThrow(() -> new ServiceException("Estoque indisponível"));
                })
                .orElseThrow(() -> new ServiceException("Pedido inexistente"));
    }

    private void verificaItens(ItemPedido itemPedido) {
        if (itemPedidoRepository.findByPedido(itemPedido.getPedido().getId())
                .stream()
                .anyMatch(ip -> ip.getProduto().getId().equals(itemPedido.getProduto().getId())
                        && !ip.getStatusItem().getId().equals(StatusItem.CANCELADO)))
            throw new ServiceException("O produto já se encontra na lista de itens");
    }
}
