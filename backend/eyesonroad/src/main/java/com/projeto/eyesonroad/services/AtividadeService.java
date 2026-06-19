package com.projeto.eyesonroad.services;

import com.projeto.eyesonroad.entities.Atividade;
import com.projeto.eyesonroad.repositories.AtividadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AtividadeService {

    @Autowired
    private AtividadeRepository repository;

    public List<Atividade> listarTodos() {
        return repository.findAll();
    }

    public Optional<Atividade> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Atividade salvar(Atividade atividade) {
        return repository.save(atividade);
    }

    public Optional<Atividade> atualizar(Long id, Atividade dadosAtualizados) {
        return repository.findById(id).map(atividadeExistente -> {
            atividadeExistente.setDtLigado(dadosAtualizados.getDtLigado());
            atividadeExistente.setDtDesligado(dadosAtualizados.getDtDesligado());
            return repository.save(atividadeExistente);
        });
    }

    public boolean deletar(Long id) {
        return repository.findById(id).map(atividade -> {
            repository.delete(atividade);
            return true;
        }).orElse(false);
    }
}