package com.projeto.eyesonroad.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_alertas", indexes = @Index(columnList = "usuario_id,data"))
public class Alerta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "usuario_id", nullable = false)
    public Long usuarioId;
    @Column(nullable = false, length = 50)
    public String tipo;
    @Column(nullable = false, length = 500)
    public String mensagem;
    @Column(nullable = false)
    public LocalDateTime data;
}
