package com.API_de_Funcionarios.dto;

import com.API_de_Funcionarios.entity.Funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioResponseDTO(

        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        BigDecimal salario,
        LocalDate dataAdmissao,
        String cargo

){

    public static FuncionarioResponseDTO mapearParaDto(Funcionario funcionario) {

        return new FuncionarioResponseDTO(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getCpf(),
                funcionario.getEmail(),
                funcionario.getTelefone(),
                funcionario.getSalario(),
                funcionario.getDataAdmissao(),
                funcionario.getCargo().getNome()
        );
    }
}


