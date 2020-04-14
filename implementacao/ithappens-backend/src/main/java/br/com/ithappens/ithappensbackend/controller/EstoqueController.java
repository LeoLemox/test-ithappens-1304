package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.Estoque;
import br.com.ithappens.ithappensbackend.repository.EstoqueRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@AllArgsConstructor
@RequestMapping("/estoque")
public class EstoqueController {

    private EstoqueRepository estoqueRepository;

    @GetMapping
    public ResponseEntity<List<Estoque>> buscarTodos() {
        List<Estoque> estoques = estoqueRepository.findAll();
        return estoques.isEmpty() ? noContent().build() : ok(estoques);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estoque> buscarPorId(@PathVariable Long id) {
        return estoqueRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(noContent().build());
    }

    @GetMapping("/filial/{filialId}")
    public ResponseEntity<List<Estoque>> buscarPorFilial(@PathVariable Long filialId) {
        List<Estoque> estoques = estoqueRepository.findByFilial(filialId);
        return estoques.isEmpty() ? noContent().build() : ok(estoques);
    }
}
