package com.siasa.prestamos.controller;

import com.siasa.prestamos.dto.PrestamoAudioVisualDTO;
import com.siasa.prestamos.service.PrestamoAudioVisualService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/prestamoaudiovisual")
public class PrestamoAudioVisualController {

    private final PrestamoAudioVisualService prestamoAudioVisualService;

    public PrestamoAudioVisualController(PrestamoAudioVisualService prestamoAudioVisualService) {
        this.prestamoAudioVisualService = prestamoAudioVisualService;
    }

    @GetMapping
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findAll() {
        return prestamoAudioVisualService.findAll();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<PrestamoAudioVisualDTO>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                                 @RequestParam(defaultValue = "idPrestamoAudioVisual") String sortBy,
                                                                 @RequestParam(defaultValue = "asc") String sortOrder) {
        return prestamoAudioVisualService.findAllP(pageNumber, pageSize, sortBy, sortOrder);
    }

    @PostMapping("/in")
    public ResponseEntity<PrestamoAudioVisualDTO> createIn(@RequestParam Integer idInventarioAudioVisual,
                                                           @RequestParam String idRfid,
                                                           @RequestParam(required = false) String nota,
                                                           @RequestParam Integer cantidad) {
        return prestamoAudioVisualService.createIn(idInventarioAudioVisual, idRfid, nota, cantidad);
    }

    @PostMapping("/out")
    public ResponseEntity<PrestamoAudioVisualDTO> createOut (@RequestParam Integer idPrestamoAudioVisual,
                                                             @RequestParam(required = false) String observaciones) {
        return prestamoAudioVisualService.createOut(idPrestamoAudioVisual, observaciones);
    }

    @GetMapping("/rfid/{idRfid}")
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByIdRfid(@PathVariable String idRfid) {
        return prestamoAudioVisualService.findAllByIdRfid(idRfid);
    }

    @GetMapping("/codigou/{idUdec}")
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByIdUdec(@PathVariable String idUdec) {
        return prestamoAudioVisualService.findAllByIdUdec(idUdec);
    }

    @GetMapping("/fechaprestamo")
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaPrestamo(@RequestParam LocalDateTime fechaInicial,
                                                                            @RequestParam LocalDateTime fechaFinal) {
        return prestamoAudioVisualService.findByFechaPrestamo(fechaInicial, fechaFinal);
    }

    @GetMapping("/fechadevolucion")
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaDevolucion(@RequestParam LocalDateTime fechaInicial,
                                                                            @RequestParam LocalDateTime fechaFinal) {
        return prestamoAudioVisualService.findByFechaDevolucion(fechaInicial, fechaFinal);
    }

    @GetMapping("/devolucionempty")
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaDevolucionIsEmpty() {
        return prestamoAudioVisualService.findByFechaDevolucionIsEmpty();
    }

    @GetMapping("/inventario/{nombreObjeto}")
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByInventarioAudioVisualNombreBeLike(@PathVariable String nombreObjeto) {
        return prestamoAudioVisualService.findByInventarioAudioVisualNombreBeLike(nombreObjeto);
    }
}
