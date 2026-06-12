package com.projeto.eyesonroad.entities;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.projeto.eyesonroad.validations.annotatios.TelefoneBR;
import com.projeto.eyesonroad.enums.TipoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
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
	   @Pattern(regexp="^\\(?\\d{2}\\)?\\s?9?\\d{4}$",
		message="Telefone inválido. Ex.: (15) 99999-9999"
		)
	   @NotBlank(message="O telefone é obrigatório.")
	    private String telefone;
	    
	    @NotNull(message = "A data de nascimento é obrigatória.")
	    @Past(message = "Data de nascimento deve estar no passado.")
	    private LocalDate dataNascimento;
	    
	    @NotBlank(message = "O CPF é obrigatório.")
	    @CPF(message = "CPF inválido.")
	    @Column(nullable = false, unique = true, length = 14)
	    private String cpf;
	    
	    @Enumerated(EnumType.STRING)
		@Column(nullable = false, name = "tipo_usuario")
		private TipoUsuario tipo;
	    
	  
    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String telefone,
                   String cpf, LocalDate dataNascimento,
                   String senha, TipoUsuario tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipoUsuario(TipoUsuario tipo) {
        this.tipo = tipo;
    }
}