package com.projeto.eyesonroad.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.projeto.eyesonroad.entities.Usuario;
import com.projeto.eyesonroad.repositories.UsuarioRepository;

public class UsuarioService {

    // Injeta automaticamente o repositório
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Responsável por criptografar e validar senhas
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ================================
    // 📌 LISTAR TODOS OS USUÁRIOS
    // ================================
    public List<Usuario> listarTodos() {

        // Busca todos os usuários no banco
        return usuarioRepository.findAll();
    }

    // ================================
    // 📌 BUSCAR USUÁRIO POR ID
    // ================================
    public Usuario buscarPorId(Long id) {

        // Optional evita NullPointerException
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado."));
    }

    // ================================
    // 📌 SALVAR USUÁRIO
    // ================================
    public Usuario salvar(Usuario usuario) {

        // Verifica se já existe usuário com o mesmo login
        Usuario usuarioExistente = usuarioRepository.findByLogin(usuario.getLogin());

        // Se encontrou um usuário com esse login
        if (usuarioExistente != null) {
            throw new RuntimeException("Já existe usuário com este login.");
        }

        // 🔐 Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());

        // Substitui a senha original pelo hash criptografado
        usuario.setSenha(senhaCriptografada);

        // Salva no banco
        return usuarioRepository.save(usuario);
    }

    // ================================
    // 📌 ATUALIZAR USUÁRIO
    // ================================
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {

        // Busca o usuário existente no banco
        Usuario usuarioExistente = buscarPorId(id);

        // Atualiza login
        usuarioExistente.setLogin(usuarioAtualizado.getLogin());

        // Atualiza role
        usuarioExistente.setRole(usuarioAtualizado.getRole());

        // ⚠️ Só atualiza senha se ela for enviada
        if (usuarioAtualizado.getSenha() != null
                && !usuarioAtualizado.getSenha().isBlank()) {

            // Criptografa a nova senha
            String senhaCriptografada =
                    passwordEncoder.encode(usuarioAtualizado.getSenha());

            usuarioExistente.setSenha(senhaCriptografada);
        }

        // Salva atualização
        return usuarioRepository.save(usuarioExistente);
    }

    // ================================
    // 📌 DELETAR USUÁRIO
    // ================================
    public void deletar(Long id) {

        // Verifica se o usuário existe antes de deletar
        Usuario usuario = buscarPorId(id);

        // Remove do banco
        usuarioRepository.delete(usuario);
    }

    // ================================
    // 📌 AUTENTICAR LOGIN
    // ================================
    public Usuario autenticar(String login, String senha) {

        // Busca usuário pelo login
        Usuario usuario = usuarioRepository.findByLogin(login);

        // Se não encontrou
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        // 🔐 Compara senha digitada com hash salvo
        boolean senhaValida =
                passwordEncoder.matches(senha, usuario.getSenha());

        // Se senha estiver incorreta
        if (!senhaValida) {
            throw new RuntimeException("Senha inválida.");
        }

        // Retorna usuário autenticado
        return usuario;
    }
}