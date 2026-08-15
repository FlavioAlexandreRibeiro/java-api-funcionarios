package com.API_de_Funcionarios.repository;

import com.API_de_Funcionarios.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}
