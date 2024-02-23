package com.siasa.principalfailover.service;

import com.siasa.principalfailover.dto.CodigoUDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CodigoUService {

    ResponseEntity<List<CodigoUDto>> findAll();

    ResponseEntity<Page<CodigoUDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              @RequestParam(defaultValue = "idCodigoU") String sortBy,
                                              @RequestParam(defaultValue = "asc") String sortOrder);

    ResponseEntity<CodigoUDto> findById(@PathVariable String id);

    ResponseEntity<CodigoUDto> findByRfid(@PathVariable String idRfid);

    ResponseEntity<CodigoUDto> create(@RequestBody CodigoUDto codigoUDto);

    ResponseEntity<CodigoUDto> update(@PathVariable String id, @RequestBody CodigoUDto codigoUDto);

    ResponseEntity<String> delete(@PathVariable String id);
}
