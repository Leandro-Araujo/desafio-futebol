package com.araujo.leandro.desafiofinal.partidas.dtos;

import com.araujo.leandro.desafiofinal.clubes.dtos.ClubeDTO;
import com.araujo.leandro.desafiofinal.clubes.models.Clube;
import com.araujo.leandro.desafiofinal.estadios.dtos.EstadioDTO;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PartidaResponseDTO {
    private Long id;
    private ClubeDTO clubeMandante;
    private ClubeDTO clubeVisitante;
    private Integer golsClubeMandante;
    private Integer golsClubeVisitante;
    private EstadioDTO estadio;
    private LocalDateTime dataHora;
}
