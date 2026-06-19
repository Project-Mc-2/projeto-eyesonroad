package com.projeto.eyesonroad.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "status_sono")
public class StatusSono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idStatusSono;

    private Boolean cochilo;

    private String sono;

    private Boolean desmaio;

    public StatusSono() {
    }

    public StatusSono(Boolean cochilo, String sono, Boolean desmaio) {
        this.cochilo = cochilo;
        this.sono = sono;
        this.desmaio = desmaio;
    }

    public Long getIdStatusSono() {
        return idStatusSono;
    }

    public void setIdStatusSono(Long idStatusSono) {
        this.idStatusSono = idStatusSono;
    }

    public Boolean getCochilo() {
        return cochilo;
    }

    public void setCochilo(Boolean cochilo) {
        this.cochilo = cochilo;
    }

    public String getSono() {
        return sono;
    }

    public void setSono(String sono) {
        this.sono = sono;
    }

    public Boolean getDesmaio() {
        return desmaio;
    }

    public void setDesmaio(Boolean desmaio) {
        this.desmaio = desmaio;
    }
}