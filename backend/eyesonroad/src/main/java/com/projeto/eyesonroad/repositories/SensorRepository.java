package com.projeto.eyesonroad.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.eyesonroad.entities.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
}