package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Produto;
import br.com.ithappens.ithappensbackend.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;

    @InjectMocks
    ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void init() {
        produto = Produto.builder()
                .id(51L)
                .descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(10.69))
                .codigoBarras("7894904015108")
                .build();
    }

    @Test
    void aoSalvarProdutoDeveRetornarProdutoSalvo() {

        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto novoProduto = produtoService.salvar(new Produto());

        assertEquals(produto.getId(), novoProduto.getId());
        assertEquals(produto.getCodigoBarras(), novoProduto.getCodigoBarras());
    }

    @Test
    void aoVerificarCodigoDeBarrasJaExistenteLancarExcecao() {

        when(produtoRepository.findByCodigoBarras(any(String.class))).thenReturn(Optional.of(produto));

        assertThrows(ServiceException.class, () -> produtoService.salvar(produto));
    }

    @Test
    void aoAtualizarProdutoDeveRetornarProdutoAtualizado() {

        Produto produtoAtualizado = Produto.builder()
                .descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(9.69))
                .codigoBarras("7894904015110")
                .build();

        when(produtoRepository.findById(any(Long.class))).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        produtoService.atualizar(51L, produtoAtualizado);

        assertEquals(produto.getValor(), produtoAtualizado.getValor());
    }

    @Test
    void aoAtualizarProdutoDeveRetornarExcecaoAoIdentificarCodigoDeBarrasJaExistenteParaOutroProduto() {

        Produto produtoAtualizado = Produto.builder()
                .descricao("Filé de Peito de Frango Seara Bandeja 1kg")
                .valor(BigDecimal.valueOf(10.69))
                .codigoBarras("7894904015110")
                .build();

        Produto produtoExistente = Produto.builder()
                .id(52L)
                .descricao("Filé de Peito de Frango Seara Bandeja 2kg")
                .valor(BigDecimal.valueOf(20.69))
                .codigoBarras("7894904015110")
                .build();

        when(produtoRepository.findByCodigoBarras(any(String.class))).thenReturn(Optional.of(produtoExistente));

        assertThrows(ServiceException.class, () -> produtoService.atualizar(51L, produtoAtualizado));
    }
}
