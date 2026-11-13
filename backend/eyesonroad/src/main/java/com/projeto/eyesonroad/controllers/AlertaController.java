package com.projeto.eyesonroad.controllers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.projeto.eyesonroad.entities.Alerta;
import com.projeto.eyesonroad.repositories.AlertaRepository;
import com.projeto.eyesonroad.repositories.UsuarioRepository;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {
    private final AlertaRepository alertas;
    private final UsuarioRepository usuarios;
    @Value("${eyesonroad.alert.api-key:}")
    private String apiKey;

    public AlertaController(AlertaRepository alertas, UsuarioRepository usuarios) {
        this.alertas = alertas;
        this.usuarios = usuarios;
    }

    @GetMapping
    public List<Alerta> listar(HttpServletRequest request) {
        var session = request.getSession(false);
        Object id = session == null ? null : session.getAttribute("eyesUserId");
        if (!(id instanceof Long usuarioId)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        List<Alerta> result = alertas.findTop200ByUsuarioIdOrderByDataDesc(usuarioId);
        Collections.reverse(result);
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Long> criar(@RequestHeader(value = "X-API-Key", required = false) String provided,
                                    @RequestBody Alerta alerta) {
        if (apiKey.isBlank() || provided == null ||
            !MessageDigest.isEqual(apiKey.getBytes(StandardCharsets.UTF_8), provided.getBytes(StandardCharsets.UTF_8)))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (alerta.usuarioId == null || !usuarios.existsById(alerta.usuarioId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inválido");
        if (alerta.tipo == null || alerta.tipo.isBlank() || alerta.tipo.length() > 50 ||
            alerta.mensagem == null || alerta.mensagem.isBlank() || alerta.mensagem.length() > 500)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alerta inválido");
        alerta.id = null;
        alerta.data = LocalDateTime.now();
        return Map.of("id", alertas.save(alerta).id);
    }
}
