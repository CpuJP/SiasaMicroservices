package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.InventarioAudioVisualDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface InventarioAudioVisualService {

    ResponseEntity<List<InventarioAudioVisualDTO>> findAll();

    ResponseEntity<Page<InventarioAudioVisualDTO>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                            @RequestParam(defaultValue = "10") int pageSize,
                                                            @RequestParam(defaultValue = "idAudioVisual") String sortBy,
                                                            @RequestParam(defaultValue = "asc") String sortOrder);

    ResponseEntity<List<InventarioAudioVisualDTO>> findByNombre(@PathVariable String nombre);

    ResponseEntity<InventarioAudioVisualDTO> create(@RequestBody InventarioAudioVisualDTO audioVisualDTO);

    ResponseEntity<InventarioAudioVisualDTO> update(@PathVariable Integer id, @RequestBody InventarioAudioVisualDTO audioVisualDTO);
}
