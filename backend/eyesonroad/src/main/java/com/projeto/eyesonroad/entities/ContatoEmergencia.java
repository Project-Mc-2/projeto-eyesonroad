package com.projeto.eyesonroad.entities;

import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ContatoEmergencia {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idCarro;
	    
	    @Pattern(
	    		regexp="^\\p{L}]+( [\\p{L}]+)*$",
	    		message="O nome do usuário deve conter apenas letras e espaços.")
	    @NotBlank(message="O nome é obrigatório.")
	    private String nome;

	    @Pattern(
	    		regexp="^\\p{L}]+( [\\p{L}]+)*$",
	    		message="A marca deve conter apenas letras e espaços.")
	    @NotBlank(message="A marca é obrigatória")
	    private String marca;

	    @Pattern( regexp = "^[A-Z]{3}-[0-9]{4}$")
	    @NotBlank(message= "A placa do carro é obrigatória.")
	    private String placa;

	    @NotBlank(message= "O ano é obrigatório.")
	    private LocalDate ano;
	    
	    @NotBlank(message= "A cor é obrigatória.")
	    private String cor;
	  
	    public ContatoEmergencia() {}
	    public ContatoEmergencia(String placa, String nome, LocalDate ano, String cor, String marca) {
	        this.marca = marca;
	        this.ano = ano;
	        this.placa = placa;
	        this.cor = cor;
	        this.nome = nome;
	    }
		public Long getIdCarro() {
			return idCarro;
		}
		public void setIdCarro(Long idCarro) {
			this.idCarro = idCarro;
		}
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
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
		public LocalDate getAno() {
			return ano;
		}
		public void setAno(LocalDate ano) {
			this.ano = ano;
		}
		public String getCor() {
			return cor;
		}
		public void setCor(String cor) {
			this.cor = cor;
		}
	    
}