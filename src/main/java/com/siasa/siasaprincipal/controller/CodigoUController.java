package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.Rfid;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.CodigoURepository;
import com.siasa.siasaprincipal.service.CodigoUService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/codigou")
public class CodigoUController {

    private final CodigoUService codigoUService;
    private final CodigoURepository codigoURepository;

    public CodigoUController(CodigoUService codigoUService, CodigoURepository codigoURepository) {
        this.codigoUService = codigoUService;
        this.codigoURepository = codigoURepository;
    }

    @Operation(summary = "Get all CodigoU's")
    @GetMapping
    public ResponseEntity<List<CodigoUDto>> findAll() {
        return codigoUService.findAll();
    }

    @Operation(summary = "Find CodigoU By Id")
    @PostMapping("/{id}")
    public ResponseEntity<CodigoUDto> findById(@PathVariable String id) {
        return codigoUService.findById(id);
    }

    @Operation(summary = "Find CodigoU By Id of Rfid")
    @GetMapping("/rfid/{idRfid}")
    public ResponseEntity<CodigoUDto> findByRfid(@PathVariable String idRfid) {
        return codigoUService.findByRfid(idRfid);
    }

    @Operation(summary = "Create a new CodigoU")
    @PostMapping
    public ResponseEntity<CodigoUDto> create(@RequestBody CodigoUDto codigoUDto) {
        return codigoUService.create(codigoUDto);
    }

    @Operation(summary = "Update info of CodigoU")
    @PatchMapping("/update/{id}")
    public ResponseEntity<CodigoUDto> update(@PathVariable String id, @RequestBody CodigoUDto codigoUDto) {
        return codigoUService.update(id, codigoUDto);
    }
}

