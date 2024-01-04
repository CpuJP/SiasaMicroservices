package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.LaboratorioDto;
import com.siasa.siasaprincipal.entity.Laboratorio;
import com.siasa.siasaprincipal.repository.LaboratorioRepository;
import com.siasa.siasaprincipal.service.LaboratorioService;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/laboratorio")
@CrossOrigin(origins = "*")
public class LaboratorioController {

    public final LaboratorioService laboratorioService;

    public LaboratorioController(LaboratorioService laboratorioService) {
        this.laboratorioService = laboratorioService;
    }

    @Operation(summary = "Get all Laboratorio access")
    @GetMapping
    public ResponseEntity<List<LaboratorioDto>> findAll() {
        return laboratorioService.findAll();
    }

    @Operation(summary = "Get all Laboratorio access in pages")
    @GetMapping("/page")
    public ResponseEntity<Page<LaboratorioDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return laboratorioService.findAllP(pageNumber, pageSize);
    }

    @Operation(summary = "Get all access to the campus by Id CodigoU")
    @GetMapping("/codigou/{idCodigoU}")
    public ResponseEntity<List<LaboratorioDto>> findByCodigoU(@PathVariable String idCodigoU) {
        return laboratorioService.findByCodigoUIdCodigoU(idCodigoU);
    }

    @Operation(summary = "Get if there is entrance to the laboratory")
    @GetMapping("/exists/{idCodigoU}")
    public ResponseEntity<String> existsByCodigoU(@PathVariable String idCodigoU) {
        return laboratorioService.existsByCodigoUIdCodigoU(idCodigoU);
    }

    @Operation(summary = "Create IN the Laboratory access record")
    @PostMapping("{idRfid}")
    public ResponseEntity<LaboratorioDto> createIn(@PathVariable String idRfid) {
        return laboratorioService.createIn(idRfid);
    }

    @Operation(summary = "Create OUT the Laboratory access record")
    @PostMapping("/out/{idRfid}")
    public ResponseEntity<LaboratorioDto> createOut(@PathVariable String idRfid) {
        return laboratorioService.createOut(idRfid);
    }


}
