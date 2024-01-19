package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.PrestamoMaterialDeportivoDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

public interface PrestamoMaterialDeportivoService {

    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findALl();

    ResponseEntity<Page<PrestamoMaterialDeportivoDTO>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                                @RequestParam(defaultValue = "10") int pageSize,
                                                                @RequestParam(defaultValue = "idPrestamoMaterialDeportivo") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String sortOrder);

    ResponseEntity<PrestamoMaterialDeportivoDTO> createIn(@RequestParam Integer idInventarioMaterialDeportivo,
                                                          @RequestParam String idRfid,
                                                          @RequestParam(required = false) String nota,
                                                          @RequestParam Integer cantidad);

    ResponseEntity<PrestamoMaterialDeportivoDTO> createOut(@RequestParam Integer idPrestamoMaterialDeportivo,
                                                           @RequestParam(required = false) String observaciones);

    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findAllByIdRfid(@PathVariable String idRfid);

    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findAllByIdUdec(@PathVariable String idUdec);

    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByFechaPrestamo(@RequestParam LocalDateTime fechaInicial,
                                                                           @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByFechaDevolucion(@RequestParam LocalDateTime fechaInicial,
                                                                             @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByFechaDevolucionIsEmpty();

    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByInventarioMaterialDeportivoNombreBeLike(@PathVariable String nombreObjeto);
}
