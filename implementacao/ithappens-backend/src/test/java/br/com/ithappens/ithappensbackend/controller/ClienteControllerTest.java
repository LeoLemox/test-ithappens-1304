package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Cliente;
import br.com.ithappens.ithappensbackend.repository.ClienteRepository;
import br.com.ithappens.ithappensbackend.service.ClienteService;
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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository repository;

    @MockBean
    private ClienteService service;

    private ObjectMapper mapper;

    private Cliente cliente;
    private List<Cliente> clientes;

    @BeforeEach
    void init() {

        mapper = new ObjectMapper();
        cliente = Cliente.builder().id(1L).nome("Odin").cpf("315.774.880-75").build();
        clientes = asList(
                cliente,
                Cliente.builder().id(2L).nome("Frigga").cpf("686.764.740-52").build(),
                Cliente.builder().id(3L).nome("Loki").cpf("327.010.520-76").build(),
                Cliente.builder().id(4L).nome("Thor").cpf("260.305.790-10").build(),
                Cliente.builder().id(5L).nome("Tyr").cpf("269.106.840-44").build(),
                Cliente.builder().id(6L).nome("Mimir").cpf("907.862.960-62").build(),
                Cliente.builder().id(7L).nome("Aegir").cpf("504.880.470-80").build(),
                Cliente.builder().id(8L).nome("Bader").cpf("739.414.110-12").build());
    }

    @Test
    void deveRetornarStatus200EListagemDeClientes() throws Exception {

        when(repository.findAll(Sort.by(asc("nome")))).thenReturn(
                clientes.stream()
                        .sorted(Comparator.comparing(Cliente::getNome))
                        .collect(Collectors.toList())
        );

        mockMvc.perform(get("/cliente")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(8)));
    }

    @Test
    void deveRetornarStatus204CasoNaoExistamClientesCadastrados() throws Exception {

        when(repository.findAll(Sort.by(asc("nome")))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cliente")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus200EClienteComIdCorrespondente() throws Exception {

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/cliente/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(cliente)));
    }

    @Test
    void deveRetornarStatus204AoNaoEncontrarOClienteComIdCorrespondente() throws Exception {

        when(repository.findById(9L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/cliente/9")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus201EClienteCriado() throws Exception {

        Cliente novoCliente = Cliente.builder().nome("José").cpf("437.974.240-78").build();
        Cliente clienteSalvo = Cliente.builder().id(9L).nome("José").cpf("437.974.240-78").build();

        when(service.salvar(novoCliente)).thenReturn(clienteSalvo);

        mockMvc.perform(post("/cliente")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoCliente)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "http://localhost/cliente/9"));
    }

    @Test
    void deveValidarAtributosObrigatoriosERetornarStatus400EListaDeErrosParaAtributosInvalidos() throws Exception {

        mockMvc.perform(post("/cliente")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Cliente())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].userMessage", containsInAnyOrder("Nome é obrigatório(a)", "CPF é obrigatório(a)")));
    }

    @Test
    void deveRetornarStatus400EMensagemDeErroAoEncontrarCpfJaCadastrado() throws Exception {

        Cliente novoCliente = Cliente.builder().nome("José").cpf("315.774.880-75").build();

        when(service.salvar(novoCliente)).thenThrow(new ServiceException("CPF já cadastrado."));

        mockMvc.perform(post("/cliente")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoCliente)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("CPF já cadastrado.")));
    }

    @Test
    void deveRetornarStatus204AoExcluirCliente() throws Exception {

        mockMvc.perform(delete("/cliente/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus404EMensagemDeErroAoTentarExcluirClienteInexistente() throws Exception {

        doThrow(new EmptyResultDataAccessException(1)).when(repository).deleteById(99L);

        mockMvc.perform(delete("/cliente/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Recurso não encontrado")));
    }

    @Test
    void deveRetornarStatus404EMensagemDeErroAoTentarAtualizarClienteInexistente() throws Exception {

        Cliente atualizado = Cliente.builder().id(99L).build();

        when(service.atualizar(99L, atualizado)).thenThrow(new EmptyResultDataAccessException(1));

        mockMvc.perform(put("/cliente/99")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(atualizado)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Recurso não encontrado")));
    }
}
