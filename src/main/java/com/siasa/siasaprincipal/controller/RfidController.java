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

    //se crea el endpoint get principal de la entidad rfid donde se traen todos los datos de la BD
    @GetMapping
    public ResponseEntity<List<RfidDto>> findAll() {
        //se utiliza el service de la implementacion en el endpoint para no cargar la
        // parte de transferencia con logica de negocio
        return rfidService.findAll();
    }

    //se crea el endpoint post con la ruta /{id} donde se envia el id desde la api al server
    @PostMapping("/{id}")
    public ResponseEntity<RfidDto> findById(@PathVariable String id) {
        //se utiliza el service de la implementacion en el endpoint para no cargar la
        // parte de transferencia con logica de negocio
        return rfidService.findById(id);
    }

}
