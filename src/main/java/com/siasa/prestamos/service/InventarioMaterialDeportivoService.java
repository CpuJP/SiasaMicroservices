package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.InventarioAudioVisualDTO;
import com.siasa.prestamos.dto.InventarioMaterialDeportivoDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface InventarioMaterialDeportivoService {

    ResponseEntity<List<InventarioMaterialDeportivoDTO>> findAll();

    ResponseEntity<Page<InventarioMaterialDeportivoDTO>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(defaultValue = "idMaterialDeportivo") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String sortOrder);


    ResponseEntity<List<InventarioMaterialDeportivoDTO>> findByNombre(@PathVariable String nombre);

    ResponseEntity<InventarioMaterialDeportivoDTO> create(@RequestBody InventarioMaterialDeportivoDTO materialDeportivoDTO);

    ResponseEntity<InventarioMaterialDeportivoDTO> updtae(@PathVariable Integer id, @RequestBody InventarioMaterialDeportivoDTO materialDeportivoDTO);
}
