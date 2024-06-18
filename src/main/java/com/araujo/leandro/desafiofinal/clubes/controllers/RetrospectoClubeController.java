package com.araujo.leandro.desafiofinal.clubes.controllers;

import com.araujo.leandro.desafiofinal.clubes.dtos.ClubeDTO;
import com.araujo.leandro.desafiofinal.clubes.dtos.RetrospectoClubeDTO;
import com.araujo.leandro.desafiofinal.clubes.dtos.RetrospectoPartidasClubesDTO;
import com.araujo.leandro.desafiofinal.clubes.dtos.RetrospectoPartidasEntreClubesDTO;
import com.araujo.leandro.desafiofinal.clubes.services.ClubeRetrospectoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clubes")
public class RetrospectoClubeController {
    private final ClubeRetrospectoService clubeRetrospectoService;

    public RetrospectoClubeController(ClubeRetrospectoService clubeRetrospectoService) {
        this.clubeRetrospectoService = clubeRetrospectoService;
    }

    @GetMapping("/{id}/retrospecto")
    public ResponseEntity<RetrospectoClubeDTO>  buscarRetrospectoClube(@PathVariable Long id){
        RetrospectoClubeDTO retrospectoClubeDTO = clubeRetrospectoService.buscarRetrospectoClube(id);
        return new ResponseEntity<>(retrospectoClubeDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}/retrospecto/adversarios")
    public ResponseEntity<List<RetrospectoPartidasClubesDTO>> buscarRetrospectoAdversarios(@PathVariable Long id){
        List<RetrospectoPartidasClubesDTO> retrospecto = clubeRetrospectoService.buscarRetrospectoAdversarios(id);
        return new ResponseEntity<>(retrospecto, HttpStatus.OK);
    }

    @GetMapping("/clube/{id1}/adversario/{id2}/retrospecto")
    public ResponseEntity<RetrospectoPartidasEntreClubesDTO> buscarRetrospectoEntreClubes(@PathVariable Long id1, @PathVariable Long id2){
        RetrospectoPartidasEntreClubesDTO retro = clubeRetrospectoService.buscarRetrospectoEntreClubes(id1, id2);
        return new ResponseEntity<>(retro, HttpStatus.OK);
    }


}
