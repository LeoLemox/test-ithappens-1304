package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.FormaPagamento;
import br.com.ithappens.ithappensbackend.repository.FormaPagamentoRepository;
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
public class FormaPagamentoServiceTest {

    @Mock
    FormaPagamentoRepository formaPagamentoRepository;

    @InjectMocks
    FormaPagamentoService formaPagamentoService;

    private FormaPagamento formaPagamento;

    @BeforeEach
    void init() {
        formaPagamento = FormaPagamento.builder().id(4L).descricao("Ticket").build();
    }

    @Test
    void aoSalvarFormaDePagamentoDeveRetornarFormaSalva() {

        when(formaPagamentoRepository.save(any(FormaPagamento.class))).thenReturn(formaPagamento);

        FormaPagamento novaForma = formaPagamentoService.salvar(new FormaPagamento());

        assertEquals(formaPagamento.getId(), novaForma.getId());
        assertEquals(formaPagamento.getDescricao(), novaForma.getDescricao());
    }

    @Test
    void aoVerificarDescricaoJaExistenteLancarExcecao() {

        when(formaPagamentoRepository.findByDescricao(any(String.class))).thenReturn(Optional.of(formaPagamento));

        assertThrows(ServiceException.class, () -> formaPagamentoService.salvar(formaPagamento));
    }

    @Test
    void aoAtualizarFromaDePagamentoDeveRetornarFormaAtualizada() {

        FormaPagamento fpAtualizada = FormaPagamento.builder().id(4L).descricao("Ticket alimentação").build();

        when(formaPagamentoRepository.findById(any(Long.class))).thenReturn(Optional.of(formaPagamento));
        when(formaPagamentoRepository.save(any(FormaPagamento.class))).thenReturn(formaPagamento);

        formaPagamentoService.atualizar(fpAtualizada.getId(), fpAtualizada);

        assertEquals(formaPagamento.getDescricao(), fpAtualizada.getDescricao());
    }

    @Test
    void aoAtualizarFormaDePagamentoDeveRetornarExcecaoAoIdentificarDescricaoJaExistente() {

        FormaPagamento fpAtualizada = FormaPagamento.builder().id(6L).descricao("Boleto").build();
        FormaPagamento fp = FormaPagamento.builder().id(2L).descricao("Boleto").build();

        when(formaPagamentoRepository.findByDescricao(any(String.class))).thenReturn(Optional.of(fp));

        assertThrows(ServiceException.class, () -> formaPagamentoService.atualizar(fpAtualizada.getId(), fpAtualizada));
    }
}
