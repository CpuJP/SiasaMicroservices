package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.dto.LaboratorioDto;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.Laboratorio;
import com.siasa.siasaprincipal.exception.MessageBadRequestException;
import com.siasa.siasaprincipal.exception.MessageConflictException;
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
            throw new MessageNotFoundException("No hay datos en la tabla laboratorio");
        }
    }

    @Override
    public ResponseEntity<Page<LaboratorioDto>> findAllP(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Laboratorio> laboratorioPage = laboratorioRepository.findAll(pageable);
        if (laboratorioPage.hasContent()) {
            Page<LaboratorioDto> laboratorioDtoPage = laboratorioPage.map(this::mapToDto);
            return new ResponseEntity<>(laboratorioDtoPage, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla laboratorio");
            throw new MessageNotFoundException("No hay datos en la tabla laboratorio");
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
        CodigoU codigoU = codigoURepository.findByRfidIdRfid(idRfid)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid)));

        Laboratorio laboratorio = laboratorioRepository.findFirstByCodigoUIdCodigoUOrderByFechaIngresoDesc(codigoU.getIdCodigoU())
                .orElseThrow(() -> new MessageBadRequestException(String.format("No registra ningún ingreso al laboratorio la persona con código de carnet %s", idRfid)));

        if (laboratorio.getFechaSalida() != null) {
            log.warn(String.format("La persona con código %s no ha ingresado antes al laboratorio", codigoU.getIdCodigoU()));
            throw new MessageConflictException(String.format("La persona con código %s no ha ingresado antes al laboratorio", codigoU.getIdCodigoU()));
        }

        LocalDateTime fechaActual = LocalDateTime.now();
        laboratorio.setFechaSalida(fechaActual);
        laboratorioRepository.save(laboratorio);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(laboratorio.getIdLaboratorio())
                .toUri();
        log.info("Registro de salida a Laboratorio creado exitosamente");

        LaboratorioDto laboratorioDto = mapToDto(laboratorio);
        return ResponseEntity.created(location).body(laboratorioDto);
    }


    @Override
    public ResponseEntity<String> existsByCodigoUIdCodigoU(String idCodigoU) {
        if(!codigoURepository.existsById(idCodigoU)) {
            throw new MessageNotFoundException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        if (laboratorioRepository.existsByCodigoUIdCodigoU(idCodigoU)) {
            return ResponseEntity.ok(String.format("La persona con código %s si registra ingresos al laboratorio", idCodigoU));
        } else {
            throw new MessageNotFoundException(String.format("La persona con código %s NO registra ingresos al laboratorio", idCodigoU));
        }
    }

    @Override
    public ResponseEntity<List<LaboratorioDto>> findByFechaIngreso(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        List<Laboratorio> laboratorios = laboratorioRepository.findLaboratoriosByFechaIngresoBetween(fechaInicial, fechaFinal);
        if (!laboratorios.isEmpty()) {
            List<LaboratorioDto> laboratorioDtos = laboratorios.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(laboratorioDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }

    @Override
    public ResponseEntity<List<LaboratorioDto>> findByFechaSalida(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        List<Laboratorio> laboratorios = laboratorioRepository.findLaboratoriosByFechaSalidaBetween(fechaInicial, fechaFinal);
        if (!laboratorios.isEmpty()) {
            List<LaboratorioDto> laboratorioDtos = laboratorios.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(laboratorioDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }

    @Override
    public ResponseEntity<List<LaboratorioDto>> findByIdCodigoUAndFechaIngreso(String idCodigoU, LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        if(!codigoURepository.existsById(idCodigoU)) {
            throw new MessageBadRequestException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        List<Laboratorio> laboratorios = laboratorioRepository.findLaboratoriosByCodigoUIdCodigoUAndFechaIngresoBetween(idCodigoU, fechaInicial, fechaFinal);
        if (!laboratorios.isEmpty()) {
            List<LaboratorioDto> laboratorioDtos = laboratorios.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(laboratorioDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos para el usuario con ID UDEC %s en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", idCodigoU, fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }

    @Override
    public ResponseEntity<List<LaboratorioDto>> findByIdCodigoUAndFechaSalida(String idCodigoU, LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        if(!codigoURepository.existsById(idCodigoU)) {
            throw new MessageBadRequestException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        List<Laboratorio> laboratorios = laboratorioRepository.findLaboratoriosByCodigoUIdCodigoUAndFechaSalidaBetween(idCodigoU, fechaInicial, fechaFinal);
        if (!laboratorios.isEmpty()) {
            List<LaboratorioDto> laboratorioDtos = laboratorios.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(laboratorioDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos para el usuario con ID UDEC %s en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", idCodigoU, fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }
}
