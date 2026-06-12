package com.projeto.eyesonroad.controllers;

import com.projeto.eyesonroad.entities.Atividade;
import com.projeto.eyesonroad.services.AtividadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atividade")
public class AtividadeController {

    @Autowired
    private AtividadeService service;

    @GetMapping
    public List<Atividade> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atividade> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(atividade -> ResponseEntity.ok().body(atividade))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Atividade> criar(@Valid @RequestBody Atividade novaAtividade) {
        Atividade atividadeSalva = service.salvar(novaAtividade);
        return ResponseEntity.status(HttpStatus.CREATED).body(atividadeSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atividade> atualizar(@PathVariable Long id, @Valid @RequestBody Atividade dadosAtualizados) {
        return service.atualizar(id, dadosAtualizados)
                .map(atividadeAtualizada -> ResponseEntity.ok().body(atividadeAtualizada))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (service.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}