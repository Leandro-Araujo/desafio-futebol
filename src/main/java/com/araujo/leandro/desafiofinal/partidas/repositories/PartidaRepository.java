package com.araujo.leandro.desafiofinal.partidas.repositories;

import com.araujo.leandro.desafiofinal.clubes.models.Clube;
import com.araujo.leandro.desafiofinal.estadios.models.Estadio;
import com.araujo.leandro.desafiofinal.estadios.repositories.EstadioRepository;
import com.araujo.leandro.desafiofinal.partidas.models.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    boolean existsByClubeMandanteOrClubeVisitanteAndDataHoraBetween(Clube clubeMandante, Clube clubeVisitante, LocalDateTime localDateTime, LocalDateTime localDateTime1);
    boolean existsByEstadioAndDataHoraBetween(Estadio estadio, LocalDateTime localDateTime, LocalDateTime localDateTime1);
    List<Partida> findByIdNotAndEstadio(Long id, Estadio estadio);
    List<Partida> findByEstadio(Estadio estadio);
    Page<Partida> findByClubeMandanteNomeContainingOrClubeVisitanteNomeContainingAndEstadioNomeContaining(String nomeClube, String nomeClube1, String nomeEstadio, Pageable pageable);
    Page<Partida> findByClubeMandanteNomeContainingOrClubeVisitanteNomeContaining(String nomeClube, String nomeClube1, Pageable pageable);
    Page<Partida> findByEstadioNomeContaining(String nomeEstadio, Pageable pageable);
    Page<Partida> findAll(Pageable pageable);
    Page<Partida> findByClubeMandanteContainingOrClubeVisitanteContainingOrEstadioContaining(String nomeClube, String nomeClube1, String nomeEstadio, Pageable pageable);
    List<Partida> findByClubeMandanteIdOrClubeVisitanteId(Long id, Long id1);
    List<Partida> findByClubeMandanteIdAndClubeVisitanteIdOrClubeMandanteIdAndClubeVisitanteId(Long id1, Long id2, Long id11, Long id21);
}
