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
        usuario = Usuario.builder().id(6L).nome("José da Silva").email("jose.silva@ithappens.com").senha("jose@123").build();
    }

    @Test
    void aoSalvarUsuarioDeveRetornarUsuarioSalvo() {

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario novoUsuario = usuarioService.salvar(usuario);

        assertEquals(usuario.getId(), novoUsuario.getId());
        assertEquals(usuario.getNome(), novoUsuario.getNome());
    }

    @Test
    void aoVerificarEmailJaExistenteLancarExcecao() {

        when(usuarioRepository.findByEmail(any(String.class))).thenReturn(Optional.of(usuario));

        assertThrows(ServiceException.class, () -> usuarioService.salvar(usuario));
    }

    @Test
    void aoVerificarUsuarioInexistenteLancarExcecao() {

        when(usuarioRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> usuarioService.excluir(99L));
    }
}
