package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.InventarioAudioVisualDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface InventarioAudioVisualService {

    ResponseEntity<List<InventarioAudioVisualDTO>> findAll();

    ResponseEntity<List<InventarioAudioVisualDTO>> findByNombre(@PathVariable String nombre);

    ResponseEntity<InventarioAudioVisualDTO> create(@RequestBody InventarioAudioVisualDTO audioVisualDTO);

    ResponseEntity<InventarioAudioVisualDTO> update(@PathVariable Integer id, @RequestBody InventarioAudioVisualDTO audioVisualDTO);
}
