	package com.projeto.eyesonroad.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
	@Table(name = "tb_carros")
	public class Carro {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idCarro;
	
	    @NotBlank(message = "O modelo é obrigatório.")
	    private String modelo;
	
	    @NotBlank(message = "A marca é obrigatória.")
	    private String marca;
	
	    @Column(name = "placa")
	    @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$", message = "Placa inválida.") // Exemplo de padrão Mercosul
	    private String placa;
	
	    @NotNull(message = "O ano é obrigatório.")
	    private Integer ano;
	
	    public Carro() {
	    }
	
	    public Carro(String modelo, String marca,
	                 String placa, Integer ano) {
	        this.modelo = modelo;
	        this.marca = marca;
	        this.placa = placa;
	        this.ano = ano;
	   }
	
		public Long getIdCarro() {
			return idCarro;
		}
	
		public void setIdCarro(Long idCarro) {
			this.idCarro = idCarro;
		}
	
		public String getModelo() {
			return modelo;
		}
	
		public void setModelo(String modelo) {
			this.modelo = modelo;
		}
	
		public String getMarca() {
			return marca;
		}
	
		public void setMarca(String marca) {
			this.marca = marca;
		}
	
		public String getPlaca() {
			return placa;
		}
	
		public void setPlaca(String placa) {
			this.placa = placa;
		}
	
		public Integer getAno() {
			return ano;
		}
	
		public void setAno(Integer ano) {
			this.ano = ano;
		}
	    
	
	}