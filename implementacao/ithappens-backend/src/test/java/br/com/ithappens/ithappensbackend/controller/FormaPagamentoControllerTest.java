package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.FormaPagamento;
import br.com.ithappens.ithappensbackend.repository.FormaPagamentoRepository;
import br.com.ithappens.ithappensbackend.service.FormaPagamentoService;
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

@WebMvcTest(FormaPagamentoController.class)
public class FormaPagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FormaPagamentoRepository repository;

    @MockBean
    private FormaPagamentoService service;

    private ObjectMapper mapper;

    private FormaPagamento formaPagamento;
    private List<FormaPagamento> formasPagamento;

    @BeforeEach
    void init() {

        mapper = new ObjectMapper();
        formaPagamento = FormaPagamento.builder().id(1L).descricao("A VISTA").build();
        formasPagamento = asList(
                formaPagamento,
                FormaPagamento.builder().id(2L).descricao("BOLETO").build(),
                FormaPagamento.builder().id(3L).descricao("CARTAO").build());
    }

    @Test
    void deveRetornarStatus200EListagemDeFormasDePagamento() throws Exception {

        when(repository.findAll(Sort.by(asc("descricao")))).thenReturn(
                formasPagamento.stream()
                        .sorted(Comparator.comparing(FormaPagamento::getDescricao))
                        .collect(Collectors.toList())
        );

        mockMvc.perform(get("/formaPagamento")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void deveRetornarStatus204CasoNaoExistamFormasDePagamentoCadastradas() throws Exception {

        when(repository.findAll(Sort.by(asc("descricao")))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/formaPagamento")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus200EFormaPagamentoComIdCorrespondente() throws Exception {

        when(repository.findById(1L)).thenReturn(Optional.of(formaPagamento));

        mockMvc.perform(get("/formaPagamento/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(formaPagamento)));
    }

    @Test
    void deveRetornarStatus204AoNaoEncontrarAFormaPagamentoComIdCorrespondente() throws Exception {

        when(repository.findById(4L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/formaPagamento/4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus201EFormaPagamentoCriada() throws Exception {

        FormaPagamento novaFormaPagamento = FormaPagamento.builder().descricao("TICKET").build();
        FormaPagamento formaPagamentoSalva = FormaPagamento.builder().id(4L).descricao("TICKET").build();

        when(service.salvar(novaFormaPagamento)).thenReturn(formaPagamentoSalva);

        mockMvc.perform(post("/formaPagamento")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novaFormaPagamento)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "http://localhost/formaPagamento/4"));
    }

    @Test
    void deveValidarAtributosObrigatoriosERetornarStatus400EListaDeErrosParaAtributosInvalidos() throws Exception {

        mockMvc.perform(post("/formaPagamento")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new FormaPagamento())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].userMessage", containsInAnyOrder("Descrição é obrigatório(a)")));
    }

    @Test
    void deveRetornarStatus400EMensagemDeErroAoEncontrarDescricaoJaCadastrada() throws Exception {

        FormaPagamento novaFormaPagamento = FormaPagamento.builder().descricao("Boleto").build();

        when(service.salvar(novaFormaPagamento)).thenThrow(new ServiceException("Descrição já cadastrada."));

        mockMvc.perform(post("/formaPagamento")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novaFormaPagamento)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Descrição já cadastrada.")));
    }

    @Test
    void deveRetornarStatus204AoExcluirFormaPagamento() throws Exception {

        mockMvc.perform(delete("/formaPagamento/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus404EMensagemDeErroAoTentarExcluirFormaPagamentoInexistente() throws Exception {

        doThrow(new EmptyResultDataAccessException(1)).when(repository).deleteById(99L);

        mockMvc.perform(delete("/formaPagamento/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Recurso não encontrado")));
    }

    @Test
    void deveRetornarStatus404EMensagemDeErroAoTentarAtualizarFormaPagamentoInexistente() throws Exception {

        FormaPagamento formaPagamentoAtualizada = FormaPagamento.builder().id(99L).build();

        when(service.atualizar(99L, formaPagamentoAtualizada)).thenThrow(new EmptyResultDataAccessException(1));

        mockMvc.perform(put("/formaPagamento/99")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(formaPagamentoAtualizada)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Recurso não encontrado")));
    }
}
