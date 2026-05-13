package com.projeto.eyesonroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.eyesonroad.entities.Evento;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
}