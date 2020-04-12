package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Filial;
import br.com.ithappens.ithappensbackend.repository.FilialRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static br.com.ithappens.ithappensbackend.util.CommonUtil.copyValues;

@Service
@AllArgsConstructor
public class FilialService {

    private FilialRepository filialRepository;

    @Transactional
    public Filial atualizar(Long id, Filial filial) {

        verificarNome(filial.getNome());
        return filialRepository.findById(id)
                .map(filialSalva -> {
                    copyValues(filial, filialSalva, "id");
                    return filialRepository.save(filialSalva);
                })
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Transactional
    public Filial salvar(Filial filial) {

        this.verificarNome(filial.getNome());
        return filialRepository.save(filial);
    }

    private void verificarNome(String nome) {

        if (filialRepository.findByNome(nome).isPresent())
            throw new ServiceException("Nome já cadastrado.");
    }
}
