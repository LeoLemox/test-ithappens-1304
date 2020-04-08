package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.model.Usuario;
import br.com.ithappens.ithappensbackend.repository.UsuarioRepository;
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
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository repository;

    private ObjectMapper objectMapper;
    private Usuario usuario;
    private List<Usuario> usuarios;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();

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

        mockMvc.perform(get("/usuario").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarStatus204() throws Exception {

        when(repository.findAll(Sort.by(asc("nome")))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuario").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus200EUsuarioComIdCorrespondente() throws Exception {

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/usuario/1").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(usuario)));
    }

    @Test
    void deveRetornarStatus404AoNaoEncontrarOUsuarioComIdCorrespondente() throws Exception {

        when(repository.findById(7L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuario/7").accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}