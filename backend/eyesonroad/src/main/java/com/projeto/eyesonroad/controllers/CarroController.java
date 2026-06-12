package com.projeto.eyesonroad.controllers;

import com.projeto.eyesonroad.entities.Carro;
import com.projeto.eyesonroad.services.CarroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carro") 
public class CarroController {

    @Autowired
    private CarroService service;

    @GetMapping
    public List<Carro> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(carro -> ResponseEntity.ok().body(carro))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Carro> criar(@Valid @RequestBody Carro novoCarro) {
        Carro carroSalvo = service.salvar(novoCarro);
        return ResponseEntity.status(HttpStatus.CREATED).body(carroSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carro> atualizar(@PathVariable Long id, @Valid @RequestBody Carro dadosAtualizados) {
        return service.atualizar(id, dadosAtualizados)
                .map(carroAtualizado -> ResponseEntity.ok().body(carroAtualizado))
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