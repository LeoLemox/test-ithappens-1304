package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.Filial;
import br.com.ithappens.ithappensbackend.repository.FilialRepository;
import br.com.ithappens.ithappensbackend.service.FilialService;
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
@RequestMapping("/filial")
public class FilialController {

    private FilialRepository filialRepository;
    private FilialService filialService;

    @GetMapping
    private ResponseEntity<List<Filial>> buscarTodos() {
        List<Filial> filiais = filialRepository.findAll(Sort.by(asc("nome")));
        return filiais.isEmpty() ? noContent().build() : ok(filiais);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Filial> buscarPorId(@PathVariable Long id) {
        return filialRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(noContent().build());
    }

    @PostMapping
    private ResponseEntity<Filial> novo(@Valid @RequestBody Filial filial) {
        Filial novaFilial = filialService.salvar(filial);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(novaFilial.getId()).toUri();
        return created(location).body(novaFilial);
    }

    @PutMapping("/{id}")
    private ResponseEntity atualizar(@PathVariable Long id, @RequestBody Filial filial) {
        return ok(filialService.atualizar(id, filial));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity excluir(@PathVariable Long id) {
        filialRepository.deleteById(id);
        return noContent().build();
    }
}
