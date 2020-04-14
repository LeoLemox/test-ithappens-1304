package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.model.dto.PedidoDto;
import br.com.ithappens.ithappensbackend.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.*;

@Service
@AllArgsConstructor
public class PedidoService {

    private PedidoRepository pedidoRepository;


    public Page<PedidoDto> buscaAvancada(Long tipoPedido, Long formaPagamento, String cliente, String filial,
                                         String usuario, int page, int size) {
        return pedidoRepository.search(tipoPedido, formaPagamento, format(cliente), format(filial), format(usuario),
                PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }

    private String format(String s) {
        return join('%', trim(lowerCase(s)), '%');
    }
}
