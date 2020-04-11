package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Cliente;
import br.com.ithappens.ithappensbackend.repository.ClienteRepository;
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
public class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @InjectMocks
    ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void init() {
        cliente = Cliente.builder().id(9L).nome("José da Silva").cpf("255.567.480-20").build();
    }

    @Test
    void aoSalvarClienteDeveRetornarClienteSalvo() {

        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente novoCliente = clienteService.salvar(new Cliente());

        assertEquals(cliente.getId(), novoCliente.getId());
        assertEquals(cliente.getCpf(), novoCliente.getCpf());
    }

    @Test
    void aoVerificarCpfJaExistenteLancarExcecao() {

        when(clienteRepository.findByCpf(any(String.class))).thenReturn(Optional.of(cliente));

        assertThrows(ServiceException.class, () -> clienteService.salvar(cliente));
    }

    @Test
    void aoAtualizarClienteDeveRetornarClienteAtualizado() {

        Cliente clienteAtualizado = Cliente.builder().id(9L).nome("José Oliveira da Silva").cpf("255.567.480-20").build();

        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        clienteService.atualizar(clienteAtualizado.getId(), clienteAtualizado);

        assertEquals(cliente.getNome(), clienteAtualizado.getNome());
    }

    @Test
    void aoAtualizarClienteDeveRetornarExcecaoAoIdentificarCpfJaExistenteParaOutroCliente() {

        Cliente clienteAtualizado = Cliente.builder().id(9L).nome("José da Silva").cpf("437.974.240-78").build();
        Cliente clienteCpf = Cliente.builder().id(7L).nome("José Oliveira Ramos").cpf("437.974.240-78").build();

        when(clienteRepository.findByCpf(any(String.class))).thenReturn(Optional.of(clienteCpf));

        assertThrows(ServiceException.class, () -> clienteService.atualizar(clienteAtualizado.getId(), clienteAtualizado));
    }
}
