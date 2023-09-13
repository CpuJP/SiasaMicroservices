package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.SalaComputoDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SalaComputoService {

    ResponseEntity<List<SalaComputoDto>> findAll();

    ResponseEntity<Page<SalaComputoDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "10") int pageSize);

    ResponseEntity<List<SalaComputoDto>> findByCodigoUIdCodigoU(@PathVariable String idCodigoU);

    ResponseEntity<SalaComputoDto> createIn(@PathVariable String idRfid, @RequestParam(defaultValue = "GISTFA") String salaDestino);

    ResponseEntity<SalaComputoDto> createOut(@PathVariable String idRfid);

    ResponseEntity<String> existsByCodigoUIdCodigoU(@PathVariable String idCodigoU);
}
