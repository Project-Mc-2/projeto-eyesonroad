package com.projeto.eyesonroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.eyesonroad.entities.Atividade;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
}