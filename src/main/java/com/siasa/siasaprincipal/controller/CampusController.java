package com.siasa.siasaprincipal.controller;

import com.siasa.siasaprincipal.dto.CampusDto;
import com.siasa.siasaprincipal.service.CampusService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campus")
public class CampusController {

    public final CampusService campusService;

    public CampusController(CampusService campusService) {
        this.campusService = campusService;
    }

    @Operation(summary = "Get all Campus access")
    @GetMapping
    public ResponseEntity<List<CampusDto>> findAll() {
        return campusService.findAll();
    }

    @Operation(summary = "Get all Campus access in pages")
    @GetMapping("/page")
    public ResponseEntity<Page<CampusDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                    @RequestParam(defaultValue = "10") int pageSize) {
        return campusService.findAllP(pageNumber, pageSize);
    }

    @Operation(summary = "Get all access to the campus by Id CodigoU")
    @GetMapping("/codigou/{idCodigoU}")
    public ResponseEntity<List<CampusDto>> findByCodigoU(@PathVariable String idCodigoU) {
        return campusService.findByCodigoUIdCodigoU(idCodigoU);
    }

    @Operation(summary = "Get if there is entrance to the campus")
    @GetMapping("/exists/{idCodigoU}")
    public ResponseEntity<String> existsByCodigo(@PathVariable String idCodigoU) {
        return campusService.existsByCodigoUIdCodigoU(idCodigoU);
    }

    @Operation(summary = "Create the Campus access record")
    @PostMapping("{idRfid}")
    public ResponseEntity<CampusDto> create(@PathVariable String idRfid) {
        return campusService.create(idRfid);
    }
}
