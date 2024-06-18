package com.araujo.leandro.desafiofinal.estadios.controllers;

import com.araujo.leandro.desafiofinal.estadios.dtos.EstadioDTO;
import com.araujo.leandro.desafiofinal.estadios.services.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estadios")
public class EstadioController {
    private final EstadioService estadioService;

    @Autowired
    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarEstadio(@RequestBody EstadioDTO estadioDTO) {
        System.out.println("Estadio cadastrado com sucesso!");
        estadioService.cadastrarEstadio(estadioDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarEstadio(@PathVariable Long id, @RequestBody EstadioDTO estadioDTO){
        estadioService.atualizarEstadio(id, estadioDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> buscarEstadio(@PathVariable Long id){
        EstadioDTO estadioDTO = estadioService.buscarEstadio(id);
        return new ResponseEntity<>(estadioDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EstadioDTO>> listarEstadios(
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanhoPagina,
            @RequestParam(defaultValue = "nome") String ordenacao,
            @RequestParam(defaultValue = "asc") String direcao
    ){

        List<EstadioDTO> estadios = estadioService.listarEstadios(pagina, tamanhoPagina, ordenacao, direcao);
        return new ResponseEntity<>(estadios, HttpStatus.OK);
    }
}
