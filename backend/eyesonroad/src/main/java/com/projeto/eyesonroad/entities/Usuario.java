package com.projeto.eyesonroad.entities;

import java.time.LocalDate;
import com.projeto.eyesonroad.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message="O nome é obrigatório.")
    private String nome;

    @Column(nullable = false)
    @NotBlank(message="A senha é obrigatória.")
    @Size(min=5, message="A senha deve ter no mínimo 5 caracteres.")
    private String senha;
    
    @NotBlank(message = "O login é obrigatório")
    @Column(nullable = false, unique = true, length = 50)
    private String login;
    
    @Email(message = "E-mail inválido.")
    @Column(unique = true, length = 120)
    private String email;
    
    @Column(length = 20)
    @NotBlank(message="O telefone é obrigatório.")
    private String telefone;
    
    @NotNull(message = "A data de nascimento é obrigatória.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento; 
    
    @NotBlank(message = "O CPF é obrigatório.")
    @Column(nullable = false, unique = true, length = 11)
    private String cpf;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo_usuario")
    private TipoUsuario tipo;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carro_id", referencedColumnName = "idCarro")
    private Carro carro;

    public Usuario() {
    }

    public Usuario(Long id, String nome, String login, String email, String telefone,
                   String cpf, LocalDate dataNascimento, String senha, TipoUsuario tipo, Carro carro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.senha = senha;
        this.tipo = tipo;
        this.login = login;
        this.carro = carro;
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
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

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	public Carro getCarro() {
		return carro;
	}

	public void setCarro(Carro carro) {
		this.carro = carro;
	}

}