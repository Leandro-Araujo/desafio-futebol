package com.araujo.leandro.desafiofinal.clubes.controllers;

import com.araujo.leandro.desafiofinal.clubes.dtos.ClubeDTO;
import com.araujo.leandro.desafiofinal.clubes.services.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubes")
public class ClubeController {
    private final ClubeService clubeService;
    @Autowired
    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping
    public ResponseEntity<Void> cadastrarClube(@RequestBody ClubeDTO clubeDTO){
        System.out.println("Clube cadastrado com sucesso!");
        clubeService.cadastrarClube(clubeDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editarClube(@PathVariable Long id, @RequestBody ClubeDTO clubeDTO){
        System.out.println("Clube editado com sucesso!");
        clubeService.editarClube(id, clubeDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarClube(@PathVariable Long id){
        System.out.println("Clube inativado com sucesso!");
        clubeService.inativarClube(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubeDTO> buscarClube(@PathVariable Long id){
        ClubeDTO clubeDTO = clubeService.buscarClube(id);

        return new ResponseEntity<>(clubeDTO, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Page<ClubeDTO>> listarClubes(
            @RequestParam(defaultValue = "") String nome,
            @RequestParam(defaultValue = "") String estado,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            @RequestParam(defaultValue = "nome") String ordenacao,
            @RequestParam(defaultValue = "asc") String direcao
    ){
        Page<ClubeDTO> clubes = clubeService.listarClubes(nome, estado, ativo, pagina, tamanho, ordenacao, direcao);

        return new ResponseEntity<>(clubes, HttpStatus.OK);
    }
}
