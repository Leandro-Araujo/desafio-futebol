package com.araujo.leandro.desafiofinal.clubes.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RetrospectoClubeDTO {
    private String nomeClube;
    private Long totalVitorias;
    private Long totalEmpates;
    private Long totalDerrotas;
    private Long totalGolsFeitos;
    private Long totalGolsSofridos;
}
