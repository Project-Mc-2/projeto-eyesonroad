package com.projeto.eyesonroad.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tb_atividades")
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAtividade;

    @NotNull(message = "A data e hora de início são obrigatórias.")
    @Column(nullable = false)
    private LocalDateTime dtLigado;

    @NotNull(message = "A data e hora de desligamento são obrigatórias.")
    @Column(nullable = false)
    private LocalDateTime dtDesligado;

    public Atividade() {
    }

    public Atividade(LocalDateTime dtLigado, LocalDateTime dtDesligado) {
        this.dtLigado = dtLigado;
        this.dtDesligado = dtDesligado;
    }

    public Long getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(Long idAtividade) {
        this.idAtividade = idAtividade;
    }

    public LocalDateTime getDtLigado() {
        return dtLigado;
    }

    public void setDtLigado(LocalDateTime dtLigado) {
        this.dtLigado = dtLigado;
    }

    public LocalDateTime getDtDesligado() {
        return dtDesligado;
    }

    public void setDtDesligado(LocalDateTime dtDesligado) {
        this.dtDesligado = dtDesligado;
    }
}