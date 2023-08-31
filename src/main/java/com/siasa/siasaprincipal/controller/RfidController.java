package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.RfidDto;
import com.siasa.siasaprincipal.service.RfidService;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.Operation;
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

    //se crea el endpoint get principal de la entidad rfid donde se traen todos los datos de la BD
    @Operation(summary = "Get all Rfid's")
    @GetMapping
    public ResponseEntity<List<RfidDto>> findAll() {
        //se utiliza el service de la implementacion en el endpoint para no cargar la
        // parte de transferencia con logica de negocio
        return rfidService.findAll();
    }

    //se crea el endpoint post con la ruta /{id} donde se envia el id desde la api al server
    @Operation(summary = "Find Rfid by Id")
    @PostMapping("/{id}")
    public ResponseEntity<RfidDto> findById(@PathVariable String id) {
        //se utiliza el service de la implementacion en el endpoint para no cargar la
        // parte de transferencia con logica de negocio
        return rfidService.findById(id);
    }

    // Se crea el endpoint post principal para la creción de nuevos datos en BD de
    // la entidad Rfid
    @Operation(summary = "Create a new Rfid")
    @PostMapping
    public ResponseEntity<RfidDto> create(@RequestBody RfidDto rfidDto) {
        return rfidService.create(rfidDto);
    }

    // Se crea el endpoint delete para borrar un carnet ya existente en la base de datos.
    @Operation(summary = "Delete a Rfid by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        return rfidService.delete(id);
    }
}
