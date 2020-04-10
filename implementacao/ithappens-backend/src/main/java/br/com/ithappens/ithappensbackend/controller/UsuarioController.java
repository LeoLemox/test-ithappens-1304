package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.Usuario;
import br.com.ithappens.ithappensbackend.repository.UsuarioRepository;
import br.com.ithappens.ithappensbackend.service.UsuarioService;
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
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @GetMapping
    private ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll(Sort.by(asc("nome")));
        return usuarios.isEmpty() ? noContent().build() : ok(usuarios);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(noContent().build());
    }

    @PostMapping
    private ResponseEntity<Usuario> novo(@Valid @RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.salvar(usuario);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(novoUsuario.getId()).toUri();
        return created(location).body(novoUsuario);
    }

    @PutMapping("/{id}")
    private ResponseEntity atualizar(@PathVariable Long id, Usuario usuario) {
        return ok(usuarioService.atualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity excluir(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return noContent().build();
    }
}
