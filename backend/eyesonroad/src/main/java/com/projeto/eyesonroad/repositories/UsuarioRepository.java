package com.projeto.eyesonroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.eyesonroad.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	// SELECT * FROM tb_usuario WHERE login = login;
	Usuario findByLogin(String Login);
	
	// SELECT 1 FROM tb_usuario WHERE login = login LIMIT 1;
	boolean existsByLogin(String login);

}
