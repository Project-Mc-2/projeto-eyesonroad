package com.projeto.eyesonroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.eyesonroad.entities.Carro;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long> {
}