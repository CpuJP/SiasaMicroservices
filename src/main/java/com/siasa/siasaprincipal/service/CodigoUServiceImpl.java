package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.Rfid;
import com.siasa.siasaprincipal.exception.*;
import com.siasa.siasaprincipal.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "codigoUService")
@Slf4j
public class CodigoUServiceImpl implements CodigoUService{

    private final CodigoURepository codigoURepository;
    private final RfidRepository rfidRepository;

    private final BibliotecaRepository bibliotecaRepository;

    private final CampusRepository campusRepository;

    private final LaboratorioRepository laboratorioRepository;

    private final SalaComputoRepository salaComputoRepository;

    private final ModelMapper modelMapper;

    private final CacheManager cacheManager;


    public CodigoUServiceImpl(CodigoURepository codigoURepository, RfidRepository rfidRepository, BibliotecaRepository bibliotecaRepository, CampusRepository campusRepository, LaboratorioRepository laboratorioRepository, SalaComputoRepository salaComputoRepository, ModelMapper modelMapper, CacheManager cacheManager) {
        this.codigoURepository = codigoURepository;
        this.rfidRepository = rfidRepository;
        this.bibliotecaRepository = bibliotecaRepository;
        this.campusRepository = campusRepository;
        this.laboratorioRepository = laboratorioRepository;
        this.salaComputoRepository = salaComputoRepository;
        this.modelMapper = modelMapper;
        this.cacheManager = cacheManager;
    }

