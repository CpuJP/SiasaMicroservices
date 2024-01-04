package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.SalaComputoDto;
import com.siasa.siasaprincipal.service.SalaComputoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salacomputo")
@CrossOrigin(origins = "*")
public class SalaComputoController {

    public final SalaComputoService salaComputoService;

    public SalaComputoController(SalaComputoService salaComputoService) {
        this.salaComputoService = salaComputoService;
    }

    @Operation(summary = "Get all Sala Cómputo access")
    @GetMapping
    public ResponseEntity<List<SalaComputoDto>> findAll() {
        return salaComputoService.findAll();
    }

    @Operation(summary = "Gett all Sala Cómputo access in pages")
    @GetMapping("/page")
    public ResponseEntity<Page<SalaComputoDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return salaComputoService.findAllP(pageNumber, pageSize);
    }

    @Operation(summary = "Get all access to the Sala Cómputo by Id CodigoU")
    @GetMapping("/codigou/{idCodigoU}")
    public ResponseEntity<List<SalaComputoDto>> findByCodigoU(@PathVariable String idCodigoU) {
        return salaComputoService.findByCodigoUIdCodigoU(idCodigoU);
    }

    @Operation(summary = "Get if there is entrance to the Sala Cómputo")
    @GetMapping("/exists/{idCodigoU}")
    public ResponseEntity<String> existsByCodigoU(@PathVariable String idCodigoU) {
        return salaComputoService.existsByCodigoUIdCodigoU(idCodigoU);
    }

    @Operation(summary = "Create IN the Sala Cómputo access record")
    @PostMapping("{idRfid}")
    public ResponseEntity<SalaComputoDto> createIn(@PathVariable String idRfid, @RequestParam(defaultValue = "GISTFA") String salaDestino) {
        return salaComputoService.createIn(idRfid, salaDestino);
    }

    @Operation(summary = "Create OUT the Sala Cómputo access record")
    @PostMapping("/out/{idRfid}")
    public ResponseEntity<SalaComputoDto> createOut(@PathVariable String idRfid) {
        return salaComputoService.createOut(idRfid);
    }
}
