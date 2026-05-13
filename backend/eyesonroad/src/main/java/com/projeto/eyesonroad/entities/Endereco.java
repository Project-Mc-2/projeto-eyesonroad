package com.projeto.eyesonroad.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Endereco {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Pattern(
	    		regexp="^\\p{L}]+( [\\p{L}]+)*$",
	    		message="O nome do usuário deve conter apenas letras e espaços.")
	    private String numero;


	    @Column(unique = true, nullable = false, length = 20)
	    @NotBlank(message="A cidade é obrigatória.")
	    private String cidade;
	    
	    @Column(unique = true, nullable = false, length = 20)	
	    @NotBlank(message="Logradouro obrigatório")
	    private String logradouro;
	    
	    public String getLogradouro() {
			return logradouro;
		}
		public void setLogradouro(String logradouro) {
			this.logradouro = logradouro;
		}
		@Column(unique = true, nullable = false, length = 20)
	    @NotBlank(message="A cidade é obrigatória.")
	    private String estado;
	    
	    @Column(unique = true, nullable = false, length = 20)
	    @NotBlank(message="A cidade é obrigatória.")
	    private String bairro;
	    	
	    
	    public Endereco() {}
	    public Endereco(String bairro, String logradouro, String cidade, String numero, String estado) {
	        this.bairro = bairro;
	        this.numero = numero;
	        this.estado = estado;
	        this.cidade = cidade;
	        this.logradouro = logradouro;
	       
	    }
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getNumero() {
			return numero;
		}
		public void setNumero(String numero) {
			this.numero = numero;
		}
		public String getCidade() {
			return cidade;
		}
		public void setCidade(String cidade) {
			this.cidade = cidade;
		}
		public String getEstado() {
			return estado;
		}
		public void setEstado(String estado) {
			this.estado = estado;
		}
		public String getBairro() {
			return bairro;
		}
		public void setBairro(String bairro) {
			this.bairro = bairro;
		}

	    
}
