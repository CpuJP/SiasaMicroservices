package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.RfidDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//Se crea una interface para abstraccion de los metodos a usar con los repositorio
public interface RfidService {

    ResponseEntity<List<RfidDto>> findAll();

    ResponseEntity<Page<RfidDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                           @RequestParam(defaultValue = "10") int pageSize);

    ResponseEntity<List<RfidDto>> findRfidWithoutCodigoU();

    ResponseEntity<RfidDto> findById(@PathVariable String id);

    ResponseEntity<RfidDto> create(@RequestBody RfidDto rfidDto);

    ResponseEntity<String> delete(@PathVariable String id);
}
