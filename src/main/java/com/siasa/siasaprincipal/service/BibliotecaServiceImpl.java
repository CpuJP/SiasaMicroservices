package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.BibliotecaDto;
import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.entity.Biblioteca;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.exception.MessageBadRequestException;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.BibliotecaRepository;
import com.siasa.siasaprincipal.repository.CodigoURepository;
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

@Service(value = "bibliotecaService")
@Slf4j
public class BibliotecaServiceImpl implements BibliotecaService{

    private final BibliotecaRepository bibliotecaRepository;

    private final ModelMapper modelMapper;

    private final CodigoURepository codigoURepository;

    public BibliotecaServiceImpl(BibliotecaRepository bibliotecaRepository, ModelMapper modelMapper, CodigoURepository codigoURepository) {
        this.bibliotecaRepository = bibliotecaRepository;
        this.modelMapper = modelMapper;
        this.codigoURepository = codigoURepository;
    }

    private BibliotecaDto matToDto(Biblioteca biblioteca) {
        TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
        typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
        TypeMap<Biblioteca, BibliotecaDto> typeMap1 = modelMapper.typeMap(Biblioteca.class, BibliotecaDto.class);
        typeMap1.addMapping(Biblioteca::getCodigoU, BibliotecaDto::setCodigoUDto);
        return typeMap1.map(biblioteca);
    }

    @Override
    public ResponseEntity<List<BibliotecaDto>> findAll() {
        List<Biblioteca> bibliotecas = bibliotecaRepository.findAll();
        if (!bibliotecas.isEmpty()) {
            List<BibliotecaDto> bibliotecaDtos = bibliotecas.stream()
                    .map(this::matToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(bibliotecaDtos, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla biblioteca");
            throw new MessageNotFoundException("No hay datos en la tabla biblioteca");
        }
    }

    @Override
    public ResponseEntity<Page<BibliotecaDto>> findAllP(int pageNumber, int pageSize, String sortBY, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBY).ascending() : Sort.by(sortBY).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Biblioteca> bibliotecaPage = bibliotecaRepository.findAll(pageable);
        if (bibliotecaPage.hasContent()) {
            Page<BibliotecaDto> bibliotecaDtoPage = bibliotecaPage.map(this::matToDto);
            return new ResponseEntity<>(bibliotecaDtoPage, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla campus");
            throw new MessageNotFoundException("No hay datos en la tabla biblioteca");
        }
    }

    @Override
    public ResponseEntity<List<BibliotecaDto>> findByCodigoUIdCodigoU(String idCodigoU) {
        if (!codigoURepository.existsById(idCodigoU)) {
            throw new MessageBadRequestException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
        List<Biblioteca> bibliotecas = bibliotecaRepository.findByCodigoUIdCodigoU(idCodigoU);
        if (!bibliotecas.isEmpty()) {
            List<BibliotecaDto> bibliotecaDtos = bibliotecas.stream()
                    .map(this::matToDto)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(bibliotecaDtos, HttpStatus.OK);
        } else {
            log.warn(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
            throw new MessageNotFoundException(String.format("La persona con el código %s no existe en base de datos", idCodigoU));
        }
    }

    @Override
    public ResponseEntity<BibliotecaDto> create(String idRfid) {
        Optional<CodigoU> codigoUOptional = Optional.ofNullable(codigoURepository.findByRfidIdRfid(idRfid)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El carnet %s no registra en base de datos", idRfid))));
        if (codigoUOptional.isPresent()) {
            LocalDateTime fechaActual = LocalDateTime.now();
            CodigoU codigoU = codigoUOptional.get();
            Biblioteca biblioteca = Biblioteca.builder()
                    .codigoU(codigoU)
                    .fechaIngreso(fechaActual)
                    .build();
            bibliotecaRepository.save(biblioteca);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(biblioteca.getIdBiblioteca())
                    .toUri();

            log.info("Registro de ingreso a Biblioteca creado exitosamente");

            BibliotecaDto bibliotecaDto = matToDto(biblioteca);
            return ResponseEntity.created(location).body(bibliotecaDto);
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
        if (bibliotecaRepository.existsByCodigoUIdCodigoU(idCodigoU)) {
            return ResponseEntity.ok(String.format("La persona con código %s si registra ingresos a la biblioteca", idCodigoU));
        } else {
            throw new MessageNotFoundException(String.format("La persona con código %s NO registra ingresos a la biblioteca", idCodigoU));
        }
    }
}