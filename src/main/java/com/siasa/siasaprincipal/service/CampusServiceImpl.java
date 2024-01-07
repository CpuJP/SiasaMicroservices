package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CampusDto;
import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.entity.Campus;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.exception.MessageBadRequestException;
import com.siasa.siasaprincipal.exception.MessageNotContentException;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.CampusRepository;
import com.siasa.siasaprincipal.repository.CodigoURepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class CampusServiceImpl implements CampusService{

    private final CampusRepository campusRepository;
    private final ModelMapper modelMapper;
    private final CodigoURepository codigoURepository;

    public CampusServiceImpl(CampusRepository campusRepository, ModelMapper modelMapper, CodigoURepository codigoURepository) {
        this.campusRepository = campusRepository;
        this.modelMapper = modelMapper;
        this.codigoURepository = codigoURepository;

    }

    private CampusDto mapToDto(Campus campus) {
        TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
        typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
        TypeMap<Campus, CampusDto> typeMap1 = modelMapper.typeMap(Campus.class, CampusDto.class);
        typeMap1.addMapping(Campus::getCodigoU, CampusDto::setCodigoUDto);
        return typeMap1.map(campus);
    }

    @Override
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
    public ResponseEntity<Page<CampusDto>> findAllP(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
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
}
