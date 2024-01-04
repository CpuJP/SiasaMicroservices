package com.siasa.prestamos.controller;

import com.siasa.prestamos.dto.InventarioAudioVisualDTO;
import com.siasa.prestamos.service.InventarioAudioVisualService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audiovisual")
public class InventarioAudioVisualController {

    private final InventarioAudioVisualService inventarioAudioVisualService;

    public InventarioAudioVisualController(InventarioAudioVisualService inventarioAudioVisualService) {
        this.inventarioAudioVisualService = inventarioAudioVisualService;
    }

    @Operation(summary = "Get all AudioVisual's")
    @GetMapping
    public ResponseEntity<List<InventarioAudioVisualDTO>> findAll() {
        return inventarioAudioVisualService.findAll();
    }

    @Operation(summary = "Get all AudioVisual's by Name")
    @GetMapping("/{nombre}")
    public ResponseEntity<List<InventarioAudioVisualDTO>> findByNombre(@PathVariable String nombre) {
        return inventarioAudioVisualService.findByNombre(nombre);
    }

    @Operation(summary = "Create a new Object in AudioVisual")
    @PostMapping
    public ResponseEntity<InventarioAudioVisualDTO> create(@RequestBody InventarioAudioVisualDTO audioVisualDTO) {
        return inventarioAudioVisualService.create(audioVisualDTO);
    }

    @Operation(summary = "Update info to InventarioAudioVisual")
    @PatchMapping("/update/{id}")
    public ResponseEntity<InventarioAudioVisualDTO> update(@PathVariable Integer id, @RequestBody InventarioAudioVisualDTO audioVisualDTO) {
        return inventarioAudioVisualService.update(id, audioVisualDTO);
    }
}
