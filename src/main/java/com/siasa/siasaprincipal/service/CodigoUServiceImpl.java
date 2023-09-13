package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.Rfid;
import com.siasa.siasaprincipal.exception.MessageBadRequestException;
import com.siasa.siasaprincipal.exception.MessageConflictException;
import com.siasa.siasaprincipal.exception.MessageNotContentException;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
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
    private final ModelMapper modelMapper;
    private final BibliotecaRepository bibliotecaRepository;
    private final CampusRepository campusRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final SalaComputoRepository salaComputoRepository;

    public CodigoUServiceImpl(CodigoURepository codigoURepository, RfidRepository rfidRepository, ModelMapper modelMapper, BibliotecaRepository bibliotecaRepository, CampusRepository campusRepository, LaboratorioRepository laboratorioRepository, SalaComputoRepository salaComputoRepository) {
        this.codigoURepository = codigoURepository;
        this.rfidRepository = rfidRepository;
        this.modelMapper = modelMapper;
        this.bibliotecaRepository = bibliotecaRepository;
        this.campusRepository = campusRepository;
        this.laboratorioRepository = laboratorioRepository;
        this.salaComputoRepository = salaComputoRepository;
    }

    @Override
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
            throw new MessageNotContentException("No hay datos en la tabla codigoU");
        }
    }

    @Override
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
            throw new MessageBadRequestException("El carnet que se intenta asociar no existe");
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
    public ResponseEntity<CodigoUDto> update(String id, CodigoUDto codigoUDto) {
        CodigoU codigoU = codigoURepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(String.format("La persona con código %s no está registrada", id)));
        codigoU.setPrimerNombre(codigoUDto.getPrimerNombre());
        codigoU.setSegundoNombre(codigoUDto.getSegundoNombre());
        codigoU.setPrimerApellido(codigoUDto.getPrimerApellido());
        codigoU.setSegundoApellido(codigoUDto.getSegundoApellido());

        Rfid rfidDelete = new Rfid();
        rfidDelete.setIdRfid(codigoU.getRfid().getIdRfid());

        Rfid rfid = new Rfid();
        rfid.setIdRfid(codigoUDto.getRfidDto().getIdRfid());
        codigoU.setRfid(rfid);



        if (codigoU.getPrimerNombre().isEmpty() || codigoU.getPrimerApellido().isEmpty() || codigoU.getRfid().getIdRfid().isEmpty()) {
            throw new MessageBadRequestException("El Primer Nombre, el Primer Apellido, y el código del Rfid son obligatorios");
        } else if (!rfidRepository.existsById(codigoUDto.getRfidDto().getIdRfid())) {
            log.warn("El carnet que se intenta asociar no existe");
            throw new MessageBadRequestException("El carnet que se intenta asociar no existe");
        }

        try {
            codigoU.setPrimerNombre(codigoU.getPrimerNombre().toUpperCase().trim());
            codigoU.setSegundoNombre(codigoU.getSegundoNombre().toUpperCase());
            codigoU.setPrimerApellido(codigoU.getPrimerApellido().toUpperCase().trim());
            codigoU.setSegundoApellido(codigoU.getSegundoApellido().toUpperCase().trim());
            codigoURepository.save(codigoU);
            TypeMap<CodigoU, CodigoUDto> typeMap = modelMapper.typeMap(CodigoU.class, CodigoUDto.class);
            typeMap.addMapping(CodigoU::getRfid, CodigoUDto::setRfidDto);
            codigoUDto = typeMap.map(codigoU);
            rfidRepository.deleteById(rfidDelete.getIdRfid());
            log.info(String.format("El carnet con código %s se eliminó con éxito", rfidDelete.getIdRfid()));
            log.info("Actualización exitosa");
            return ResponseEntity.ok(codigoUDto);
        } catch (Exception e) {
            log.warn(String.format("El código de carnet %s que está intentado vincular con el código %s ya se encuentra vinculado a otra persona", codigoUDto.getRfidDto().getIdRfid(), id));
            throw new MessageConflictException(String.format("El código de carnet %s que está intentado vincular con el código %s ya se encuentra vinculado a otra persona", codigoUDto.getRfidDto().getIdRfid(), id));
        }

    }

    @Override
    public ResponseEntity<String> delete(String id) {
        // Verificar si existen datos antes de eliminarlos
        boolean borrarBiblioteca = bibliotecaRepository.existsByCodigoUIdCodigoU(id);
        boolean borrarCampus = campusRepository.existsByCodigoUIdCodigoU(id);
        boolean borrarLaboratorio = laboratorioRepository.existsByCodigoUIdCodigoU(id);
        boolean borrarSalaComputo = salaComputoRepository.existsByCodigoUIdCodigoU(id);

        // Si no existe ninguno de los registros, retorna un ResponseEntity con un mensaje
        if (!borrarBiblioteca && !borrarCampus && !borrarLaboratorio && !borrarSalaComputo) {
            throw new MessageNotFoundException(String.format("No existen registros vinculados al código %s para eliminar", id));
        }

        // Eliminar registros de las tablas relacionadas
        if (borrarBiblioteca) {
            bibliotecaRepository.deleteAllByCodigoUIdCodigoU(id);
        }
        if (borrarCampus) {
            campusRepository.deleteAllByCodigoUIdCodigoU(id);
        }
        if (borrarLaboratorio) {
            laboratorioRepository.deleteAllByCodigoUIdCodigoU(id);
        }
        if (borrarSalaComputo) {
            salaComputoRepository.deleteAllByCodigoUIdCodigoU(id);
        }

        // Obtener el ID del RFID
        CodigoUDto codigoU = findById(id).getBody();

        // Eliminar el registro de CodigoU
        codigoURepository.deleteById(id);

        if (codigoU != null) {
            String idRfidC = codigoU.getRfidDto().getIdRfid();

            // Eliminar el registro de RFID
            rfidRepository.deleteById(idRfidC);
        }

        // Retornar un ResponseEntity con un mensaje de éxito
        return new ResponseEntity<>("Registros eliminados con éxito", HttpStatus.OK);
    }
}
