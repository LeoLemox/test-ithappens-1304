package br.com.ithappens.ithappensbackend.service;

import br.com.ithappens.ithappensbackend.exception.ServiceException;
import br.com.ithappens.ithappensbackend.model.Usuario;
import br.com.ithappens.ithappensbackend.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        verificarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    private void verificarEmail(String email) {
        if (usuarioRepository.findByEmail(email).isPresent()) throw new ServiceException("Email já cadastrado.");
    }
}
