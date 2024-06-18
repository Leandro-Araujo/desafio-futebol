package com.araujo.leandro.desafiofinal.partidas.dtos;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PartidaDTO {
    private Long id;
    private Long clubeMandanteId;
    private Long clubeVisitanteId;
    private Integer golsClubeMandante;
    private Integer golsClubeVisitante;
    private Long estadioId;
    private LocalDateTime dataHora;
}
