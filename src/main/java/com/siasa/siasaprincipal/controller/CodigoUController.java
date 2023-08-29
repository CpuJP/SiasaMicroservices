package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.Rfid;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.CodigoURepository;
import com.siasa.siasaprincipal.service.CodigoUService;
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

    @GetMapping
    public ResponseEntity<List<CodigoUDto>> findAll() {
        return codigoUService.findAll();
    }

    @PostMapping("/{id}")
    public ResponseEntity<CodigoUDto> findById(@PathVariable String id) {
        return codigoUService.findById(id);
    }

    @GetMapping("/rfid/{idRfid}")
    public ResponseEntity<CodigoUDto> findByRfid(@PathVariable String idRfid) {
        return codigoUService.findByRfid(idRfid);
    }
}

