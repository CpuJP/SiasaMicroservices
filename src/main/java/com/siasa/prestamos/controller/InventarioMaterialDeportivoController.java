package com.siasa.prestamos.controller;

import com.siasa.prestamos.dto.InventarioMaterialDeportivoDTO;
import com.siasa.prestamos.service.InventarioMaterialDeportivoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materialdeportivo")
public class InventarioMaterialDeportivoController {

    private final InventarioMaterialDeportivoService inventarioMaterialDeportivoService;

    public InventarioMaterialDeportivoController(InventarioMaterialDeportivoService inventarioMaterialDeportivoService) {
        this.inventarioMaterialDeportivoService = inventarioMaterialDeportivoService;
    }

    @Operation(summary = "Get all Material Deportivo")
    @GetMapping
    public ResponseEntity<List<InventarioMaterialDeportivoDTO>> findAll() {
        return inventarioMaterialDeportivoService.findAll();
    }

    @Operation(summary = "Get all Material Deportivo by Name")
    @GetMapping("/{nombre}")
    public ResponseEntity<List<InventarioMaterialDeportivoDTO>> findByNombre(@PathVariable String nombre) {
        return inventarioMaterialDeportivoService.findByNombre(nombre);
    }

    @Operation(summary = "Create a new Object in Material Deportivo")
    @PostMapping
    public ResponseEntity<InventarioMaterialDeportivoDTO> create(@RequestBody InventarioMaterialDeportivoDTO materialDeportivoDTO) {
        return inventarioMaterialDeportivoService.create(materialDeportivoDTO);
    }

    @Operation(summary = "Update info to InventarioMaterialDeportivo")
    @PatchMapping("/update/{id}")
    public ResponseEntity<InventarioMaterialDeportivoDTO> update(@PathVariable Integer id, @RequestBody InventarioMaterialDeportivoDTO materialDeportivoDTO) {
        return inventarioMaterialDeportivoService.updtae(id, materialDeportivoDTO);
    }
}
