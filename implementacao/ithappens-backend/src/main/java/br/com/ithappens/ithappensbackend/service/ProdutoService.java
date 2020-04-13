package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Produto;
import br.com.ithappens.ithappensbackend.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static br.com.ithappens.ithappensbackend.util.CommonUtil.copyValues;

@Service
@AllArgsConstructor
public class ProdutoService {

    private ProdutoRepository produtoRepository;

    @Transactional
    public Produto atualizar(Long id, Produto produto) {

        if (!StringUtils.isEmpty(produto.getCodigoBarras())) verificarCodigoBarras(id, produto.getCodigoBarras());

        return produtoRepository.findById(id)
                .map(produtoSalvo -> {
                    copyValues(produto, produtoSalvo, "id");
                    return produtoRepository.save(produtoSalvo);
                })
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @Transactional
    public Produto salvar(Produto produto) {

        verificarCodigoBarras(produto.getCodigoBarras());
        return produtoRepository.save(produto);
    }

    public Page<Produto> buscaAvancada(Long id, String descriaco, String codigoDeBarras, int page, int size) {
        return produtoRepository.search(id, descriaco, codigoDeBarras, PageRequest.of(page, size, Sort.Direction.ASC, "descricao"));
    }

    private void verificarCodigoBarras(String codigoBarras) {

        if (produtoRepository.findByCodigoBarras(codigoBarras).isPresent())
            throw new ServiceException("Código de barras já cadastrado.");
    }

    private void verificarCodigoBarras(Long id, String codigoBarras) {

        produtoRepository.findByCodigoBarras(codigoBarras)
                .ifPresent(produto -> {
                    if (!produto.getId().equals(id))
                        throw new ServiceException("Código de barras já cadastrado.");
                });
    }
}
