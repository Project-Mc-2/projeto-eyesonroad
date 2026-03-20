package br.com.eyesonroad.api.entities;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import br.com.eyesonroad.validations.annotations.TelefoneBR;

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

public class Usuario {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    @Pattern(
	    		regexp="^\\p{L}]+( [\\p{L}]+)*$",
	    		message="O nome do aluno deve conter apenas letras e espaços.")
	    @NotBlank(message="O nome é obrigatório.")
	    private String nome;


	    @Pattern(regexp="^SENHA-\\d{4}-\\d{4}$",
	    		message="A senha deve conter apenas letras, números e hifen")
	    @Column(unique = true, nullable = false, length = 20)
	    @NotBlank(message="senha é obrigatório.")
	    @Size(min=5, max=20, message="A senha deve ter entre 5 e 20 caracteres.")
	    private String senha;
	    
	    @Email(message = "E-mail inválido.")
	    @Size(max=120, message="E-mail deve ter no máximo 120 caracteres.")
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
	    
	    public Usuario() {}
	    public Usuario(String nome, String senha, LocalDate dataNascimento, String email, String telefone, String cpf) {
	        this.nome = nome;
	        this.senha = senha;
	        this.dataNascimento = dataNascimento;
	        this.email = email;
	        this.telefone = telefone;
	        this.cpf = cpf;
	    }

	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }
	    public String getCpf() {
			return cpf;
		}
		public void setCpf(String cpf) {
			this.cpf = cpf;
		}
		public String getNome() { return nome; }
	    public void setNome(String nome) { this.nome = nome; }
	    
	    
	   
	    
	    
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
	    
	    
}
