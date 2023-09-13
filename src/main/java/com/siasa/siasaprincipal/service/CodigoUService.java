package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CodigoUService {

    ResponseEntity<List<CodigoUDto>> findAll();

    ResponseEntity<CodigoUDto> findById(@PathVariable String id);

    ResponseEntity<CodigoUDto> findByRfid(@PathVariable String idRfid);

    ResponseEntity<CodigoUDto> create(@RequestBody CodigoUDto codigoUDto);

    ResponseEntity<CodigoUDto> update(@PathVariable String id, @RequestBody CodigoUDto codigoUDto);

    ResponseEntity<String> delete(@PathVariable String id);
}
