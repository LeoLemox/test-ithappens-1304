package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.FormaPagamento;
import br.com.ithappens.ithappensbackend.repository.FormaPagamentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.ithappens.ithappensbackend.util.CommonUtil.copyValues;

@Service
@AllArgsConstructor
public class FormaPagamentoService {

    private FormaPagamentoRepository formaPagamentoRepository;

    @Transactional
    public FormaPagamento atualizar(Long id, FormaPagamento formaPagamento) {

        verificarDescricao(formaPagamento.getDescricao());
        return formaPagamentoRepository.findById(id)
                .map(formaPagSalva -> {
                    copyValues(formaPagamento, formaPagSalva, "id");
                    return formaPagamentoRepository.save(formaPagSalva);
                })
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Transactional
    public FormaPagamento salvar(FormaPagamento formaPagamento) {

        verificarDescricao(formaPagamento.getDescricao());
        return formaPagamentoRepository.save(formaPagamento);
    }

    private void verificarDescricao(String descricao) {

        if (formaPagamentoRepository.findByDescricao(descricao).isPresent())
            throw new ServiceException("Descrição já cadastrada.");
    }
}
