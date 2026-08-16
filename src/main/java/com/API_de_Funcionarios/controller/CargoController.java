package com.API_de_Funcionarios.controller;


import com.API_de_Funcionarios.dto.CargoRequestDTO;
import com.API_de_Funcionarios.dto.CargoResponseDTO;
import com.API_de_Funcionarios.service.CargoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cargo")
@RequiredArgsConstructor
public class CargoController {

    private final CargoService cargoService;

    @PostMapping
    public ResponseEntity<CargoResponseDTO> criar(
            @Valid @RequestBody CargoRequestDTO cargo) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cargoService.cadastrar(cargo));
    }


    @GetMapping
    public Page<CargoResponseDTO> listar(
            @RequestParam(required = false) String nome,
            Pageable pageable) {

        return cargoService.listar(nome, pageable);
    }


    @GetMapping("/{id}")
    public CargoResponseDTO buscarPorId(@PathVariable Long id) {
        return cargoService.buscarPorId(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<CargoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CargoRequestDTO cargo) {

        return ResponseEntity.ok(cargoService.atualizar(id, cargo));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cargoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

