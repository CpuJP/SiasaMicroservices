package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.RfidDto;
import com.siasa.siasaprincipal.service.RfidService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rfid")
public class RfidController {

    private final RfidService rfidService;

    public RfidController(RfidService rfidService) {
        this.rfidService = rfidService;
    }

    @GetMapping
    public ResponseEntity<List<RfidDto>> findAll() {
        return rfidService.findAll();
    }

    @PostMapping("/{id}")
    public ResponseEntity<RfidDto> findById(@PathVariable String id) {
        return rfidService.findById(id);
    }

}
