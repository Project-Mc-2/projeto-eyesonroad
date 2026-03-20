package br.com.eyesonroad.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eyesonroad.entities.Usuario;
import br.com.eyesonroad.repositories.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuarioAlterado) {
        Optional<Usuario> existente = buscarPorId(id);
        
        if(existente.isPresent()) {
        	
        	Usuario atualizado = existente.get();
        	
        atualizado.setNome(alunoAlterado.getNome());
        atualizado.setTurma(alunoAlterado.getTurma());
        atualizado.setRa(alunoAlterado.getRa());
    
        if(alunoAlterado.getDocumentos() != null) {
        
        atualizado.setDocumentos(alunoAlterado.getDocumentos());
        
       
    }
        		return repository.save(atualizado);
        		
 }
        

        return null;
   }
    public void deletar(Long id) {
    	repository.deleteById(id);
    }
}