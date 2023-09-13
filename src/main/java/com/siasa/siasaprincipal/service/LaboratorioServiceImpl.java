package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.dto.LaboratorioDto;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.Laboratorio;
import com.siasa.siasaprincipal.exception.MessageBadRequestException;
import com.siasa.siasaprincipal.exception.MessageNotContentException;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.CodigoURepository;
import com.siasa.siasaprincipal.repository.LaboratorioRepository;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "laboratorioService")
@Slf4j
public class LaboratorioServiceImpl implements LaboratorioService{

    private final LaboratorioRepository laboratorioRepository;
    private final ModelMapper modelMapper;
    private final CodigoURepository codigoURepository;

    public LaboratorioServiceImpl(LaboratorioRepository laboratorioRepository, ModelMapper modelMapper, CodigoURepository codigoURepository) {
        this.laboratorioRepository = laboratorioRepository;
        this.modelMapper = modelMapper;
        this.codigoURepository = codigoURepository;
    }

    private LaboratorioDto mapToDto(Laboratorio laboratorio) {
        TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
        typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
        TypeMap<Laboratorio, LaboratorioDto> typeMap1 = modelMapper.typeMap(Laboratorio.class, LaboratorioDto.class);
        typeMap1.addMapping(Laboratorio::getCodigoU, LaboratorioDto::setCodigoUDto);
        return typeMap1.map(laboratorio);
    }

    @Override
    public ResponseEntity<List<LaboratorioDto>> findAll() {
        List<Laboratorio> laboratorioList = laboratorioRepository.findAll();
        if (!laboratorioList.isEmpty()) {
            List<LaboratorioDto> laboratorioDtos = laboratorioList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(laboratorioDtos, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla laboratorio");
            throw new MessageNotContentException("No hay datos en la tabla laboratorio");
        }
    }

    @Override
    public ResponseEntity<Page<LaboratorioDto>> findAllP(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Laboratorio> laboratorioPage = laboratorioRepository.findAll(pageable);
        if (laboratorioPage.hasContent()) {
            Page<LaboratorioDto> laboratorioDtoPage = laboratorioPage.map(this::mapToDto);
            return new ResponseEntity<>(laboratorioDtoPage, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla laboratorio");
            throw new MessageNotContentException("No hay datos en la tabla laboratorio");
        }
    }

    @Override
    public ResponseEntity<List<LaboratorioDto>> findByCodigoUIdCodigoU(String idCodiogU) {
        if(!codigoURepository.existsById(idCodiogU)) {
            throw new MessageBadRequestException(String.format("La persona con el código %s no existe en base de datos", idCodiogU));
        }
        List<Laboratorio> laboratorioList = laboratorioRepository.findByCodigoUIdCodigoU(idCodiogU);
        if (!laboratorioList.isEmpty()) {
            List<LaboratorioDto> laboratorioDtos = laboratorioList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(laboratorioDtos, HttpStatus.OK);
        } else {
            log.warn(String.format("La persona con el código %s no registra en ingreso al laboratorio", idCodiogU));
            throw new MessageNotFoundException(String.format("La persona con el código %s no registra en ingreso al laboratorio", idCodiogU));
        }
    }

    @Override
    public ResponseEntity<LaboratorioDto> createIn(String idRfid) {
        Optional<CodigoU> codigoUOptional = Optional.ofNullable(codigoURepository.findByRfidIdRfid(idRfid)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid))));
        if (codigoUOptional.isPresent()) {
            LocalDateTime fechaActual = LocalDateTime.now();
            CodigoU codigoU = codigoUOptional.get();
            Laboratorio laboratorio = Laboratorio.builder()
                    .codigoU(codigoU)
                    .fechaIngreso(fechaActual)
                    .build();
            laboratorioRepository.save(laboratorio);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(laboratorio.getIdLaboratorio())
                    .toUri();
            log.info("Registro de ingreso a Laboratorio creado exitosamente");

            LaboratorioDto laboratorioDto = mapToDto(laboratorio);
            return ResponseEntity.created(location).body(laboratorioDto);
        } else {
            log.warn(String.format("El carnet %s no registra en base de datos", idRfid));
            throw new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid));
        }
    }

    @Override
    public ResponseEntity<LaboratorioDto> createOut(String idRfid) {
        Optional<CodigoU> codigoUOptional = Optional.ofNullable(codigoURepository.findByRfidIdRfid(idRfid)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid))));
        if (codigoUOptional.isPresent()) {
            CodigoU codigoU = codigoUOptional.get();
            Optional<Laboratorio> laboratorio = Optional.ofNullable(laboratorioRepository.findFirstByCodigoUIdCodigoUOrderByFechaIngresoDesc(codigoU.getIdCodigoU())
                    .orElseThrow(() -> new MessageNotFoundException(String.format("No registra ningún ingreso al laboratorio la persona con código de carnet %s", idRfid))));
            if (laboratorio.isPresent()) {
                Laboratorio laboratorio1 = laboratorio.get();

                if (laboratorio1.getFechaSalida() == null) {
                    LocalDateTime fechaActual = LocalDateTime.now();
                    laboratorio1.setFechaSalida(fechaActual);
                    laboratorioRepository.save(laboratorio1);

                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(laboratorio1.getIdLaboratorio())
                            .toUri();
                    log.info("Registro de salida a Laboratorio creado exitosamente");

                    LaboratorioDto laboratorioDto = mapToDto(laboratorio1);
                    return ResponseEntity.created(location).body(laboratorioDto);
                } else {
                    log.warn(String.format("la persona con código %s no ha ingresado antes al laboratorio", codigoU.getIdCodigoU()));
                    throw new MessageBadRequestException(String.format("la persona con código %s no ha ingresado antes al laboratorio", codigoU.getIdCodigoU()));
                }

            } else {
                log.warn(String.format("No registra ningún ingreso al laboratorio la persona con código %s", codigoU.getRfid()));
                throw new MessageBadRequestException(String.format("No registra ningún ingreso al laboratorio la persona con código de carnet %s", idRfid));
            }

        } else {
            log.warn(String.format("El carnet %s no registra en base de datos", idRfid));
            throw new MessageBadRequestException(String.format("El carnet %s no registra en base de datos", idRfid));
        }
    }

    @Override
    public ResponseEntity<String> existsByCodigoUIdCodigoU(String idCodigoU) {
        if(!codigoURepository.existsById(idCodigoU)) {
            throw new MessageBadRequestException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        if (laboratorioRepository.existsByCodigoUIdCodigoU(idCodigoU)) {
            return ResponseEntity.ok(String.format("La persona con código %s si registra ingresos al laboratorio", idCodigoU));
        } else {
            throw new MessageBadRequestException(String.format("La persona con código %s NO registra ingresos al laboratorio", idCodigoU));
        }
    }
}
