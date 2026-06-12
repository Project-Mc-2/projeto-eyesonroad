package com.projeto.eyesonroad.services;

import com.projeto.eyesonroad.entities.Evento;
import com.projeto.eyesonroad.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository repository;

    public List<Evento> listarTodos() {
        return repository.findAll();
    }

    public Optional<Evento> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Evento salvar(Evento evento) {
        return repository.save(evento);
    }

    public Optional<Evento> atualizar(Long id, Evento dadosAtualizados) {
        return repository.findById(id).map(eventoExistente -> {

        	eventoExistente.setDia(dadosAtualizados.getdataHora());
            eventoExistente.setAcao(dadosAtualizados.getAcao());
            eventoExistente.setSensor(dadosAtualizados.getSensor());
            eventoExistente.setTipoUsuario(dadosAtualizados.getUsuario());
            return repository.save(eventoExistente);
        });
    }

    public boolean deletar(Long id) {
        return repository.findById(id).map(evento -> {
            repository.delete(evento);
            return true;
        }).orElse(false);
    }
}