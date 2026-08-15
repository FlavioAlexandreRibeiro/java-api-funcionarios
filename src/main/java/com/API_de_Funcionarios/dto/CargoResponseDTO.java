package com.API_de_Funcionarios.dto;

import com.API_de_Funcionarios.entity.Cargo;

public record CargoResponseDTO(

        Long id,
        String nome,
        String descricao
) {

    public static CargoResponseDTO mapearParaDto(Cargo cargo) {

        return new CargoResponseDTO(
                cargo.getId(),
                cargo.getNome(),
                cargo.getDescriao()
        );
    }


}
