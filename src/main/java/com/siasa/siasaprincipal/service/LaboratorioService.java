package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.LaboratorioDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface LaboratorioService {

    ResponseEntity<List<LaboratorioDto>> findAll();

    ResponseEntity<Page<LaboratorioDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "10") int pageSize);

    ResponseEntity<List<LaboratorioDto>> findByCodigoUIdCodigoU(@PathVariable String idCodiogU);

    ResponseEntity<LaboratorioDto> createIn(@PathVariable String idRfid);

    ResponseEntity<LaboratorioDto> createOut(@PathVariable String idRfid);

    ResponseEntity<String> existsByCodigoUIdCodigoU(@PathVariable String idCodigoU);
}