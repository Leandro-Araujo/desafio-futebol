package com.araujo.leandro.desafiofinal.partidas.controllers;

import com.araujo.leandro.desafiofinal.partidas.dtos.PartidaDTO;
import com.araujo.leandro.desafiofinal.partidas.dtos.PartidaResponseDTO;
import com.araujo.leandro.desafiofinal.partidas.services.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partidas")
public class PartidaController {
    private final PartidaService partidaService;

    @Autowired
    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarPartida(@RequestBody PartidaDTO partidaDTO){
        partidaService.cadastrarPartida(partidaDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editarPartida(@PathVariable Long id, @RequestBody PartidaDTO partidaDTO){
        partidaService.editarPartida(id, partidaDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPartida(@PathVariable Long id){
        partidaService.removerPartida(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> buscarPartida(@PathVariable Long id){
        PartidaResponseDTO partidaResponseDTO = partidaService.buscarPartida(id);
        return new ResponseEntity<>(partidaResponseDTO, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Page<PartidaResponseDTO>> listarPartidas(@RequestParam(required = false) String nomeClube,
                                                                  @RequestParam(required = false) String nomeEstadio,
                                                                  @RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer tamanhoPagina,
                                                                  @RequestParam(defaultValue = "clubeMandante.nome") String ordenacao,
                                                                  @RequestParam(defaultValue = "asc") String direcao){
        Page<PartidaResponseDTO> partidas = partidaService.listarPartidas(nomeClube, nomeEstadio, page, tamanhoPagina, ordenacao, direcao);
        return new ResponseEntity<>(partidas, HttpStatus.OK);
    }



}
