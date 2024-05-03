package com.siasa.principalfailover.service;

import com.siasa.principalfailover.dto.CampusDto;
import com.siasa.principalfailover.dto.CodigoUDto;
import com.siasa.principalfailover.entity.Campus;
import com.siasa.principalfailover.entity.CodigoU;
import com.siasa.principalfailover.exception.MessageBadRequestException;
import com.siasa.principalfailover.exception.MessageNotContentException;
import com.siasa.principalfailover.exception.MessageNotFoundException;
import com.siasa.principalfailover.repository.CampusRepository;
import com.siasa.principalfailover.repository.CodigoURepository;
import com.siasa.principalfailover.repository.RfidRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "campusService")
@Slf4j
public class CampusServiceImpl implements CampusService {

    private final CampusRepository campusRepository;
    private final ModelMapper modelMapper;
    private final CodigoURepository codigoURepository;
    private final RfidRepository rfidRepository;

    public CampusServiceImpl(CampusRepository campusRepository, ModelMapper modelMapper, CodigoURepository codigoURepository, RfidRepository rfidRepository) {
        this.campusRepository = campusRepository;
        this.modelMapper = modelMapper;
        this.codigoURepository = codigoURepository;
        this.rfidRepository = rfidRepository;
    }

    private CampusDto mapToDto(Campus campus) {
        TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
        typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
        TypeMap<Campus, CampusDto> typeMap1 = modelMapper.typeMap(Campus.class, CampusDto.class);
        typeMap1.addMapping(Campus::getCodigoU, CampusDto::setCodigoUDto);
        return typeMap1.map(campus);
    }

    @Override
    @Cacheable(value = "campus-failover", key = "'findAll'")
    public ResponseEntity<List<CampusDto>> findAll() {
        List<Campus> campusList = campusRepository.findAll();
        if (!campusList.isEmpty()) {
            List<CampusDto> campusDtos = campusList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(campusDtos, HttpStatus.OK);
        } else {
            // Manejo de caso en el que no hay datos en la tabla Campus
            log.warn("No hay datos en la tabla Campus");
            throw new MessageNotFoundException("No hay datos en la tabla Campus");
        }
    }

    @Override
    @Cacheable(value = "campus-failover", key = "'findAllP' + #pageNumber + #pageSize + #sortBY + #sortOrder")
    public ResponseEntity<Page<CampusDto>> findAllP(int pageNumber, int pageSize, String sortBY, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBY).ascending() : Sort.by(sortBY).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Campus> campusPage = campusRepository.findAll(pageable);
        if (campusPage.hasContent()) {
            Page<CampusDto> campusDtoPage = campusPage.map(this::mapToDto);
            return new ResponseEntity<>(campusDtoPage, HttpStatus.OK);
        } else {
            // Manejo de caso en el que no hay datos en la tabla Campus
            log.warn("No hay datos en la tabla Campus");
            throw new MessageNotContentException("No hay datos en la tabla Campus");
        }

    }

    @Override
    @Cacheable(value = "campus-failover", key = "'findByCodigoUIdCodigoU' + #idCodigoU")
    public ResponseEntity<List<CampusDto>> findByCodigoUIdCodigoU(String idCodigoU) {
        if (!codigoURepository.existsById(idCodigoU)) {
            throw new MessageBadRequestException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        List<Campus> campusList = campusRepository.findByCodigoUIdCodigoU(idCodigoU);
        if (!campusList.isEmpty()) {
            List<CampusDto> campusDtos = campusList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(campusDtos, HttpStatus.OK);
        } else {
            log.warn(String.format("La persona con código %s no registra en ingreso al campus", idCodigoU));
            throw new MessageNotFoundException(String.format("La persona con código %s no registra en ingreso al campus", idCodigoU));
        }
    }

    @Override
    @CacheEvict(value = "campus-failover", allEntries = true)
    public ResponseEntity<CampusDto> create(String idRfid) {
        Optional<CodigoU> codigoUOptional = Optional.ofNullable(codigoURepository.findByRfidIdRfid(idRfid)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid))));
        if (codigoUOptional.isPresent()) {
            LocalDateTime fechaActual = LocalDateTime.now();
            CodigoU codigoU = codigoUOptional.get();
            Campus campus = Campus.builder()
                    .codigoU(codigoU)
                    .fechaIngreso(fechaActual)
                    .build();
            campusRepository.save(campus);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(campus.getIdCampus())
                    .toUri();
            log.info("Registro de ingreso a Campus creado existosamente");

            CampusDto campusDto = mapToDto(campus);
            return ResponseEntity.created(location).body(campusDto);
        } else {
            log.warn(String.format("El carnet %s no registra en base de datos", idRfid));
            throw new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid));
        }
    }

    @Override
    @Cacheable(value = "campus-failover", key = "'existsByCodigoUIdCodigoU' + #idCodigoU")
    public ResponseEntity<String> existsByCodigoUIdCodigoU(String idCodigoU) {
        if (!codigoURepository.existsById(idCodigoU)) {
            throw new MessageNotFoundException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        if (campusRepository.existsByCodigoUIdCodigoU(idCodigoU)) {
            return ResponseEntity.ok(String.format("La persona con código %s si registra ingresos al campus", idCodigoU));

        } else {
            throw new MessageNotFoundException(String.format("La persona con código %s NO registra ingresos al campus", idCodigoU));
        }
    }

    @Override
    @Cacheable(value = "campus-failover", key = "'findByIdRfid' + #idRfid")
    public ResponseEntity<List<CampusDto>> findByIdRfid(String idRfid) {
        if (!rfidRepository.existsById(idRfid)) {
            throw new MessageBadRequestException(String.format("La persona co el idRfid %s no registra en base de datos", idRfid));
        }
        List<Campus> campus = campusRepository.findCampusByCodigoURfidIdRfid(idRfid);
        if (!campus.isEmpty()) {
            List<CampusDto> campusDtos = campus.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(campusDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos para el usuario con IdRfid %s", idRfid));
        }
    }

    @Override
    @Cacheable(value = "campus-failover", key = "'findByFechaIngreso' + #fechaInicial + #fechaFinal")
    public ResponseEntity<List<CampusDto>> findByFechaIngreso(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        List<Campus> campusList = campusRepository.findCampusByFechaIngresoBetween(fechaInicial, fechaFinal);
        if (!campusList.isEmpty()) {
            List<CampusDto> campusDtos = campusList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(campusDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }

    @Override
    @Cacheable(value = "campus-failover", key = "'findByIdCodigoUAndFechaIngreso' + #idCodigoU + #fechaInicial + #fechaFinal")
    public ResponseEntity<List<CampusDto>> findByIdCodigoUAndFechaIngreso(String idCodigoU, LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        if (!codigoURepository.existsById(idCodigoU)) {
            throw new MessageNotFoundException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        List<Campus> campusList = campusRepository.findCampusByCodigoUIdCodigoUAndFechaIngresoBetween(idCodigoU, fechaInicial, fechaFinal);
        if (!campusList.isEmpty()) {
            List<CampusDto> campusDtos = campusList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(campusDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos para el usuario con ID UDEC %s en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", idCodigoU, fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }
}
