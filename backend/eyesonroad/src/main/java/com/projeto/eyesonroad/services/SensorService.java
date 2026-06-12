package com.projeto.eyesonroad.services;

import com.projeto.eyesonroad.entities.Sensor;
import com.projeto.eyesonroad.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorService {

    @Autowired
    private SensorRepository repository;

    public List<Sensor> listarTodos() {
        return repository.findAll();
    }

    public Optional<Sensor> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Sensor salvar(Sensor sensor) {
        return repository.save(sensor);
    }

    public Optional<Sensor> atualizar(Long id, Sensor dadosAtualizados) {
        return repository.findById(id).map(sensorExistente -> {
            sensorExistente.setModelo(dadosAtualizados.getModelo());
            sensorExistente.setNumeroSerie(dadosAtualizados.getNumeroSerie());
            return repository.save(sensorExistente);
        });
    }

    public boolean deletar(Long id) {
        return repository.findById(id).map(sensor -> {
            repository.delete(sensor);
            return true;
        }).orElse(false);
    }
}