package com.projeto.eyesonroad.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSensor;

    @NotBlank
    @Pattern(
        regexp = "^[\\p{L}]+( [\\p{L}]+)*$",
        message = "O modelo deve conter apenas letras e espaços."
    )
    private String modelo;

    @Column(unique = true, nullable = false)
    @NotBlank
    @Pattern(
        regexp = "^SERIE-\\d{4}-\\d{4}$",
        message = "O número de série deve seguir o formato SERIE-0000-0000."
    )
    private String numeroSerie;

    public Sensor() {}

    public Sensor(String modelo, String numeroSerie) {
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
    }

	public Long getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(Long idSensor) {
		this.idSensor = idSensor;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
    
    

}