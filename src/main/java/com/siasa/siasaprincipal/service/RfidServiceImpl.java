package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.RfidDto;
import com.siasa.siasaprincipal.entity.Rfid;
import com.siasa.siasaprincipal.exception.MessageNotContentException;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.RfidRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
            throw new MessageNotContentException("No hay datos en la tabla Rfid");
        }
    }

    @Override
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
    public ResponseEntity<RfidDto> create(RfidDto rfidDto) {
        return null;
    }

    @Override
    public ResponseEntity<RfidDto> update(RfidDto rfidDto) {
        return null;
    }

    @Override
    public ResponseEntity<RfidDto> delete(String id) {
        return null;
    }
}
