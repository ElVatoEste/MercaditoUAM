package com.example.mercaditouam.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String problema;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id")
    private Estudiante solicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;  // La publicación asociada al ticket (opcional)

    private Date fechaCreacion;

    @Enumerated(EnumType.STRING)
    private EstadoTicket estado;

    // Enumeración para los posibles estados de un ticket
    public enum EstadoTicket {
        ABIERTO, EN_PROCESO, CERRADO
    }
}
