package com.siasa.principalfailover.service;

import com.siasa.principalfailover.dto.RfidDto;
import com.siasa.principalfailover.entity.Rfid;
import com.siasa.principalfailover.exception.MessageBadRequestException;
import com.siasa.principalfailover.exception.MessageConflictException;
import com.siasa.principalfailover.exception.MessageNotFoundException;
import com.siasa.principalfailover.repository.RfidRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service(value = "rfidService")
public class RfidServiceImpl implements RfidService{

    //Se inyectan los Beans necesarios
    private final RfidRepository rfidRepository;

    private final ModelMapper modelMapper;

    public RfidServiceImpl(RfidRepository rfidRepository, ModelMapper modelMapper) {
        this.rfidRepository = rfidRepository;
        this.modelMapper = modelMapper;
    }


    //Se crea la logica de negocio implementando la interface para abstraer la logica de la exposicion de apis
    @Override
    @Cacheable(value = "rfid", key = "'findAll'")
    public ResponseEntity<List<RfidDto>> findAll() {
        List<Rfid> rfids = rfidRepository.findAll();
        //Si la lista no esta vacia se mapea del entity a dto para exponerlo en el endpoint
        if (!rfids.isEmpty()) {
            List<RfidDto> rfidDtos = rfids.stream()
                    .map(rfid -> modelMapper.map(rfid, RfidDto.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(rfidDtos, HttpStatus.OK);
            //si la lista esta vacia, se envia un mensaje de excepcion donde se muestra el codigo 204 sin contenido
        } else {
            log.warn("No hay datos en la tabla Rfid");
            throw new MessageNotFoundException("No hay datos en la tabla Rfid");
        }
    }

    @Override
    @Cacheable(value = "rfid", key = "'findAllP' + #pageNumber + #pageSize")
    public ResponseEntity<Page<RfidDto>> findAllP(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Rfid> rfidPage = rfidRepository.findAll(pageable);
        if (rfidPage.hasContent()) {
            Page<RfidDto> rfidDtoPage = rfidPage.map(rfid -> modelMapper.map(rfid, RfidDto.class));
            return new ResponseEntity<>(rfidDtoPage, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla Rfid");
            throw new MessageNotFoundException("No hay datos en la tabla Rfid");
        }
    }

    @Override
    @Cacheable(value = "rfid", key = "'findRfidWithoutCodigoU'")
    public ResponseEntity<List<RfidDto>> findRfidWithoutCodigoU() {
        List<Rfid> rfids = rfidRepository.findRfidWithoutCodigoU();
        if (!rfids.isEmpty()) {
            List<RfidDto> rfidDtos = rfids.stream()
                    .map(rfid -> modelMapper.map(rfid, RfidDto.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(rfidDtos, HttpStatus.OK);
        } else {
            log.warn("Todos los carnets registrados se encuentran vinculados a un usuario");
            throw new MessageNotFoundException("Todos los carnets registrados se encuentran vinculados a un usuario");
        }
    }

    @Override
    @Cacheable(value = "rfid", key = "'findById' + #id")
    public ResponseEntity<RfidDto> findById(String id) {
        //Se toma el id obtenido por la api y se envia al repositorio para que lo busque en la BD
        Optional<Rfid> rfids = Optional.ofNullable(rfidRepository.findById(id)
                //si el id no existe se envia un mensaje de excepcion 404 de no encontrado
                .orElseThrow(() -> new MessageNotFoundException(String.format("El Rfid con id %s no existe",id))));
        //si se encuentra el id, se envia al body la informacion de la entidad mapeada en el dto
        if (rfids.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(rfids.get(), RfidDto.class));
        } else {
            log.warn(String.format("El Rfid con el id %s no existe", id));
            throw new MessageNotFoundException(String.format("El Rfid con id %s no existe",id));
        }
    }

    @Override
    @CacheEvict(value = "rfid", allEntries = true)
    public ResponseEntity<RfidDto> create(RfidDto rfidDto) {
        if (rfidDto.getIdRfid().isEmpty()) {
            log.warn("El campo del código del carnet es obligatorio");
            throw new MessageBadRequestException("El campo del código del carnet es obligatorio");
        }
        if (rfidRepository.existsById(rfidDto.getIdRfid())) {
            log.warn(String.format("El carnet con código %s ya se encuentra registrado", rfidDto.getIdRfid()));
            throw new MessageConflictException(String.format("El carnet con código %s ya se encuentra registrado", rfidDto.getIdRfid()));
        } else {
            Rfid rfid = modelMapper.map(rfidDto, Rfid.class);
            Rfid saveRfid = rfidRepository.save(rfid);

            // Construir la URL del nuevo recurso creado
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(saveRfid.getIdRfid())
                    .toUri();
            log.info("Rfid creado exitosamente");
            return ResponseEntity.created(location).body(modelMapper.map(saveRfid, RfidDto.class));
        }
    }

    @Override
    @CacheEvict(value = "rfid", allEntries = true)
    public ResponseEntity<String> delete(String id) {
        if (rfidRepository.isRfidLinkedToCodigoU(id)) {
            log.warn(String.format("El carnet que se intenta eliminar con código %s tiene datos ligados", id));
            throw new MessageConflictException(String.format("El carnet que se intenta eliminar con código %s tiene datos ligados", id));
        }
        if (!rfidRepository.existsById(id)) {
            log.warn(String.format("El carnet que se intenta eliminar con código %s no se encuentra registrado", id));
            throw new MessageNotFoundException(String.format("El carnet que se intenta eliminar con código %s no se encuentra registrado", id));
        }
        rfidRepository.deleteById(id);
        log.info(String.format("EL rfid con código %s se eliminó exitosamente", id));
        return new ResponseEntity<>(String.format("EL rfid con código %s se eliminó exitosamente", id), HttpStatus.OK);
    }
}
