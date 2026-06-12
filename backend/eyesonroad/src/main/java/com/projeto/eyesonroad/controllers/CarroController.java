package com.projeto.eyesonroad.controllers;

import java.util.List;
import java.util.Optional;

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

import com.projeto.eyesonroad.entities.Carro;
import com.projeto.eyesonroad.services.CarroService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/carro)
@CrossOrigin("*")
public class CarroController {


    @Autowired
    private CarroService service;

    @GetMapping
    public ResponseEntity<List<Carro>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> buscar(@PathVariable Long id) {
        Optional<Carro> carro = service.buscarPorId(id);

    if(carro != null) {
    	return ResponseEntity.ok(carro.get());
    }
    
    return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Carro> criar(@Valid @RequestBody Carro carro) {
    	Carro novoCarro = service.salvar(carro);
    	
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCarro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carro> atualizar(@PathVariable Long id, @Valid @RequestBody Carro carro) {
    	Carro carroAtualizado = service.atualizar(id, carro);
        
        if(carroAtualizado != null) {
        	return ResponseEntity.ok(carroAtualizado);
        }
        return ResponseEntity.notFound().build();
        	
        }
  
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluir(@PathVariable Long id) {
    	Optional<Carro> carro = service.buscarPorId(id);   
    	
    	if(carro.isPresent()) {
    		service.deletar(id);
    		
    		return ResponseEntity.status(HttpStatus.OK) // 200 (ok ou null)
    				.body("Sucesso: O aluno foi excluido permanentemente!");
    	}
    	
    	return ResponseEntity.status(HttpStatus.NOT_FOUND) // 404
    			.body("Erro: Não foi possível deletar. O aluno com ID " + id + " não foi encontrado");
    	}
    }