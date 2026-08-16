package com.API_de_Funcionarios.exception;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErroResponse {

    private int status;
    private String mensagem;
    private LocalDateTime timestamp;
    private Map<String, String> erros;

    public ErroResponse(
            int status,
            String mensagem) {

        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    public ErroResponse(
            int status,
            String mensagem,
            Map<String, String> erros) {

        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
        this.erros = erros;
    }
}