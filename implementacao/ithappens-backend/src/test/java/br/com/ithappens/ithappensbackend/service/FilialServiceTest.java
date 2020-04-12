package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Filial;
import br.com.ithappens.ithappensbackend.repository.FilialRepository;
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
public class FilialServiceTest {

    @Mock
    FilialRepository filialRepository;

    @InjectMocks
    FilialService filialService;

    private Filial filial;

    @BeforeEach
    void init() {
        filial = Filial.builder().id(6L).nome("Cajazeiras").build();
    }

    @Test
    void aoSalvarFilialDeveRetornarFilialSalva() {

        when(filialRepository.save(any(Filial.class))).thenReturn(filial);

        Filial novaFilial = filialService.salvar(new Filial());

        assertEquals(filial.getId(), novaFilial.getId());
        assertEquals(filial.getNome(), novaFilial.getNome());
    }

    @Test
    void aoVerificarNomeJaExistenteLancarExcecao() {

        when(filialRepository.findByNome(any(String.class))).thenReturn(Optional.of(filial));

        assertThrows(ServiceException.class, () -> filialService.salvar(filial));
    }

    @Test
    void aoAtualizarFilialDeveRetornarFilialAtualizada() {

        Filial filialAtualizada = Filial.builder().id(6L).nome("Filial Cajazeiras").build();

        when(filialRepository.findById(any(Long.class))).thenReturn(Optional.of(filial));
        when(filialRepository.save(any(Filial.class))).thenReturn(filial);

        filialService.atualizar(filialAtualizada.getId(), filialAtualizada);

        assertEquals(filial.getNome(), filialAtualizada.getNome());
    }

    @Test
    void aoAtualizarFilialDeveRetornarExcecaoAoIdentificarNomeJaExistenteParaOutraFilial() {

        Filial filialAtualizada = Filial.builder().id(6L).nome("Filial Bacanga").build();
        Filial filial = Filial.builder().id(1L).nome("Filial Bacanga").build();

        when(filialRepository.findByNome(any(String.class))).thenReturn(Optional.of(filial));

        assertThrows(ServiceException.class, () -> filialService.atualizar(filialAtualizada.getId(), filialAtualizada));
    }
}
