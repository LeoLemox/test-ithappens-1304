package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.Cliente;
import br.com.ithappens.ithappensbackend.repository.ClienteRepository;
import br.com.ithappens.ithappensbackend.service.ClienteService;
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
@RequestMapping("/cliente")
public class ClienteController {

    private ClienteRepository clienteRepository;
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> buscarTodos() {
        List<Cliente> clientes = clienteRepository.findAll(Sort.by(asc("nome")));
        return clientes.isEmpty() ? noContent().build() : ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(noContent().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> novo(@Valid @RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.salvar(cliente);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(novoCliente.getId()).toUri();
        return created(location).body(novoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return ok(clienteService.atualizar(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return noContent().build();
    }
}
