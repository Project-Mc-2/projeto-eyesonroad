package com.projeto.eyesonroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.eyesonroad.entities.Monitor;

@Repository
public interface MonitorRepository extends JpaRepository<Monitor, Long> {
}