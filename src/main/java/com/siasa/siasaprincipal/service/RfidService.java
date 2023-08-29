package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.RfidDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//Se crea una interface para abstraccion de los metodos a usar con los repositorio
public interface RfidService {

    ResponseEntity<List<RfidDto>> findAll();

    ResponseEntity<RfidDto> findById(@PathVariable String id);

    ResponseEntity<RfidDto> create(@RequestBody RfidDto rfidDto);

    ResponseEntity<Void> delete(@PathVariable String id);
}
