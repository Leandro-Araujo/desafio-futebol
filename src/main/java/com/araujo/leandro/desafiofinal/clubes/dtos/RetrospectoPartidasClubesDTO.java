package com.araujo.leandro.desafiofinal.clubes.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RetrospectoPartidasClubesDTO {
    private Long idAdversario;
    private String nomeAdversario;
    private RetrospectoClubeDTO retrospectoClubeDTO;
}
