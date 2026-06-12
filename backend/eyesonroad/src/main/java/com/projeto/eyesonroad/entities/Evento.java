package com.projeto.eyesonroad.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvento;

    @Column(nullable = false)
    private LocalDate dataHora;


    @Column(nullable = false)
    private String acao;

    @ManyToOne
    @JoinColumn(name = "id_sensor")
    private Sensor sensor;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Evento() {
    }

    public Evento(LocalDate dataHora, String acao, Sensor sensor, Usuario usuario) {
        this.dataHora = dataHora;
        this.acao = acao;
        this.sensor = sensor;
        this.usuario = usuario;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
     }

    public LocalDate getdataHora() {
        return dataHora;
    }

    public void setDia(LocalDate dataHora) {
        this.dataHora = dataHora;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    
    public Usuario getUsuario() {
    	return usuario;
    }
    	
    	public void setTipoUsuario(Usuario usuario) {
    	this.usuario = usuario;
    }
}