package com.siasa.prestamos.controller;

import com.siasa.prestamos.dto.PrestamoMaterialDeportivoDTO;
import com.siasa.prestamos.service.PrestamoMaterialDeportivoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/prestamomaterialdeportivo")
public class PrestamoMaterialDeportivoController {

    private final PrestamoMaterialDeportivoService prestamoMaterialDeportivoService;

    public PrestamoMaterialDeportivoController(PrestamoMaterialDeportivoService prestamoMaterialDeportivoService) {
        this.prestamoMaterialDeportivoService = prestamoMaterialDeportivoService;
    }

    @GetMapping
    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findALl() {
        return prestamoMaterialDeportivoService.findALl();
    };

    @GetMapping("/page")
    ResponseEntity<Page<PrestamoMaterialDeportivoDTO>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                                @RequestParam(defaultValue = "10") int pageSize,
                                                                @RequestParam(defaultValue = "idPrestamoMaterialDeportivo") String sortBy,
                                                                @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return prestamoMaterialDeportivoService.findAllP(pageNumber, pageSize, sortBy, sortOrder);
    }

    @PostMapping("/in")
    ResponseEntity<PrestamoMaterialDeportivoDTO> createIn(@RequestParam Integer idInventarioMaterialDeportivo,
                                                          @RequestParam String idRfid,
                                                          @RequestParam(required = false) String nota,
                                                          @RequestParam Integer cantidad) {
        return prestamoMaterialDeportivoService.createIn(idInventarioMaterialDeportivo, idRfid, nota, cantidad);
    }

    @PostMapping("/out")
    ResponseEntity<PrestamoMaterialDeportivoDTO> createOut(@RequestParam Integer idPrestamoMaterialDeportivo,
                                                           @RequestParam(required = false) String observaciones) {
        return prestamoMaterialDeportivoService.createOut(idPrestamoMaterialDeportivo, observaciones);
    }

    @GetMapping("/rfid/{idRfid}")
    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findAllByIdRfid(@PathVariable String idRfid) {
        return prestamoMaterialDeportivoService.findAllByIdRfid(idRfid);
    }

    @GetMapping("/codigou/{idUdec}")
    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findAllByIdUdec(@PathVariable String idUdec) {
        return prestamoMaterialDeportivoService.findAllByIdUdec(idUdec);
    }

    @GetMapping("/fechaprestamo")
    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByFechaPrestamo(@RequestParam LocalDateTime fechaInicial,
                                                                           @RequestParam LocalDateTime fechaFinal) {
        return prestamoMaterialDeportivoService.findByFechaPrestamo(fechaInicial, fechaFinal);
    }

    @GetMapping("/fechadevolucion")
    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByFechaDevolucion(@RequestParam LocalDateTime fechaInicial,
                                                                             @RequestParam LocalDateTime fechaFinal) {
        return prestamoMaterialDeportivoService.findByFechaDevolucion(fechaInicial, fechaFinal);
    }

    @GetMapping("/devolucionempty")
    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByFechaDevolucionIsEmpty() {
        return prestamoMaterialDeportivoService.findByFechaDevolucionIsEmpty();
    }

    @GetMapping("/inventario/{nombreObjeto}")
    ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByInventarioMaterialDeportivoNombreBeLike(@PathVariable String nombreObjeto) {
        return prestamoMaterialDeportivoService.findByInventarioMaterialDeportivoNombreBeLike(nombreObjeto);
    }
}
