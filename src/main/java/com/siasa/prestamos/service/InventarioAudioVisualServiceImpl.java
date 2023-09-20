package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.InventarioAudioVisualDTO;
import com.siasa.prestamos.entity.InventarioAudioVisual;
import com.siasa.prestamos.exception.MessageBadRequestException;
import com.siasa.prestamos.exception.MessageConflictException;
import com.siasa.prestamos.exception.MessageNotContentException;
import com.siasa.prestamos.repository.InventarioAudioVisualRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "inventarioAudioVisualService")
@Slf4j
public class InventarioAudioVisualServiceImpl implements InventarioAudioVisualService{

    private final ModelMapper modelMapper;
    private final InventarioAudioVisualRepository audioVisualRepository;

    public InventarioAudioVisualServiceImpl(ModelMapper modelMapper, InventarioAudioVisualRepository audioVisualRepository) {
        this.modelMapper = modelMapper;
        this.audioVisualRepository = audioVisualRepository;
    }

    @Override
    public ResponseEntity<List<InventarioAudioVisualDTO>> findAll() {
        List<InventarioAudioVisual> audioVisuals = audioVisualRepository.findAll();
        if (!audioVisuals.isEmpty()) {
            List<InventarioAudioVisualDTO> audioVisualDTOS = audioVisuals.stream()
                    .map(audioVisual -> modelMapper.map(audioVisual, InventarioAudioVisualDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(audioVisualDTOS, HttpStatus.OK);

        } else {
            log.warn("No hay datos en la tabla Inventario AudioVisuales");
            throw new MessageNotContentException("No hay datos en la tabla Inventario AudioVisuales");
        }
    }

    @Override
    public ResponseEntity<List<InventarioAudioVisualDTO>> findByNombre(String nombre) {
        List<InventarioAudioVisual> audioVisuals = audioVisualRepository.findByNombreContaining(nombre);
        if (!audioVisuals.isEmpty()) {
            List<InventarioAudioVisualDTO> audioVisualDTOS = audioVisuals.stream()
                    .map(audiovisual -> modelMapper.map(audiovisual, InventarioAudioVisualDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(audioVisualDTOS, HttpStatus.OK);
        } else {
            log.warn(String.format("No hay datos en el inventario que contengan el nombre %s", nombre));
            throw new MessageBadRequestException(String.format("No hay datos en el inventario que contengan el nombre %s", nombre));
        }
    }

    @Override
    public ResponseEntity<InventarioAudioVisualDTO> create(InventarioAudioVisualDTO audioVisualDTO) {
        if (audioVisualDTO.getNombre().isEmpty() || audioVisualDTO.getDisponible() < 0) {
            log.warn("El campo de nombre es obligatorio, y los objetos disponibles no pueden ser menor a 0");
            throw new MessageBadRequestException("El campo de nombre es obligatorio, y los objetos disponibles no pueden ser menor a 0");
        }
        if (audioVisualRepository.existsByNombre(audioVisualDTO.getNombre())) {
            log.warn(String.format("El objeto con nombre %s ya existe en el inventario", audioVisualDTO.getNombre()));
            throw new MessageConflictException(String.format("El objeto con nombre %s ya existe en el inventario", audioVisualDTO.getNombre()));
        } else {
            InventarioAudioVisual audioVisual = modelMapper.map(audioVisualDTO, InventarioAudioVisual.class);
            audioVisual.setNombre(audioVisualDTO.getNombre().toUpperCase());
            audioVisual.setDisponible(audioVisualDTO.getDisponible());
            audioVisual.setDescripcion(audioVisualDTO.getDescripcion().toUpperCase());
            InventarioAudioVisual save = audioVisualRepository.save(audioVisual);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(save.getIdAudioVisual())
                    .toUri();
            log.info("Objeto creado exitosamente");
            audioVisualDTO = modelMapper.map(audioVisual, InventarioAudioVisualDTO.class);
            return ResponseEntity.created(location).body(audioVisualDTO);
        }
    }

    @Override
    public ResponseEntity<InventarioAudioVisualDTO> update(Integer id, InventarioAudioVisualDTO audioVisualDTO) {
        return null;
    }
}
