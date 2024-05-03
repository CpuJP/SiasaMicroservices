package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.dto.SalaComputoDto;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.SalaComputo;
import com.siasa.siasaprincipal.exception.MessageBadRequestException;
import com.siasa.siasaprincipal.exception.MessageConflictException;
import com.siasa.siasaprincipal.exception.MessageNotContentException;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.CodigoURepository;
import com.siasa.siasaprincipal.repository.RfidRepository;
import com.siasa.siasaprincipal.repository.SalaComputoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
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

@Service(value = "salaComputoService")
@Slf4j
public class SalaComputoServiceImpl implements SalaComputoService{
    private final RfidRepository rfidRepository;

    private final SalaComputoRepository salaComputoRepository;
    private final ModelMapper modelMapper;
    private final CodigoURepository codigoURepository;


    public SalaComputoServiceImpl(SalaComputoRepository salaComputoRepository, ModelMapper modelMapper, CodigoURepository codigoURepository,
                                  RfidRepository rfidRepository) {
        this.salaComputoRepository = salaComputoRepository;
        this.modelMapper = modelMapper;
        this.codigoURepository = codigoURepository;
        this.rfidRepository = rfidRepository;
    }

    private SalaComputoDto mapToDto(SalaComputo salaComputo) {
        TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
        typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
        TypeMap<SalaComputo, SalaComputoDto> typeMap1 = modelMapper.typeMap(SalaComputo.class, SalaComputoDto.class);
        typeMap1.addMapping(SalaComputo::getCodigoU, SalaComputoDto::setCodigoUDto);
        return typeMap1.map(salaComputo);
    }

