package com.projeto.eyesonroad.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.projeto.eyesonroad.entities.Alerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findTop200ByUsuarioIdOrderByDataDesc(Long usuarioId);
}
