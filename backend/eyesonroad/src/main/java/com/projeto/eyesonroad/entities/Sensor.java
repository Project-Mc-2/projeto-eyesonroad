package com.projeto.eyesonroad.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Sensor {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idSensor;
	    
	    @Pattern(
	    		regexp="^\\p{L}]+( [\\p{L}]+)*$",
	    		message="O nome do modelo deve conter apenas letras e espaços.")
	    @NotBlank(message="O modelo é obrigatório.")
	    private String modelo;


	    @Pattern(regexp="^SERIE-\\d{4}-\\d{4}$",
	    		message="A senha deve conter apenas letras, números e hifen")
	    @Column(unique = true, nullable = false, length = 20)
	    @NotBlank(message="Número de Série é obrigatório.")
	    @Size(min=5, max=20, message="A senha deve ter entre 5 e 50 caracteres.")
	    private String numeroSerie;
	    
	  
	    public Sensor() {}
	    public Sensor(Long idSensor, String modelo, String numeroSerie) {
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

	    