    @Override
    @Cacheable(value = "salaComputo", key = "'findAll'")
    public ResponseEntity<List<SalaComputoDto>> findAll() {
        List<SalaComputo> salaComputoList = salaComputoRepository.findAll();
        if (!salaComputoList.isEmpty()) {
            List<SalaComputoDto> salaComputoDtos = salaComputoList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(salaComputoDtos, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla sala cómputo");
            throw new MessageNotFoundException("No hay datos en la tabla sala cómputo");
        }
    }

    @Override
    @Cacheable(value = "salaComputo", key = "'findAllP' + #pageNumber + #pageSize + #sortBy + #sortOrder")
    public ResponseEntity<Page<SalaComputoDto>> findAllP(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<SalaComputo> salaComputoPage = salaComputoRepository.findAll(pageable);
        if (salaComputoPage.hasContent()) {
            Page<SalaComputoDto> salaComputoDtoPage = salaComputoPage.map(this::mapToDto);
            return new ResponseEntity<>(salaComputoDtoPage, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla sala cómputo");
            throw new MessageNotFoundException("No hay datos en la tabla sala cómputo");
        }
    }

    @Override
    @Cacheable(value = "salaComputo", key = "'findByCodigoUIdCodigoU' + #idCodigoU")
    public ResponseEntity<List<SalaComputoDto>> findByCodigoUIdCodigoU(String idCodigoU) {
        if (!codigoURepository.existsById(idCodigoU)) {
            throw new MessageBadRequestException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        List<SalaComputo> salaComputoList = salaComputoRepository.findByCodigoUIdCodigoU(idCodigoU);
        if (!salaComputoList.isEmpty()) {
            List<SalaComputoDto> salaComputoDtos = salaComputoList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(salaComputoDtos, HttpStatus.OK);
        } else {
            log.warn(String.format("La persona con el código %s no registra en ingreso al laboratorio", idCodigoU));
            throw new MessageNotFoundException(String.format("La persona con el código %s no registra en ingreso al laboratorio", idCodigoU));
        }
    }

    @Override
    @CacheEvict(value = "salaComputo", allEntries = true)
    public ResponseEntity<SalaComputoDto> createIn(String idRfid, String salaDestino) {
        Optional<CodigoU> codigoUOptional = Optional.ofNullable(codigoURepository.findByRfidIdRfid(idRfid)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid))));
        if (codigoUOptional.isPresent()) {
            LocalDateTime fechaActual = LocalDateTime.now();
            CodigoU codigoU = codigoUOptional.get();
            SalaComputo salaComputo = SalaComputo.builder()
                    .codigoU(codigoU)
                    .fechaIngreso(fechaActual)
                    .salaIngreso(salaDestino)
                    .build();
            salaComputoRepository.save(salaComputo);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(salaComputo.getIdSalaComputo())
                    .toUri();
            log.info("Registro de ingreso a Sala de Cómputo creado exitosamente");

            SalaComputoDto salaComputoDto = mapToDto(salaComputo);
            return ResponseEntity.created(location).body(salaComputoDto);
        } else {
            log.warn(String.format("El carnet %s no registra en base de datos", idRfid));
            throw new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid));
        }
    }

    @Override
    @CacheEvict(value = "salaComputo", allEntries = true)
    public ResponseEntity<SalaComputoDto> createOut(String idRfid) {
        CodigoU codigoU = codigoURepository.findByRfidIdRfid(idRfid)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid)));
        SalaComputo salaComputo = salaComputoRepository.findFirstByCodigoUIdCodigoUOrderByFechaIngresoDesc(codigoU.getIdCodigoU())
                .orElseThrow(() -> new MessageBadRequestException(String.format("La persona con código %s no ha ingresado antes al laboratorio", codigoU.getIdCodigoU())));

        if (salaComputo.getFechaSalida() != null) {
            log.warn(String.format("La persona con código %s no ha ingresado antes al laboratorio", codigoU.getIdCodigoU()));
            throw new MessageConflictException(String.format("La persona con código %s no ha ingresado antes al laboratorio", codigoU.getIdCodigoU()));
        }

        LocalDateTime fechaActual = LocalDateTime.now();
        salaComputo.setFechaSalida(fechaActual);
        salaComputoRepository.save(salaComputo);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salaComputo.getIdSalaComputo())
                .toUri();
        log.info("Registro de salida a Sala de Cómputo creado exitosamente");

        SalaComputoDto salaComputoDto = mapToDto(salaComputo);
        return ResponseEntity.created(location).body(salaComputoDto);
    }

    @Override
    @Cacheable(value = "salaComputo", key = "'existsByCodigoUIdCodigoU' + #idCodigoU")
    public ResponseEntity<String> existsByCodigoUIdCodigoU(String idCodigoU) {
        if (!codigoURepository.existsById(idCodigoU)) {
            throw new MessageNotFoundException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        if (salaComputoRepository.existsByCodigoUIdCodigoU(idCodigoU)) {
            return ResponseEntity.ok(String.format("La persona con código %s si registra ingreso a la sala de cómputo", idCodigoU));
        } else {
            throw new MessageNotFoundException(String.format("La persona con código %s NO registra ingresos a la sala de cómputo", idCodigoU));
        }
    }

    @Override
    @Cacheable(value = "salaComputo", key = "'findByIdRfid' + #idRfid")
    public ResponseEntity<List<SalaComputoDto>> findByIdRfid(String idRfid) {
        if (!rfidRepository.existsById(idRfid)) {
            throw new MessageBadRequestException(String.format("La persona con el idRfid %s no registra en base de datos", idRfid));
        }
        List<SalaComputo> salaComputos = salaComputoRepository.findSalaComputosByCodigoURfidIdRfid(idRfid);
        if (!salaComputos.isEmpty()) {
            List<SalaComputoDto> salaComputoDtos = salaComputos.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(salaComputoDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos para el usuario con IdRfid %s", idRfid));
        }
    }

    @Override
    @Cacheable(value = "salaComputo", key = "'findByFechaIngreso' + #fechaInicial + #fechaFinal")
    public ResponseEntity<List<SalaComputoDto>> findByFechaIngreso(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        List<SalaComputo> salaComputos = salaComputoRepository.findSalaComputosByFechaIngresoBetween(fechaInicial, fechaFinal);
        if (!salaComputos.isEmpty()) {
            List<SalaComputoDto> salaComputoDtos = salaComputos.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(salaComputoDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }

    @Override
    @Cacheable(value = "salaComputo", key = "'findByFechaSalida' + #fechaInicial + #fechaFinal")
    public ResponseEntity<List<SalaComputoDto>> findByFechaSalida(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        List<SalaComputo> salaComputos = salaComputoRepository.findSalaComputosByFechaSalidaBetween(fechaInicial, fechaFinal);
        if (!salaComputos.isEmpty()) {
            List<SalaComputoDto> salaComputoDtos = salaComputos.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(salaComputoDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }

    @Override
    @Cacheable(value = "salaComputo", key = "'findByIdCodigoUAndFechaIngreso' + #idCodigoU + #fechaInicial + #fechaFinal")
    public ResponseEntity<List<SalaComputoDto>> findByIdCodigoUAndFechaIngreso(String idCodigoU, LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        if (!codigoURepository.existsById(idCodigoU)) {
            throw new MessageBadRequestException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        List<SalaComputo> salaComputos = salaComputoRepository.findSalaComputosByCodigoUIdCodigoUAndFechaIngresoBetween(idCodigoU, fechaInicial, fechaFinal);
        if (!salaComputos.isEmpty()) {
            List<SalaComputoDto> salaComputoDtos = salaComputos.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(salaComputoDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos para el usuario con ID UDEC %s en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", idCodigoU, fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }

    @Override
    @Cacheable(value = "salaComputo", key = "'findByIdCodigoUAndFechaSalida' + #idCodigoU + #fechaInicial + #fechaFinal")
    public ResponseEntity<List<SalaComputoDto>> findByIdCodigoUAndFechaSalida(String idCodigoU, LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        if (!codigoURepository.existsById(idCodigoU)) {
            throw new MessageBadRequestException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        List<SalaComputo> salaComputos = salaComputoRepository.findSalaComputosByCodigoUIdCodigoUAndFechaSalidaBetween(idCodigoU, fechaInicial, fechaFinal);
        if (!salaComputos.isEmpty()) {
            List<SalaComputoDto> salaComputoDtos = salaComputos.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(salaComputoDtos, HttpStatus.OK);
        } else {
            throw new MessageNotFoundException(String.format("NO hay registro de ingresos para el usuario con ID UDEC %s en el rango de fecha declaradas, desde: %tF %tR, hasta: %tF %tR", idCodigoU, fechaInicial, fechaInicial, fechaFinal, fechaFinal));
        }
    }
}
