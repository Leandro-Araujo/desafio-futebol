package com.araujo.leandro.desafiofinal.geral.controllers;

import com.araujo.leandro.desafiofinal.geral.exceptions.ConflitoDadosException;
import com.araujo.leandro.desafiofinal.geral.exceptions.DadoNaoExiste;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeralExceptionHandler {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e){
        System.out.println(e);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Void> handleDataIntegrityViolationException(DataIntegrityViolationException e){
        System.out.println(e);
        if (e.getMessage().contains("nome")) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e){
        System.out.println(e);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflitoDadosException.class)
    public ResponseEntity<Void> handleConflitoDadosException(ConflitoDadosException e){
        System.out.println(e);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DadoNaoExiste.class)
    public ResponseEntity<Void> handleDadoNaoExiste(DadoNaoExiste e){
        System.out.println(e);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
