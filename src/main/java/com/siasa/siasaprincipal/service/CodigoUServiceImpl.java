package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.CodigoUDto;
import com.siasa.siasaprincipal.dto.RfidDto;
import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.Rfid;
import com.siasa.siasaprincipal.exception.MessageNotContentException;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.CodigoURepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "codigoUService")
@Slf4j
public class CodigoUServiceImpl implements CodigoUService{

    private final CodigoURepository codigoURepository;

    private final ModelMapper modelMapper;

    public CodigoUServiceImpl(CodigoURepository codigoURepository, ModelMapper modelMapper) {
        this.codigoURepository = codigoURepository;
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
        return null;
    }

    @Override
    public ResponseEntity<CodigoUDto> update(String id, CodigoUDto codigoUDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        return null;
    }
}
