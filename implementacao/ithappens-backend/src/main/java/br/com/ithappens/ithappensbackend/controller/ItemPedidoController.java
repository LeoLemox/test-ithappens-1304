package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.ItemPedido;
import br.com.ithappens.ithappensbackend.model.dto.ItemPedidoDto;
import br.com.ithappens.ithappensbackend.model.requestmodel.impl.ItemPedidoRequest;
import br.com.ithappens.ithappensbackend.repository.ItemPedidoRepository;
import br.com.ithappens.ithappensbackend.service.ItemPedidoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@AllArgsConstructor
@RequestMapping("/itemPedido")
public class ItemPedidoController {

    private ItemPedidoRepository itemPedidoRepository;
    private ItemPedidoService itemPedidoService;

    @GetMapping
    public ResponseEntity<List<ItemPedidoDto>> buscarTodos() {
        List<ItemPedidoDto> itens = itemPedidoRepository.findAllDto();
        return itens.isEmpty() ? noContent().build() : ok(itens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemPedidoDto> buscarPorId(@PathVariable Long id) {
        return itemPedidoRepository.findDtoById(id)
                .map(ResponseEntity::ok)
                .orElse(noContent().build());
    }

    @GetMapping("/pedido/{id}")
    public ResponseEntity<List<ItemPedidoDto>> buscarPorPedido(@PathVariable Long id) {
        List<ItemPedidoDto> itens = itemPedidoRepository.findDtoByPedido(id);
        return itens.isEmpty() ? noContent().build() : ok(itens);
    }

    @PostMapping
    public ResponseEntity novo(@Valid @RequestBody ItemPedidoRequest itemPedido) {
        ItemPedido itemPedidoSalvo = itemPedidoService.adicionar(itemPedido.gerarItem());
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(itemPedidoSalvo.getId()).toUri();
        return created(location).build();
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<ItemPedido> cancelar(@PathVariable Long id) {
        return ok().body(itemPedidoService.cancelar(id));
    }
}
