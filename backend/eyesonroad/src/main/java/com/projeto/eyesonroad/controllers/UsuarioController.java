package com.projeto.eyesonroad.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.eyesonroad.entities.Usuario;
import com.projeto.eyesonroad.services.UsuarioService;

import jakarta.validation.Valid;

@RestController // Indica que esta classe responde requisições REST em JSON
@RequestMapping("/usuarios") // Caminho base da API
public class UsuarioController {

    // Injeta automaticamente o service
    @Autowired
    private UsuarioService usuarioService;

    // ==========================================
    // 📌 LISTAR TODOS OS USUÁRIOS
    // GET /usuarios
    // ==========================================
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {

        // Busca todos os usuários
        List<Usuario> usuarios = usuarioService.listarTodos();

        // Retorna status 200 OK + lista
        return ResponseEntity.ok(usuarios);
    }

    // ==========================================
    // 📌 BUSCAR USUÁRIO POR ID
    // GET /usuarios/{id}
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(
            @PathVariable Long id) {

        // Busca usuário pelo ID
        Usuario usuario = usuarioService.buscarPorId(id);

        // Retorna 200 OK
        return ResponseEntity.ok(usuario);
    }

    // ==========================================
    // 📌 CADASTRAR USUÁRIO
    // POST /usuarios
    // ==========================================
    @PostMapping
    public ResponseEntity<Usuario> salvar(
            @Valid @RequestBody Usuario usuario) {

        // Salva usuário
        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        // Retorna 201 CREATED
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioSalvo);
    }

    // ==========================================
    // 📌 ATUALIZAR USUÁRIO
    // PUT /usuarios/{id}
    // ==========================================
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuarioAtualizado) {

        // Atualiza usuário
        Usuario usuario = usuarioService.atualizar(id, usuarioAtualizado);

        // Retorna 200 OK
        return ResponseEntity.ok(usuario);
    }

    // ==========================================
    // 📌 DELETAR USUÁRIO
    // DELETE /usuarios/{id}
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id) {

        // Remove usuário
        usuarioService.deletar(id);

        // Retorna 204 NO CONTENT
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // 📌 LOGIN DO USUÁRIO
    // POST /usuarios/login
    // ==========================================
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(
            @RequestBody Usuario usuario) {

        // Autentica usuário
        Usuario usuarioAutenticado =
                usuarioService.autenticar(
                        usuario.getLogin(),
                        usuario.getSenha()
                );

        // Retorna 200 OK
        return ResponseEntity.ok(usuarioAutenticado);
    }
}