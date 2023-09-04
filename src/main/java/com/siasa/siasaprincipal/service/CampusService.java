package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CampusDto;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CampusService {

    ResponseEntity<List<CampusDto>> findAll();

    ResponseEntity<Page<CampusDto>> findAllP(@RequestParam(defaultValue = "0") int pageNumber,
                                             @RequestParam(defaultValue = "10") int pageSize);

    ResponseEntity<List<CampusDto>> findByCodigoUIdCodigoU(@PathVariable String idCodigoU);

    ResponseEntity<CampusDto> create(@PathVariable String idRfid);

    ResponseEntity<String> existsByCodigoUIdCodigoU(@PathVariable String idCodigoU);
}
