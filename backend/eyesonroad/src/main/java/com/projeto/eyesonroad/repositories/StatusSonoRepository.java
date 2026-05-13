package com.projeto.eyesonroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.eyesonroad.entities.StatusSono;

@Repository
public interface StatusSonoRepository extends JpaRepository<StatusSono, Long> {
}