package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Usuario;
import br.com.ithappens.ithappensbackend.repository.UsuarioRepository;
import br.com.ithappens.ithappensbackend.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository repository;

    @MockBean
    private UsuarioService service;

    private ObjectMapper mapper;

    private Usuario usuario;
    private List<Usuario> usuarios;

    @BeforeEach
    void init() {

        mapper = new ObjectMapper();
        usuario = Usuario.builder().id(1L).nome("Leonardo").email("leonardo@ithappens.com").senha("leonardo@123").build();
        usuarios = asList(
                usuario,
                Usuario.builder().id(2L).nome("Donatello").email("donatello@ithappens.com").senha("donatello@123").build(),
                Usuario.builder().id(3L).nome("Raphael").email("raphael@ithappens.com").senha("raphael@123").build(),
                Usuario.builder().id(4L).nome("Michelangelo").email("michelangelo@ithappens.com").senha("michelangelo@123").build(),
                Usuario.builder().id(5L).nome("Splinter").email("splinter@ithappens.com").senha("splinter@123").build());
    }

    @Test
    void deveRetornarStatus200EListagemDeUsuarios() throws Exception {

        when(repository.findAll(Sort.by(asc("nome")))).thenReturn(
                usuarios.stream()
                        .sorted(Comparator.comparing(Usuario::getNome))
                        .collect(Collectors.toList())
        );

        mockMvc.perform(get("/usuario")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarStatus204() throws Exception {

        when(repository.findAll(Sort.by(asc("nome")))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuario")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus200EUsuarioComIdCorrespondente() throws Exception {

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/usuario/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(usuario)));
    }

    @Test
    void deveRetornarStatus204AoNaoEncontrarOUsuarioComIdCorrespondente() throws Exception {

        when(repository.findById(7L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuario/7")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus201EUsuarioCriado() throws Exception {

        Usuario novoUsuario = Usuario.builder().nome("José").email("jose@ithappens.com").senha("jose@123").build();
        Usuario usuarioSalvo = Usuario.builder().id(6L).nome("José").email("jose@ithappens.com").senha("jose@123").build();

        when(service.salvar(novoUsuario)).thenReturn(usuarioSalvo);

        mockMvc.perform(post("/usuario")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "http://localhost/usuario/6"));
    }

    @Test
    void deveValidarAtributosObrigatoriosERetornarStatus400EListaDeErrosParaAtributosInvalidos() throws Exception {

        mockMvc.perform(post("/usuario")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Usuario())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].userMessage", containsInAnyOrder("Nome é obrigatório(a)", "Email é obrigatório(a)", "Senha é obrigatório(a)")));
    }

    @Test
    void deveRetornarStatus400EMensagemDeErroAoEncontrarEmailJaCadastrado() throws Exception {

        Usuario novoUsuario = Usuario.builder().nome("leonardo").email("leonardo@ithappens.com").senha("leonardo@123").build();

        when(service.salvar(novoUsuario)).thenThrow(new ServiceException("Email já cadastrado."));

        mockMvc.perform(post("/usuario")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Email já cadastrado.")));
    }

    @Test
    void deveRetornarStatus204AoExcluirUsuario() throws Exception {

        mockMvc.perform(delete("/usuario/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus400EMensagemDeErroAoTentarExcluirUsuarioInexistente() throws Exception {

        doThrow(new ServiceException("Usuário inexistente.")).when(service).excluir(99L);

        mockMvc.perform(delete("/usuario/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Usuário inexistente.")));
        ;
    }
}
