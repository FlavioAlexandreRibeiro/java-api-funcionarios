package com.API_de_Funcionarios.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class FuncionarioRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank
    @Size(min = 10, max = 15, message = "Telefone inválido")
    private String telefone;

    @NotNull(message = "O salário é obrigatório")
    @DecimalMin(value = "0.01", message = "O salário deve ser maior que zero")
    private BigDecimal salario;

    @NotNull(message = "A data de admissão é obrigatória")
    @PastOrPresent(message = "A data de admissão não pode ser futura")
    private LocalDate dataAdmissao;

    @NotNull(message = "O cargo é obrigatória")
    private Long cargoId;
}
