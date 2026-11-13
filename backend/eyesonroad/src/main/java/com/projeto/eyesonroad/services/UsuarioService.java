package com.projeto.eyesonroad.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.projeto.eyesonroad.entities.Usuario;
import com.projeto.eyesonroad.repositories.UsuarioRepository;
import com.projeto.eyesonroad.enums.TipoUsuario;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }

    public Usuario salvar(Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findByLogin(usuario.getLogin());
        if (usuarioExistente != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe usuário com este login.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        usuario.setId(null);
        usuario.setTipo(TipoUsuario.MOTORISTA);
        if (usuario.getCarro() != null) usuario.getCarro().setIdCarro(null);

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = buscarPorId(id);
        usuarioExistente.setLogin(usuarioAtualizado.getLogin());
        usuarioExistente.setTipo(usuarioAtualizado.getTipo());

        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isBlank()) {
            String senhaCriptografada = passwordEncoder.encode(usuarioAtualizado.getSenha());
            usuarioExistente.setSenha(senhaCriptografada);
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario autenticar(String login, String senha) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.");
        }

        boolean senhaValida = senha != null && passwordEncoder.matches(senha, usuario.getSenha());
        if (!senhaValida) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.");
        }

        return usuario;
    }
}
