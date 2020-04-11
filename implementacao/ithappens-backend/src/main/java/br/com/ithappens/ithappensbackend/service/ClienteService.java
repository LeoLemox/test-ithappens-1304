package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Cliente;
import br.com.ithappens.ithappensbackend.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static br.com.ithappens.ithappensbackend.util.CommonUtil.copyValues;

@Service
@AllArgsConstructor
public class ClienteService {

    private ClienteRepository clienteRepository;

    @Transactional
    public Cliente atualizar(Long id, Cliente cliente) {

        if (!StringUtils.isEmpty(cliente.getCpf())) verificarCpf(id, cliente.getCpf());
        return clienteRepository.findById(id)
                .map(clienteSalvo -> {
                    copyValues(cliente, clienteSalvo, "id");
                    return clienteRepository.save(clienteSalvo);
                })
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Transactional
    public Cliente salvar(Cliente cliente) {

        verificarCpf(cliente.getCpf());
        return clienteRepository.save(cliente);
    }

    private void verificarCpf(String cpf) {

        if (clienteRepository.findByCpf(cpf).isPresent())
            throw new ServiceException("CPF já cadastrado.");
    }

    private void verificarCpf(Long id, String cpf) {

        clienteRepository.findByCpf(cpf)
                .ifPresent(cliete -> {
                    if (!cliete.getId().equals(id))
                        throw new ServiceException("CPF já cadastrado.");
                });
    }
}
