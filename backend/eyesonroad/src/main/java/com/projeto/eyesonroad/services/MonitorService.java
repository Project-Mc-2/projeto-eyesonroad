package com.projeto.eyesonroad.services;

import com.projeto.eyesonroad.entities.Monitor;
import com.projeto.eyesonroad.repositories.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonitorService {

    @Autowired
    private MonitorRepository repository;

    public List<Monitor> listarTodos() {
        return repository.findAll();
    }

    public Optional<Monitor> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Monitor salvar(Monitor monitor) {
        return repository.save(monitor);
    }

    public Optional<Monitor> atualizar(Long id, Monitor dadosAtualizados) {
        return repository.findById(id).map(monitorExistente -> {
            monitorExistente.setDia(dadosAtualizados.getDia());
            monitorExistente.setHora(dadosAtualizados.getHora());
            monitorExistente.setUsuario(dadosAtualizados.getUsuario());
            monitorExistente.setCarro(dadosAtualizados.getCarro());
            monitorExistente.setSensor(dadosAtualizados.getSensor());
            monitorExistente.setStatusSono(dadosAtualizados.getStatusSono());
            return repository.save(monitorExistente);
        });
    }

    public boolean deletar(Long id) {
        return repository.findById(id).map(monitor -> {
            repository.delete(monitor);
            return true;
        }).orElse(false);
    }
}