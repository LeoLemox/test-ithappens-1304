package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Usuario;
import br.com.ithappens.ithappensbackend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsuarioServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @InjectMocks
    UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void init() {
        usuario = new Usuario();
        usuario.setNome("José da Silva");
        usuario.setEmail("jose.silva@ithappens.com");
        usuario.setSenha("jose@123");
    }

    @Test
    void aoSalvarUsuarioDeveRetornarUsuarioSalvo() {

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario novoUsuario = usuarioService.salvar(usuario);

        assertEquals(usuario.getNome(), novoUsuario.getNome());
        assertEquals(usuario.getEmail(), novoUsuario.getEmail());
        assertEquals(usuario.getSenha(), novoUsuario.getSenha());
    }

    @Test
    void aoVerificarEmailJaExistenteLancarExcecao() {

        when(usuarioRepository.findByEmail(any(String.class))).thenReturn(Optional.of(usuario));

        assertThrows(ServiceException.class, () -> usuarioService.salvar(usuario));
    }
}
