package com.projeto.eyesonroad.controllers;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.server.ResponseStatusException;

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

@RestController 
@RequestMapping("/usuarios") 
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos(HttpSession session) {

        List<Usuario> usuarios = List.of(usuarioService.buscarPorId(currentUserId(session)));

        return ResponseEntity.ok(usuarios);
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(
            @PathVariable Long id, HttpSession session) {
        requireOwner(id, session);

        Usuario usuario = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> salvar(
            @Valid @RequestBody Usuario usuario) {

        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioSalvo);
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Usuario usuarioAtualizado, HttpSession session) {
        requireOwner(id, session);

        Usuario usuario = usuarioService.atualizar(id, usuarioAtualizado);

        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id, HttpSession session) {
        requireOwner(id, session);

        usuarioService.deletar(id);

        return ResponseEntity.noContent().build();
    }

 
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(
            @RequestBody Usuario usuario, HttpServletRequest request) {

        Usuario usuarioAutenticado =
                usuarioService.autenticar(
                        usuario.getLogin(),
                        usuario.getSenha()
                );

        HttpSession previous = request.getSession(false);
        if (previous != null) previous.invalidate();
        request.getSession(true).setAttribute("eyesUserId", usuarioAutenticado.getId());
        return ResponseEntity.ok(usuarioAutenticado);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.noContent().build();
    }

    private Long currentUserId(HttpSession session) {
        Object value = session.getAttribute("eyesUserId");
        if (!(value instanceof Long id)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return id;
    }

    private void requireOwner(Long id, HttpSession session) {
        if (!id.equals(currentUserId(session))) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
}
