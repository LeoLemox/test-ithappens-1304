package br.com.ithappens.ithappensbackend.controller;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Produto;
import br.com.ithappens.ithappensbackend.repository.ProdutoRepository;
import br.com.ithappens.ithappensbackend.service.ProdutoService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoRepository repository;

    @MockBean
    private ProdutoService service;

    private ObjectMapper mapper;

    private Produto produto;
    private List<Produto> produtos;

    @BeforeEach
    void init() {

        mapper = new ObjectMapper();
        produto = Produto.builder().id(1L).descricao("Pincel Para Pintura Condor Redondo 472 Numero 02")
                .codigoBarras("7891055033326").valor(BigDecimal.valueOf(4.19)).build();
        produtos = asList(
                produto,
                Produto.builder().id(2L).descricao("Giz de Cera Fabre Castell 15x1").codigoBarras("7891360589310")
                        .valor(BigDecimal.valueOf(1.99)).build(),
                Produto.builder().id(3L).descricao("Pincel Para Pintura Condor Chato 471 número")
                        .codigoBarras("7891055066621").valor(BigDecimal.valueOf(1.95)).build(),
                Produto.builder().id(4L).descricao("Tela Para Pintura Souza 24x30cm").codigoBarras("7896309204996")
                        .valor(BigDecimal.valueOf(7.19)).build(),
                Produto.builder().id(5L).descricao("Tela Para Pintura Souza 30x40cm").codigoBarras("7896309205030")
                        .valor(BigDecimal.valueOf(8.59)).build());
    }

    @Test
    void deveRetornarStatus200EListagemDeProdutos() throws Exception {

        when(repository.findAll(Sort.by(asc("descricao")))).thenReturn(
                produtos.stream()
                        .sorted(Comparator.comparing(Produto::getDescricao))
                        .collect(Collectors.toList())
        );

        mockMvc.perform(get("/produto")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void deveRetornarStatus204CasoNaoExistamProdutosCadastrados() throws Exception {

        when(repository.findAll(Sort.by(asc("descricao")))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/produto")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus201EProdutoCriado() throws Exception {

        Produto novoProduto = Produto.builder().descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(10.69)).codigoBarras("7894904015108").build();
        Produto produtoSalvo = Produto.builder().id(6L).descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(10.69)).codigoBarras("7894904015108").build();

        when(service.salvar(novoProduto)).thenReturn(produtoSalvo);

        mockMvc.perform(post("/produto")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoProduto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "http://localhost/produto/6"));
    }

    @Test
    void deveValidarAtributosObrigatoriosERetornarStatus400EListaDeErrosParaAtributosInvalidos() throws Exception {

        mockMvc.perform(post("/produto")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Produto())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].userMessage", containsInAnyOrder("Descrição é obrigatório(a)",
                        "Código de barras é obrigatório(a)", "Valor é obrigatório(a)")));
    }

    @Test
    void deveValidarCodigoDeBarrasPermitindoApenasDigitosERetornarStatus400CasoPossuaLetraOuCaracteresEpeciais()
            throws Exception {

        Produto novoProduto = Produto.builder().descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(10.69)).codigoBarras("789490401-ABC").build();

        mockMvc.perform(post("/produto")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoProduto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].userMessage",
                        containsInAnyOrder("O Código de barras deve conter apenas dígitos")));
    }

    @Test
    void deveValidarCodigoDeBarrasPermitindoDe3A13DigitosRetornarStatus400CasoNaoSejaAtendido() throws Exception {

        Produto novoProduto = Produto.builder().descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(10.69)).codigoBarras("7894904015100008").build();

        mockMvc.perform(post("/produto")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoProduto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].userMessage",
                        containsInAnyOrder("O Código de barras deve possuir de 3 a 13 dígitos")));

        novoProduto = Produto.builder().descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(10.69)).codigoBarras("78").build();

        mockMvc.perform(post("/produto")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoProduto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].userMessage",
                        containsInAnyOrder("O Código de barras deve possuir de 3 a 13 dígitos")));
    }

    @Test
    void deveValidarValorPermitindoApenasPositivosMenoresQueUmMilhaoEComApenasDuasCasasDecimaisRetornarStatus400CasoNaoSejaAtendido()
            throws Exception {

        Produto novoProduto = Produto.builder().descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(-1000000.6999)).codigoBarras("7894904015108").build();

        mockMvc.perform(post("/produto")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoProduto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].userMessage",
                        containsInAnyOrder("Valor deve ser maior ou igual à zero", "Valor em formato inválido(a)")));
    }

    @Test
    void deveRetornarStatus400EMensagemDeErroAoEncontrarCodigoDeBarrasJaCadastrado() throws Exception {

        Produto novoProduto = Produto.builder().descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(10.69)).codigoBarras("7894904015108").build();

        when(service.salvar(novoProduto)).thenThrow(new ServiceException("Código de barras já cadastrado."));

        mockMvc.perform(post("/produto")
                .characterEncoding("utf-8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(novoProduto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Código de barras já cadastrado.")));
    }

    @Test
    void deveRetornarStatus204AoExcluirProduto() throws Exception {

        mockMvc.perform(delete("/produto/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus404EMensagemDeErroAoTentarExcluirProdutoInexistente() throws Exception {

        doThrow(new EmptyResultDataAccessException(1)).when(repository).deleteById(99L);

        mockMvc.perform(delete("/produto/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userMessage", equalTo("Recurso não encontrado")));
    }

    @Test
    void deveRetornarStatus404EMensagemDeErroAoTentarAtualizarProdutoInexistente() throws Exception {

        Produto atualizado = Produto.builder().id(99L).build();

        when(service.atualizar(99L, atualizado)).thenThrow(new EmptyResultDataAccessException(1));

        mockMvc.perform(put("/produto/99")
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
