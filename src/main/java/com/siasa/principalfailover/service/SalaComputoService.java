package com.siasa.principalfailover.service;

import com.siasa.principalfailover.dto.SalaComputoDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

public interface SalaComputoService {

    ResponseEntity<List<SalaComputoDto>> findAll();

    ResponseEntity<Page<SalaComputoDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(defaultValue = "idSalaComputo") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder);

    ResponseEntity<List<SalaComputoDto>> findByCodigoUIdCodigoU(@PathVariable String idCodigoU);

    ResponseEntity<SalaComputoDto> createIn(@PathVariable String idRfid, @RequestParam(defaultValue = "GISTFA") String salaDestino);

    ResponseEntity<SalaComputoDto> createOut(@PathVariable String idRfid);

    ResponseEntity<String> existsByCodigoUIdCodigoU(@PathVariable String idCodigoU);

    ResponseEntity<List<SalaComputoDto>> findByIdRfid(@PathVariable String idRfid);

    ResponseEntity<List<SalaComputoDto>> findByFechaIngreso(@RequestParam LocalDateTime fechaInicial,
                                                            @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<SalaComputoDto>> findByFechaSalida(@RequestParam LocalDateTime fechaInicial,
                                                           @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<SalaComputoDto>> findByIdCodigoUAndFechaIngreso(@RequestParam String idCodigoU,
                                                                        @RequestParam LocalDateTime fechaInicial,
                                                                        @RequestParam LocalDateTime fechaFinal);

    ResponseEntity<List<SalaComputoDto>> findByIdCodigoUAndFechaSalida(@RequestParam String idCodigoU,
                                                                       @RequestParam LocalDateTime fechaInicial,
                                                                       @RequestParam LocalDateTime fechaFinal);
}
