package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.InventarioMaterialDeportivoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface InventarioMaterialDeportivoService {

    ResponseEntity<List<InventarioMaterialDeportivoDTO>> findAll();


    ResponseEntity<List<InventarioMaterialDeportivoDTO>> findByNombre(@PathVariable String nombre);

    ResponseEntity<InventarioMaterialDeportivoDTO> create(@RequestBody InventarioMaterialDeportivoDTO materialDeportivoDTO);

    ResponseEntity<InventarioMaterialDeportivoDTO> updtae(@PathVariable Integer id, @RequestBody InventarioMaterialDeportivoDTO materialDeportivoDTO);
}
