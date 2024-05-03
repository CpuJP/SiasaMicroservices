package com.siasa.principalfailover.service;

import com.siasa.principalfailover.dto.LaboratorioDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

public interface LaboratorioService {

    ResponseEntity<List<LaboratorioDto>> findAll();

    ResponseEntity<Page<LaboratorioDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(defaultValue = "idLaboratorio") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder);

    ResponseEntity<List<LaboratorioDto>> findByCodigoUIdCodigoU(@PathVariable String idCodiogU);

    ResponseEntity<LaboratorioDto> createIn(@PathVariable String idRfid);

    ResponseEntity<LaboratorioDto> createOut(@PathVariable String idRfid);

    ResponseEntity<String> existsByCodigoUIdCodigoU(@PathVariable String idCodigoU);

    ResponseEntity<List<LaboratorioDto>> findByIdRfid(@PathVariable String idRfid);

    ResponseEntity<List<LaboratorioDto>> findByFechaIngreso(@RequestParam LocalDateTime fechaInicial,
                                                            @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<LaboratorioDto>> findByFechaSalida(@RequestParam LocalDateTime fechaInicial,
                                                           @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<LaboratorioDto>> findByIdCodigoUAndFechaIngreso(@RequestParam String idCodigoU,
                                                                        @RequestParam LocalDateTime fechaInicial,
                                                                        @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<LaboratorioDto>> findByIdCodigoUAndFechaSalida(@RequestParam String idCodigoU,
                                                                       @RequestParam LocalDateTime fechaInicial,
                                                                       @RequestParam LocalDateTime fechaFinal);
}
