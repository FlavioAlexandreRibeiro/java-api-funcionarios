package com.API_de_Funcionarios.repository;

import com.API_de_Funcionarios.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
