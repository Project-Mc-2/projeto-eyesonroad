package com.projeto.eyesonroad.controllers;

import com.projeto.eyesonroad.entities.StatusSono;
import com.projeto.eyesonroad.repositories.StatusSonoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/status-sono")
public class StatusSonoController {

    @Autowired
    private StatusSonoRepository repository;

    @GetMapping
    public List<StatusSono> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatusSono> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(status -> ResponseEntity.ok().body(status))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StatusSono> criar(@RequestBody StatusSono novoStatus) {
        StatusSono statusSalvo = repository.save(novoStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(statusSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatusSono> atualizar(@PathVariable Long id, @RequestBody StatusSono dadosAtualizados) {
        return repository.findById(id)
                .map(statusExistente -> {
                    statusExistente.setCochilo(dadosAtualizados.getCochilo());
                    statusExistente.setSono(dadosAtualizados.getSono());
                    statusExistente.setDesmaio(dadosAtualizados.getDesmaio());
                    
                    StatusSono atualizado = repository.save(statusExistente);
                    return ResponseEntity.ok().body(atualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(status -> {
                    repository.delete(status);
                    return ResponseEntity.noContent().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }
}