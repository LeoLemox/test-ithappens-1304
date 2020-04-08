package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.Usuario;
import br.com.ithappens.ithappensbackend.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.http.ResponseEntity.*;

@RestController
@AllArgsConstructor
@RequestMapping("/usuario")
public class UsuarioController {

    private UsuarioRepository usuarioRepository;

    @GetMapping
    private ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll(Sort.by(asc("nome")));
        return usuarios.isEmpty() ? noContent().build() : ok(usuarios);
    }

    @GetMapping("/{id}")
    private ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(notFound().build());
    }
}
