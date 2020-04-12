package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Filial;
import br.com.ithappens.ithappensbackend.repository.FilialRepository;
import br.com.ithappens.ithappensbackend.service.FilialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
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

@WebMvcTest(FilialController.class)
public class FilialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilialRepository repository;

    @MockBean
    private FilialService service;

    private ObjectMapper mapper;

    private Filial filial;
    private List<Filial> filiais;

    @BeforeEach
    void init() {

        mapper = new ObjectMapper();
        filial = Filial.builder().id(1L).nome("Filial Bacanga").build();
        filiais = asList(
                filial,
                Filial.builder().id(2L).nome("Filial Calhau").build(),
                Filial.builder().id(3L).nome("Filial Mix Curva Do 90").build(),
                Filial.builder().id(4L).nome("Filial Mix Jardim Tropical").build(),
                Filial.builder().id(5L).nome("Filial Rio Anil").build());
    }

    @Test
    void deveRetornarStatus200EListagemDeFiliais() throws Exception {

        when(repository.findAll(Sort.by(asc("nome")))).thenReturn(
                filiais.stream()
                        .sorted(Comparator.comparing(Filial::getNome))
                        .collect(Collectors.toList())
        );

        mockMvc.perform(get("/filial")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void deveRetornarStatus204CasoNaoExistamFiliaisCadastradas() throws Exception {

        when(repository.findAll(Sort.by(asc("nome")))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/filial")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus200EFilialComIdCorrespondente() throws Exception {

        when(repository.findById(1L)).thenReturn(Optional.of(filial));

        mockMvc.perform(get("/filial/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(filial)));
    }

    @Test
    void deveRetornarStatus204AoNaoEncontrarAFilialComIdCorrespondente() throws Exception {

        when(repository.findById(9L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/filial/6")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus201EFilialCriada() throws Exception {

        Filial novaFilial = Filial.builder().nome("Cajazeiras").build();
        Filial filialSalva = Filial.builder().id(6L).nome("Cajazeiras").build();

        when(service.salvar(novaFilial)).thenReturn(filialSalva);

        mockMvc.perform(post("/filial")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novaFilial)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "http://localhost/filial/6"));
    }

    @Test
    void deveValidarAtributosObrigatoriosERetornarStatus400EListaDeErrosParaAtributosInvalidos() throws Exception {

        mockMvc.perform(post("/filial")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Filial())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].userMessage", containsInAnyOrder("Nome é obrigatório(a)")));
    }

    @Test
    void deveRetornarStatus400EMensagemDeErroAoEncontrarNomeJaCadastrado() throws Exception {

        Filial novaFilial = Filial.builder().nome("Filial Bacanga").build();

        when(service.salvar(novaFilial)).thenThrow(new ServiceException("Nome já cadastrado."));

        mockMvc.perform(post("/filial")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novaFilial)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Nome já cadastrado.")));
    }

    @Test
    void deveRetornarStatus204AoExcluirFilial() throws Exception {

        mockMvc.perform(delete("/filial/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus404EMensagemDeErroAoTentarExcluirFilialInexistente() throws Exception {

        doThrow(new EmptyResultDataAccessException(1)).when(repository).deleteById(99L);

        mockMvc.perform(delete("/filial/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Recurso não encontrado")));
    }

    @Test
    void deveRetornarStatus404EMensagemDeErroAoTentarAtualizarFilialInexistente() throws Exception {

        Filial filialAtualizada = Filial.builder().id(99L).build();

        when(service.atualizar(99L, filialAtualizada)).thenThrow(new EmptyResultDataAccessException(1));

        mockMvc.perform(put("/filial/99")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(filialAtualizada)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Recurso não encontrado")));
    }
}
