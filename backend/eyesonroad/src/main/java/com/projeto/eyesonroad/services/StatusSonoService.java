package com.projeto.eyesonroad.services;

import com.projeto.eyesonroad.entities.StatusSono;
import com.projeto.eyesonroad.repositories.StatusSonoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusSonoService {

    @Autowired
    private StatusSonoRepository repository;

    public List<StatusSono> listarTodos() {
        return repository.findAll();
    }

    public Optional<StatusSono> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public StatusSono salvar(StatusSono statusSono) {
        return repository.save(statusSono);
    }

    public Optional<StatusSono> atualizar(Long id, StatusSono dadosAtualizados) {
        return repository.findById(id).map(statusExistente -> {
            statusExistente.setCochilo(dadosAtualizados.getCochilo());
            statusExistente.setSono(dadosAtualizados.getSono());
            statusExistente.setDesmaio(dadosAtualizados.getDesmaio());
            return repository.save(statusExistente);
        });
    }

    public boolean deletar(Long id) {
        return repository.findById(id).map(status -> {
            repository.delete(status);
            return true;
        }).orElse(false);
    }
}