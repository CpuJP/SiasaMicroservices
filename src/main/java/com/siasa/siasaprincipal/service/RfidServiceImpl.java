package com.siasa.siasaprincipal.service;

import com.siasa.siasaprincipal.dto.RfidDto;
import com.siasa.siasaprincipal.entity.Rfid;
import com.siasa.siasaprincipal.exception.MessageNotContentException;
import com.siasa.siasaprincipal.exception.MessageNotFoundException;
import com.siasa.siasaprincipal.repository.RfidRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "rfidService")
public class RfidServiceImpl implements RfidService{

    private final RfidRepository rfidRepository;

    private final ModelMapper modelMapper;

    public RfidServiceImpl(RfidRepository rfidRepository, ModelMapper modelMapper) {
        this.rfidRepository = rfidRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResponseEntity<List<RfidDto>> findAll() {
        List<Rfid> rfids = rfidRepository.findAll();
        if (!rfids.isEmpty()) {
            List<RfidDto> rfidDtos = rfids.stream()
                    .map(rfid -> modelMapper.map(rfid, RfidDto.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(rfidDtos, HttpStatus.OK);
        } else {
            throw new MessageNotContentException("No hay datos en la tabla Rfid");
        }
    }

    @Override
    public ResponseEntity<RfidDto> findById(String id) {
        Optional<Rfid> rfids = Optional.ofNullable(rfidRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El Rfid con id %s no existe",id))));
        if (rfids.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(rfids.get(), RfidDto.class));
        } else {
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
