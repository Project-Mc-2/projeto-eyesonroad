package com.projeto.eyesonroad.entities;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.projeto.eyesonroad.validations.annotatios.TelefoneBR;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Evento {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idEvento;
	    
	    @Pattern(
	    		regexp="^\\p{L}]+( [\\p{L}]+)*$",
	    		message="O nome do usuário deve conter apenas letras e espaços.")
	    @NotBlank(message="O nome é obrigatório.")
	    private String nome;


	    @Pattern(regexp="^SENHA-\\d{4}-\\d{4}$",
	    		message="A senha deve conter apenas letras, números e hifen")
	    @Column(unique = true, nullable = false, length = 20)
	    @NotBlank(message="senha é obrigatório.")
	    @Size(min=5, max=20, message="A senha deve ter entre 5 e 20 caracteres.")
	    private String senha;
	    
	    @Email(message = "E-mail inválido.")
	    @Size(max=120, message="E-mail deve ter no máimo 120 caracteres.")
	    @Column(unique = true, length = 120)
	    private String email;
	    
	   @TelefoneBR(message="Telefone deve estar no padrão brasileiro.")
	    @Column(length = 20)
	   @NotBlank(message="O telefone é obrigatório.")
	    private String telefone;
	    
	    @NotNull(message = "A data de nascimento é obrigatória.")
	    @Past(message = "Data de nascimento deve estar no passado.")
	    private LocalDate dataNascimento;
	    
	    @NotBlank(message = "O CPF é obrigatório.")
	    @CPF(message = "CPF inválido.")
	    @Column(nullable = false, unique = true, length = 14)
	    private String cpf;
	    
	    public Evento() {}
	    public Evento(String nome, String senha, LocalDate dataNascimento, String email, String telefone, String cpf) {
	        this.nome = nome;
	        this.senha = senha;
	        this.dataNascimento = dataNascimento;
	        this.email = email;
	        this.telefone = telefone;
	        this.cpf = cpf;
	    }
	    
}
