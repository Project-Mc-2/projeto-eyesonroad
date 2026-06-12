package com.projeto.eyesonroad.controllers;

import com.projeto.eyesonroad.entities.Sensor;
import com.projeto.eyesonroad.services.SensorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensor") 
public class SensorController {

    @Autowired
    private SensorService service;

    @GetMapping
    public List<Sensor> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(sensor -> ResponseEntity.ok().body(sensor))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Sensor> criar(@Valid @RequestBody Sensor novoSensor) {
        Sensor sensorSalvo = service.salvar(novoSensor);
        return ResponseEntity.status(HttpStatus.CREATED).body(sensorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sensor> atualizar(@PathVariable Long id, @Valid @RequestBody Sensor dadosAtualizados) {
        return service.atualizar(id, dadosAtualizados)
                .map(sensorAtualizado -> ResponseEntity.ok().body(sensorAtualizado))
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