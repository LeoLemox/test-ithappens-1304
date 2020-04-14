package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.Produto;
import br.com.ithappens.ithappensbackend.repository.ProdutoRepository;
import br.com.ithappens.ithappensbackend.service.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/produto")
public class ProdutoController {

    private ProdutoRepository produtoRepository;
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produto>> buscarTodos() {
        List<Produto> produtos = produtoRepository.findAll(Sort.by(asc("descricao")));
        return produtos.isEmpty() ? noContent().build() : ok(produtos);
    }

    @GetMapping("/buscaAvancada")
    public ResponseEntity<Page<Produto>> buscaAvancada(@RequestParam(required = false) Long sequencial,
                                                        @RequestParam(required = false) String descricao,
                                                        @RequestParam(required = false) String codigoDeBarras,
                                                        @RequestParam(required = false, defaultValue = "0") int page,
                                                        @RequestParam(required = false, defaultValue = "10") int size) {
        Page<Produto> produtos = produtoService.buscaAvancada(sequencial, descricao, codigoDeBarras, page, size);
        return produtos.isEmpty() ? noContent().build() : ok(produtos);
    }

    @PostMapping
    public ResponseEntity<Produto> novo(@Valid @RequestBody Produto produto) {
        Produto novoProduto = produtoService.salvar(produto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(novoProduto.getId()).toUri();
        return created(location).body(novoProduto);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        return ok(produtoService.atualizar(id, produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        produtoRepository.deleteById(id);
        return noContent().build();
    }
}
