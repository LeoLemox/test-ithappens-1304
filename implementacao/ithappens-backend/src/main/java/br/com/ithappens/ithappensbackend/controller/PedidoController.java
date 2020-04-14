package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.Pedido;
import br.com.ithappens.ithappensbackend.model.dto.PedidoDto;
import br.com.ithappens.ithappensbackend.model.requestmodel.PedidoRequest;
import br.com.ithappens.ithappensbackend.model.requestmodel.impl.PedidoEntradaRequest;
import br.com.ithappens.ithappensbackend.model.requestmodel.impl.PedidoSaidaRequest;
import br.com.ithappens.ithappensbackend.repository.PedidoRepository;
import br.com.ithappens.ithappensbackend.service.PedidoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@AllArgsConstructor
@RequestMapping("/pedido")
public class PedidoController {

    private PedidoRepository pedidoRepository;
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<PedidoDto>> buscarTodos() {
        List<PedidoDto> pedidos = pedidoRepository.findAllDto();
        return pedidos.isEmpty() ? noContent().build() : ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDto> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findDtoById(id)
                .map(ResponseEntity::ok)
                .orElse(noContent().build());
    }

    @GetMapping("/buscaAvancada")
    public ResponseEntity<Page<PedidoDto>> buscaAvancada(@RequestParam(required = false) Long tipoPedido,
                                                         @RequestParam(required = false) Long formaPagamento,
                                                         @RequestParam(required = false) String cliente,
                                                         @RequestParam(required = false) String filial,
                                                         @RequestParam(required = false) String usuario,
                                                         @RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "10") int size) {
        Page<PedidoDto> pedidos = pedidoService
                .buscaAvancada(tipoPedido, formaPagamento, cliente, filial, usuario, page, size);
        return pedidos.isEmpty() ? noContent().build() : ok(pedidos);
    }

    @PostMapping("/entrada")
    public ResponseEntity entrada(@Valid @RequestBody PedidoEntradaRequest pedido, HttpServletRequest request) {
        return salvarPedido(pedido, request);
    }

    @PostMapping("/saida")
    public ResponseEntity saida(@Valid @RequestBody PedidoSaidaRequest pedido, HttpServletRequest request) {
        return salvarPedido(pedido, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        pedidoRepository.deleteById(id);
        return noContent().build();
    }

    private ResponseEntity salvarPedido(PedidoRequest pedido, HttpServletRequest request) {
        Pedido novoPedido = pedidoRepository.save(pedido.gerarPedido());
        URI location = ServletUriComponentsBuilder.fromContextPath(request).path("/pedido/{id}").buildAndExpand(novoPedido.getId()).toUri();
        return created(location).build();
    }
}
