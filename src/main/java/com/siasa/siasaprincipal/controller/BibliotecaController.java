package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.BibliotecaDto;
import com.siasa.siasaprincipal.service.BibliotecaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biblioteca")
@CrossOrigin(origins = "*")
public class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    public BibliotecaController(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }

    @Operation(summary = "Get all Biblioteca access")
    @GetMapping
    public ResponseEntity<List<BibliotecaDto>> findAll() {
        return bibliotecaService.findAll();
    }

    @Operation(summary = "Get all Biblioteca access in pages")
    @GetMapping("/page")
    public ResponseEntity<Page<BibliotecaDto>> findALlP(@RequestParam(defaultValue = "0") int pageNumber,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        return bibliotecaService.findAllP(pageNumber, pageSize);
    }

    @Operation(summary = "Get all access to the campus by Id CodigoU")
    @GetMapping("/codigou/{idCodigoU}")
    public ResponseEntity<List<BibliotecaDto>> findByCodigoU(@PathVariable String idCodigoU) {
        return bibliotecaService.findByCodigoUIdCodigoU(idCodigoU);
    }

    @Operation(summary = "Get if there entrance to the campus")
    @GetMapping("/exists/{idCodigoU}")
    public ResponseEntity<String> existsByCodigoU(@PathVariable String idCodigoU) {
        return bibliotecaService.existsByCodigoUIdCodigoU(idCodigoU);
    }

    @Operation(summary = "Create the Biblioteca access record")
    @PostMapping("{idRfid}")
    public ResponseEntity<BibliotecaDto> create(@PathVariable String idRfid) {
        return bibliotecaService.create(idRfid);
    }
}
