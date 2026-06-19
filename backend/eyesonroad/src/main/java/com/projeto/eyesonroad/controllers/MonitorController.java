package com.projeto.eyesonroad.controllers;

import com.projeto.eyesonroad.entities.Monitor;
import com.projeto.eyesonroad.services.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monitor") 
public class MonitorController {

    @Autowired
    private MonitorService service;

    @GetMapping
    public List<Monitor> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monitor> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(monitor -> ResponseEntity.ok().body(monitor))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Monitor> criar(@RequestBody Monitor novoMonitor) {
        Monitor monitorSalvo = service.salvar(novoMonitor);
        return ResponseEntity.status(HttpStatus.CREATED).body(monitorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Monitor> atualizar(@PathVariable Long id, @RequestBody Monitor dadosAtualizados) {
        return service.atualizar(id, dadosAtualizados)
                .map(monitorAtualizado -> ResponseEntity.ok().body(monitorAtualizado))
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