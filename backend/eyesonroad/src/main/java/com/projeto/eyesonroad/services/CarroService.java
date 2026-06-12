package com.projeto.eyesonroad.services;

import com.projeto.eyesonroad.entities.Carro;
import com.projeto.eyesonroad.repositories.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarroService {

    @Autowired
    private CarroRepository repository;

    public List<Carro> listarTodos() {
        return repository.findAll();
    }

    public Optional<Carro> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Carro salvar(Carro carro) {
        return repository.save(carro);
    }

    public Optional<Carro> atualizar(Long id, Carro dadosAtualizados) {
        return repository.findById(id).map(carroExistente -> {
            carroExistente.setModelo(dadosAtualizados.getModelo());
            carroExistente.setMarca(dadosAtualizados.getMarca());
            carroExistente.setPlaca(dadosAtualizados.getPlaca());
            carroExistente.setAno(dadosAtualizados.getAno());
            return repository.save(carroExistente);
        });
    }

    public boolean deletar(Long id) {
        return repository.findById(id).map(carro -> {
            repository.delete(carro);
            return true;
        }).orElse(false);
    }
}