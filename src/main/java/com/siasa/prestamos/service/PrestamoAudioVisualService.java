package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.PrestamoAudioVisualDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoAudioVisualService {

    ResponseEntity<List<PrestamoAudioVisualDTO>> findAll();

    ResponseEntity<Page<PrestamoAudioVisualDTO>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(defaultValue = "idPrestamoAudioVisual") String sortBy,
                                                          @RequestParam(defaultValue = "asc") String sortOrder);

    ResponseEntity<PrestamoAudioVisualDTO> createIn(@RequestParam Integer idInventarioAudioVisual,
                                                    @RequestParam String idRfid,
                                                    @RequestParam(required = false) String nota,
                                                    @RequestParam Integer cantidad);

    ResponseEntity<PrestamoAudioVisualDTO> createOut(@RequestParam Integer idPrestamoAudioVisual,
                                                     @RequestParam(required = false) String observaciones);

    ResponseEntity<List<PrestamoAudioVisualDTO>> findAllByIdRfid(@PathVariable String idRfid);

    ResponseEntity<List<PrestamoAudioVisualDTO>> findAllByIdUdec(@PathVariable String idUdec);

    ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaPrestamo(@RequestParam LocalDateTime fechaInicial,
                                                                     @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaDevolucion(@RequestParam LocalDateTime fechaInicial,
                                                                       @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaDevolucionIsEmpty();

    ResponseEntity<List<PrestamoAudioVisualDTO>> findByInventarioAudioVisualNombreBeLike(@PathVariable String nombreObjeto);
}
