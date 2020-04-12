package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.FormaPagamento;
import br.com.ithappens.ithappensbackend.repository.FormaPagamentoRepository;
import br.com.ithappens.ithappensbackend.service.FormaPagamentoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.http.ResponseEntity.*;

@RestController
@AllArgsConstructor
@RequestMapping("/formaPagamento")
public class FormaPagamentoController {

    private FormaPagamentoRepository formaPagamentoRepository;
    private FormaPagamentoService formaPagamentoService;

    @GetMapping
    private ResponseEntity<List<FormaPagamento>> buscarTodos() {
        List<FormaPagamento> formasPagamento = formaPagamentoRepository.findAll(Sort.by(asc("descricao")));
        return formasPagamento.isEmpty() ? noContent().build() : ok(formasPagamento);
    }

    @GetMapping("/{id}")
    private ResponseEntity<FormaPagamento> buscarPorId(@PathVariable Long id) {
        return formaPagamentoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(noContent().build());
    }

    @PostMapping
    private ResponseEntity<FormaPagamento> novo(@Valid @RequestBody FormaPagamento formaPagamento) {
        FormaPagamento novaFormaPagamento = formaPagamentoService.salvar(formaPagamento);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(novaFormaPagamento.getId()).toUri();
        return created(location).body(novaFormaPagamento);
    }

    @PutMapping("/{id}")
    private ResponseEntity atualizar(@PathVariable Long id, @RequestBody FormaPagamento formaPagamento) {
        return ok(formaPagamentoService.atualizar(id, formaPagamento));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity excluir(@PathVariable Long id) {
        formaPagamentoRepository.deleteById(id);
        return noContent().build();
    }
}
