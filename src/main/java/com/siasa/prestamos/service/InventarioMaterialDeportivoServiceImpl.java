package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.InventarioMaterialDeportivoDTO;
import com.siasa.prestamos.entity.InventarioMaterialDeportivo;
import com.siasa.prestamos.exception.MessageBadRequestException;
import com.siasa.prestamos.exception.MessageConflictException;
import com.siasa.prestamos.exception.MessageNotContentException;
import com.siasa.prestamos.exception.MessageNotFoundException;
import com.siasa.prestamos.repository.InventarioMaterialDeportivoRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "inventarioMaterialDeportivoService")
@Slf4j
public class InventarioMaterialDeportivoServiceImpl implements InventarioMaterialDeportivoService{

    private final ModelMapper modelMapper;

    private final InventarioMaterialDeportivoRepository inventarioMaterialDeportivoRepository;

    public InventarioMaterialDeportivoServiceImpl(ModelMapper modelMapper, InventarioMaterialDeportivoRepository inventarioMaterialDeportivoRepository) {
        this.modelMapper = modelMapper;
        this.inventarioMaterialDeportivoRepository = inventarioMaterialDeportivoRepository;
    }

    @Override
    public ResponseEntity<List<InventarioMaterialDeportivoDTO>> findAll() {
        List<InventarioMaterialDeportivo> materialDeportivos = inventarioMaterialDeportivoRepository.findAll();
        if (!materialDeportivos.isEmpty()) {
            List<InventarioMaterialDeportivoDTO> deportivoDTOS = materialDeportivos.stream()
                    .map(materialDeportivo -> modelMapper.map(materialDeportivo, InventarioMaterialDeportivoDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(deportivoDTOS, HttpStatus.OK);
        } else {
            log.warn("No hay datos en el inventario de Materiales Deportivos");
            throw new MessageNotFoundException("No hay datos en el inventario de Materiales Deportivos");
        }
    }

    @Override
    public ResponseEntity<Page<InventarioMaterialDeportivoDTO>> findAllP(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<InventarioMaterialDeportivo> materialDeportivoPage = inventarioMaterialDeportivoRepository.findAll(pageable);
        if (materialDeportivoPage.hasContent()) {
            Page<InventarioMaterialDeportivoDTO> materialDeportivoDTOPage = materialDeportivoPage.map(inventarioMaterialDeportivo -> modelMapper.map(inventarioMaterialDeportivo, InventarioMaterialDeportivoDTO.class));
            return new ResponseEntity<>(materialDeportivoDTOPage, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla Inventario Material Deportivo");
            throw new MessageNotFoundException("No hay datos en la tabla Inventario Material Deportivo");
        }
    }

    @Override
    public ResponseEntity<List<InventarioMaterialDeportivoDTO>> findByNombre(String nombre) {
        List<InventarioMaterialDeportivo> materialDeportivos = inventarioMaterialDeportivoRepository.findByNombreContaining(nombre);
        if (!materialDeportivos.isEmpty()) {
            List<InventarioMaterialDeportivoDTO> materialDeportivoDTOS = materialDeportivos.stream()
                    .map(materialDeportivo -> modelMapper.map(materialDeportivo, InventarioMaterialDeportivoDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(materialDeportivoDTOS, HttpStatus.OK);
        } else {
            log.warn(String.format("No hay objetos en el inventario que contengan el nombre %s", nombre));
            throw new MessageNotFoundException(String.format("No hay objetos en el inventario que contengan el nombre %s", nombre));
        }
    }

    @Override
    public ResponseEntity<InventarioMaterialDeportivoDTO> create(InventarioMaterialDeportivoDTO materialDeportivoDTO) {
        if (materialDeportivoDTO.getNombre().isEmpty() || materialDeportivoDTO.getDisponible() < 0) {
            log.warn("El campo de nombre es obligatorio, y los objetos disponibles no pueden ser menor a 0");
            throw new MessageBadRequestException("El campo de nombre es obligatorio, y los objetos disponibles no pueden ser menor a 0");
        }
        if (inventarioMaterialDeportivoRepository.existsByNombre(materialDeportivoDTO.getNombre())) {
            log.warn(String.format("El objeto con nombre %s ya existe en el inventario", materialDeportivoDTO.getNombre()));
            throw new MessageConflictException(String.format("El objeto con nombre %s ya existe en el inventario", materialDeportivoDTO.getNombre()));
        } else {
            InventarioMaterialDeportivo materialDeportivo = modelMapper.map(materialDeportivoDTO, InventarioMaterialDeportivo.class);
            materialDeportivo.setNombre(materialDeportivoDTO.getNombre().toUpperCase());
            materialDeportivo.setDescripcion(materialDeportivoDTO.getDescripcion().toUpperCase());
            materialDeportivo.setDisponible(materialDeportivoDTO.getDisponible());
            InventarioMaterialDeportivo save = inventarioMaterialDeportivoRepository.save(materialDeportivo);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("{id}")
                    .buildAndExpand(save.getIdMaterialDeportivo())
                    .toUri();
            log.info("Objeto creado exitosamente");
            materialDeportivoDTO = modelMapper.map(materialDeportivo, InventarioMaterialDeportivoDTO.class);
            return ResponseEntity.created(location).body(materialDeportivoDTO);
        }
    }

    @Override
    public ResponseEntity<InventarioMaterialDeportivoDTO> updtae(Integer id, InventarioMaterialDeportivoDTO materialDeportivoDTO) {
        try {
            InventarioMaterialDeportivo inventarioMaterialDeportivo = inventarioMaterialDeportivoRepository.findById(id)
                    .orElseThrow(() -> new MessageNotFoundException(String.format("El objeto del inventario con id %s no está registrado en base de datos", id)));
            if (materialDeportivoDTO.getNombre().isEmpty() || materialDeportivoDTO.getDisponible() < 0 ) {
                throw new MessageBadRequestException("El nombre y la cantidad disponible son campos obligatorios");
            }

            inventarioMaterialDeportivo.setNombre(materialDeportivoDTO.getNombre());
            inventarioMaterialDeportivo.setDescripcion(materialDeportivoDTO.getDescripcion());
            inventarioMaterialDeportivo.setDisponible(materialDeportivoDTO.getDisponible());

            inventarioMaterialDeportivoRepository.save(inventarioMaterialDeportivo);

            log.info("Actualización exitosa");
            TypeMap<InventarioMaterialDeportivo, InventarioMaterialDeportivoDTO> typeMap = modelMapper.typeMap(InventarioMaterialDeportivo.class, InventarioMaterialDeportivoDTO.class);
            materialDeportivoDTO = typeMap.map(inventarioMaterialDeportivo);
            return ResponseEntity.ok(materialDeportivoDTO);
        } catch (MessageNotFoundException e) {
            throw e;
        } catch (MessageBadRequestException | MessageConflictException e) {
            log.warn(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al actualizar el objeto del inventario", e);
            throw new MessageConflictException("Error inesperado al actualizar el objeto del inventario");
        }
    }
}
