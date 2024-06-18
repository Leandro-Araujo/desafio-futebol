package com.araujo.leandro.desafiofinal.partidas.models;

import com.araujo.leandro.desafiofinal.clubes.models.Clube;
import com.araujo.leandro.desafiofinal.estadios.models.Estadio;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "partida")
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clube_mandante_id", nullable = false)
    private Clube clubeMandante;

    @ManyToOne
    @JoinColumn(name = "clube_visitante_id", nullable = false)
    private Clube clubeVisitante;

    @Column(name = "gols_clube_mandante", nullable = false)
    private Integer golsClubeMandante;

    @Column(name = "gols_clube_visitante", nullable = false)
    private Integer golsClubeVisitante;

    @ManyToOne
    @JoinColumn(name = "estadio_id", nullable = false)
    private Estadio estadio;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public Partida() {
    }

    public Partida(Clube clubeMandante, Clube clubeVisitante, Integer golsClubeMandante, Integer golsClubeVisitante, Estadio estadio, LocalDateTime dataHora) {
        this.setClubeMandante(clubeMandante);
        this.setClubeVisitante(clubeVisitante);
        this.clubeVisitante = clubeVisitante;
        this.setGolsClubeMandante(golsClubeMandante);
        this.setGolsClubeVisitante(golsClubeVisitante);
        this.estadio = estadio;
        this.setDataHora(dataHora);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clube getClubeMandante() {
        return clubeMandante;
    }

    public void setClubeMandante(Clube clubeMandante) {
        if (this.clubeVisitante != null && clubeMandante.equals(this.clubeVisitante))
            throw new IllegalArgumentException("Clube mandante não pode ser igual ao clube visitante");
        this.clubeMandante = clubeMandante;
    }

    public Clube getClubeVisitante() {
        return clubeVisitante;
    }

    public void setClubeVisitante(Clube clubeVisitante) {
        if (this.clubeMandante != null && clubeVisitante.equals(this.clubeMandante))
            throw new IllegalArgumentException("Clube visitante não pode ser igual ao clube mandante");
        this.clubeVisitante = clubeVisitante;
    }

    public Integer getGolsClubeMandante() {
        return golsClubeMandante;
    }

    public void setGolsClubeMandante(Integer golsClubeMandante) {
        if (golsClubeMandante < 0)
            throw new IllegalArgumentException("Quantidade de gols do clube mandante não pode ser negativa");
        this.golsClubeMandante = golsClubeMandante;
    }

    public Integer getGolsClubeVisitante() {
        return golsClubeVisitante;
    }

    public void setGolsClubeVisitante(Integer golsClubeVisitante) {
        if (golsClubeVisitante < 0)
            throw new IllegalArgumentException("Quantidade de gols do clube visitante não pode ser negativa");
        this.golsClubeVisitante = golsClubeVisitante;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        if (dataHora.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("Data e hora da partida não pode ser futura");
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", clubeMandante=" + clubeMandante +
                ", clubeVisitante=" + clubeVisitante +
                ", golsClubeMandante=" + golsClubeMandante +
                ", golsClubeVisitante=" + golsClubeVisitante +
                ", estadio=" + estadio +
                ", dataHora=" + dataHora +
                '}';
    }
}
