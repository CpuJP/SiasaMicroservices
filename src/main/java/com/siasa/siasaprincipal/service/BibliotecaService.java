package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.BibliotecaDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BibliotecaService {

    ResponseEntity<List<BibliotecaDto>> findAll();

    ResponseEntity<Page<BibliotecaDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                                 @RequestParam(defaultValue = "10") int pageSize);

    ResponseEntity<List<BibliotecaDto>> findByCodigoUIdCodigoU(@PathVariable String idCodigoU);

    ResponseEntity<BibliotecaDto> create(@PathVariable String idRfid);

    ResponseEntity<String> existsByCodigoUIdCodigoU(@PathVariable String idCodigoU);
}