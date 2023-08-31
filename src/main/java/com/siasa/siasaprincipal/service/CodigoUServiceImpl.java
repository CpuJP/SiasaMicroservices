package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.Rfid;
import com.siasa.siasaprincipal.exception.MessageBadRequestException;
import com.siasa.siasaprincipal.exception.MessageConflictException;
import com.siasa.siasaprincipal.exception.MessageNotContentException;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.CodigoURepository;
import com.siasa.siasaprincipal.repository.RfidRepository;
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

    public CodigoUServiceImpl(CodigoURepository codigoURepository, RfidRepository rfidRepository, ModelMapper modelMapper) {
        this.codigoURepository = codigoURepository;
        this.rfidRepository = rfidRepository;
        this.modelMapper = modelMapper;
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
            CodigoU saveCodigoU = codigoURepository.save(codigoU);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(saveCodigoU.getIdCodigoU())
                    .toUri();
            log.info("CodigoU creado exitosamente");
            return ResponseEntity.created(location).body(modelMapper.map(saveCodigoU, CodigoUDto.class));
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
    public ResponseEntity<Void> delete(String id) {
        //TODO: cuando estén los demás controladores, venir y borrar en cascada a todas las entiades
        return null;
    }
}