    private void limpiarCaches() {
        String[] cacheNames = {"biblioteca", "rfid", "codigoU", "laboratorio", "salaComputo", "campus"};
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("Cache '{}' borrado exitosamente.", cacheName);
            }
        }
    }

    @Override
    @Cacheable(value = "codigoU", key = "'findAll'")
    public ResponseEntity<List<CodigoUDto>> findAll() {
        List<CodigoU> codigoUS = codigoURepository.findAll();
        if (!codigoUS.isEmpty()) {
            List<CodigoUDto> codigoUDtos = codigoUS.stream()
                    .map(codigoU -> {
                        TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
                        typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
                        return typeMap.map(codigoU);
                    })
                    .collect(Collectors.toList());
            return new ResponseEntity<>(codigoUDtos, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla codigoU");
            throw new MessageNotFoundException("No hay datos en la tabla codigoU");
        }
    }

    @Override
    @Cacheable(value = "codigoU", key = "'findAllP' + #pageNumber + #pageSize + #sortBy + #sortOrder")
    public ResponseEntity<Page<CodigoUDto>> findAllP(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<CodigoU> codigoUPage = codigoURepository.findAll(pageable);
        if (codigoUPage.hasContent()) {
            Page<CodigoUDto> codigoUDtoPage = codigoUPage.map(codigoU -> {
                TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
                typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
                return typeMap.map(codigoU);
            });
            return new ResponseEntity<>(codigoUDtoPage, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla codigou");
            throw new MessageNotFoundException("No hay datos en la tabla codigou");
        }
    }

    @Override
    @Cacheable(value = "codigoU", key = "'findById' + #id")
    public ResponseEntity<CodigoUDto> findById(String id) {
        Optional<CodigoU> codigoUOptional = Optional.ofNullable(codigoURepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(String.format("La persona con código %s no existe", id))));
        if (codigoUOptional.isPresent()) {
            CodigoU codigoU = codigoUOptional.get();
            TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
            typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
            CodigoUDto codigoUDto = typeMap.map(codigoU);
            return ResponseEntity.ok(codigoUDto);
        } else {
            log.warn(String.format("La persona con código %s no existe", id));
            throw new MessageNotFoundException(String.format("La persona con código %s no existe", id));
        }
    }

    @Override
    @Cacheable(value = "codigoU", key = "'findByRfid' + #idRfid")
    public ResponseEntity<CodigoUDto> findByRfid(String idRfid) {
        if (!rfidRepository.existsById(idRfid)) {
            throw new MessageBadRequestException(String.format("El carnet con código %s no existe en base de datos", idRfid));
        }
        Optional<CodigoU> codigoUOptional = Optional.ofNullable(codigoURepository.findByRfidIdRfid(idRfid)
                .orElseThrow(() -> new MessageNotFoundException(String.format("La persona con código %s no existe", idRfid))));

        if (codigoUOptional.isPresent()) {
            CodigoU codigoU = codigoUOptional.get();
            TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
            typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
            CodigoUDto codigoUDto = typeMap.map(codigoU);
            return ResponseEntity.ok(codigoUDto);
        } else {
            log.warn(String.format("La persona con código de carnet %s no existe", idRfid));
            throw new MessageNotFoundException(String.format("La persona con código de carnet %s no existe", idRfid));
        }
    }

    @Override
    @CacheEvict(value = "codigoU", allEntries = true)
    public ResponseEntity<CodigoUDto> create(CodigoUDto codigoUDto) {
        if (codigoUDto.getIdCodigoU().isEmpty() || codigoUDto.getPrimerNombre().isEmpty() || codigoUDto.getPrimerApellido().isEmpty() || codigoUDto.getRfidDto().getIdRfid().isEmpty()) {
            log.warn("Los campos PrimerNombre, Primer Apellido, Codigo Universidad, Codigo Rfid son obligatorios");
            throw new MessageBadRequestException("Los campos PrimerNombre, Primer Apellido, Codigo Universidad, Codigo Rfid son obligatorios");
        }
        if (codigoURepository.existsById(codigoUDto.getIdCodigoU())) {
            log.warn(String.format("La persona con código %s ya se encuentra registrado", codigoUDto.getIdCodigoU()));
            throw new MessageConflictException(String.format("La persona con código %s ya se encuentra registrado", codigoUDto.getIdCodigoU()));
        } else if (codigoURepository.existsByRfidIdRfid(codigoUDto.getRfidDto().getIdRfid())){
            log.warn(String.format("El código de carnet %s que está intentado vincular con el código %s ya se encuentra vinculado a otra persona", codigoUDto.getRfidDto().getIdRfid(), codigoUDto.getIdCodigoU()));
            throw new MessageConflictException(String.format("El código de carnet %s que está intentado vincular con el código %s ya se encuentra vinculado a otra persona", codigoUDto.getRfidDto().getIdRfid(), codigoUDto.getIdCodigoU()));
        } else if (!rfidRepository.existsById(codigoUDto.getRfidDto().getIdRfid())) {
            log.warn("El carnet que se intenta asociar no existe");
            throw new MessageNotFoundException("El carnet que se intenta asociar no existe");
        } else {
            CodigoU codigoU = modelMapper.map(codigoUDto, CodigoU.class);
            codigoU.setPrimerNombre(codigoU.getPrimerNombre().toUpperCase().trim());
            codigoU.setSegundoNombre(codigoU.getSegundoNombre().toUpperCase());
            codigoU.setPrimerApellido(codigoU.getPrimerApellido().toUpperCase().trim());
            codigoU.setSegundoApellido(codigoU.getSegundoApellido().toUpperCase().trim());
            CodigoU saveCodigoU = codigoURepository.save(codigoU);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(saveCodigoU.getIdCodigoU())
                    .toUri();
            log.info("CodigoU creado exitosamente");
            TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
            typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
            codigoUDto = typeMap.map(codigoU);
            return ResponseEntity.created(location).body(codigoUDto);
        }
    }

    @Override
    @CacheEvict(value = "codigoU", allEntries = true)
    public ResponseEntity<CodigoUDto> update(String id, CodigoUDto codigoUDto) {
        try {
            CodigoU codigoU = codigoURepository.findById(id)
                    .orElseThrow(() -> new MessageNotFoundException(String.format("La persona con código %s no está registrada", id)));

            // Definir rfidDelete antes de usarlo
            Rfid rfidDelete = new Rfid();
            rfidDelete.setIdRfid(codigoU.getRfid().getIdRfid());

            // Validar campos no nulos o cumplir con criterios de validación
            if (codigoUDto.getPrimerNombre() == null || codigoUDto.getPrimerNombre().trim().isEmpty() ||
                    codigoUDto.getPrimerApellido() == null || codigoUDto.getPrimerApellido().trim().isEmpty() ||
                    codigoUDto.getRfidDto() == null || codigoUDto.getRfidDto().getIdRfid() == null || codigoUDto.getRfidDto().getIdRfid().isEmpty()) {
                throw new MessageBadRequestException("El Primer Nombre, el Primer Apellido y el código del Rfid son obligatorios");
            }

            // Verificar si el nuevo RFID existe
            if (!rfidRepository.existsById(codigoUDto.getRfidDto().getIdRfid())) {
                log.warn("El carnet que se intenta asociar no existe");
                throw new MessageConflictException("El carnet que se intenta asociar no existe");
            }

            // Actualizar los campos de la persona
            codigoU.setPrimerNombre(codigoUDto.getPrimerNombre().toUpperCase().trim());
            codigoU.setSegundoNombre(codigoUDto.getSegundoNombre() != null ? codigoUDto.getSegundoNombre().toUpperCase() : null);
            codigoU.setPrimerApellido(codigoUDto.getPrimerApellido().toUpperCase().trim());
            codigoU.setSegundoApellido(codigoUDto.getSegundoApellido() != null ? codigoUDto.getSegundoApellido().toUpperCase().trim() : null);

            // Actualizar el RFID
            Rfid rfid = new Rfid();
            rfid.setIdRfid(codigoUDto.getRfidDto().getIdRfid());
            codigoU.setRfid(rfid);

            // Guardar los cambios en la base de datos
            codigoURepository.save(codigoU);

            // Eliminar el RFID anterior si es diferente
            if (!rfidDelete.getIdRfid().equals(rfid.getIdRfid())) {
                rfidRepository.deleteById(rfidDelete.getIdRfid());
                log.info(String.format("El carnet con código %s se eliminó con éxito", rfidDelete.getIdRfid()));
            }

            log.info("Actualización exitosa");
            TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
            typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
            codigoUDto = typeMap.map(codigoU);

            limpiarCaches();


            return ResponseEntity.ok(codigoUDto);
        } catch (MessageNotFoundException e) {
            throw e;
        } catch (MessageBadRequestException | MessageConflictException e) {
            log.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al actualizar la persona.", e);
            throw new MessageInternalServerErrorException("Error inesperado al actualizar la persona.");
        }
    }


    @Override
    @CacheEvict(value = "codigoU", allEntries = true)
    public ResponseEntity<String> delete(String id) {
        // Verificar si existen datos antes de eliminarlos
        boolean borrarBiblioteca = bibliotecaRepository.existsByCodigoUIdCodigoU(id);
        boolean borrarCampus = campusRepository.existsByCodigoUIdCodigoU(id);
        boolean borrarLaboratorio = laboratorioRepository.existsByCodigoUIdCodigoU(id);
        boolean borrarSalaComputo = salaComputoRepository.existsByCodigoUIdCodigoU(id);

        // Eliminar registros de las tablas relacionadas
        if (borrarBiblioteca) bibliotecaRepository.deleteAllByCodigoUIdCodigoU(id);
        if (borrarCampus) campusRepository.deleteAllByCodigoUIdCodigoU(id);
        if (borrarLaboratorio) laboratorioRepository.deleteAllByCodigoUIdCodigoU(id);
        if (borrarSalaComputo) salaComputoRepository.deleteAllByCodigoUIdCodigoU(id);


        // Obtener el ID del RFID
        CodigoUDto codigoU = findById(id).getBody();

        // Eliminar el registro de CodigoU
        codigoURepository.deleteById(id);

        limpiarCaches();

        if (codigoU != null) {
            String idRfidC = codigoU.getRfidDto().getIdRfid();

            // Eliminar el registro de RFID
            rfidRepository.deleteById(idRfidC);
        }

        // Retornar un ResponseEntity con un mensaje de éxito
        return new ResponseEntity<>("Registros eliminados con éxito", HttpStatus.OK);
    }
}
