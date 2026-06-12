package com.projeto.eyesonroad.services;

import com.projeto.eyesonroad.entities.Endereco;
import com.projeto.eyesonroad.repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository repository;

    public List<Endereco> listarTodos() {
        return repository.findAll();
    }

    public Optional<Endereco> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Endereco salvar(Endereco endereco) {
        return repository.save(endereco);
    }

    public Optional<Endereco> atualizar(Long id, Endereco dadosAtualizados) {
        return repository.findById(id).map(enderecoExistente -> {
            enderecoExistente.setNumero(dadosAtualizados.getNumero());
            enderecoExistente.setCidade(dadosAtualizados.getCidade());
            enderecoExistente.setLogradouro(dadosAtualizados.getLogradouro());
            enderecoExistente.setBairro(dadosAtualizados.getBairro());
            enderecoExistente.setEstado(dadosAtualizados.getEstado());
            return repository.save(enderecoExistente);
        });
    }

    public boolean deletar(Long id) {
        return repository.findById(id).map(endereco -> {
            repository.delete(endereco);
            return true;
        }).orElse(false);
    }
}