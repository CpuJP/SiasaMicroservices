package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.CodigoUDTO;
import com.siasa.prestamos.dto.PrestamoAudioVisualDTO;
import com.siasa.prestamos.entity.InventarioAudioVisual;
import com.siasa.prestamos.entity.PrestamoAudioVisual;
import com.siasa.prestamos.exception.MessageBadRequestException;
import com.siasa.prestamos.exception.MessageConflictException;
import com.siasa.prestamos.exception.MessageNotFoundException;
import com.siasa.prestamos.repository.InventarioAudioVisualRepository;
import com.siasa.prestamos.repository.PrestamoAudioVisualRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(value = "prestamoAudioVisualService")
@Slf4j
public class PrestamoAudioVisualServiceImpl implements PrestamoAudioVisualService{

    private final InventarioAudioVisualRepository inventarioAudioVisualRepository;

    private final PrestamoAudioVisualRepository prestamoAudioVisualRepository;

    private final ModelMapper modelMapper;

    public PrestamoAudioVisualServiceImpl(InventarioAudioVisualRepository inventarioAudioVisualRepository, PrestamoAudioVisualRepository prestamoAudioVisualRepository, ModelMapper modelMapper) {
        this.inventarioAudioVisualRepository = inventarioAudioVisualRepository;
        this.prestamoAudioVisualRepository = prestamoAudioVisualRepository;
        this.modelMapper = modelMapper;
    }

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.url.exists.rfid}")
    private String apiUrlEBR;

    @Value("${api.url.exists.udec}")
    private String apiUrlEBU;

    private ResponseEntity<List<PrestamoAudioVisualDTO>> getListResponseEntity(List<PrestamoAudioVisual> prestamoAudioVisual) {
        List<PrestamoAudioVisualDTO> prestamoAudioVisualDTOS = prestamoAudioVisual.stream()
                .map(prestamoAudioVisual1 -> {
                    TypeMap<PrestamoAudioVisual, PrestamoAudioVisualDTO> typeMap = modelMapper.typeMap(PrestamoAudioVisual.class, PrestamoAudioVisualDTO.class);
                    typeMap.addMapping(PrestamoAudioVisual::getInventarioAudioVisual, PrestamoAudioVisualDTO::setInventarioAudioVisualDTO);
                    return typeMap.map(prestamoAudioVisual1);
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(prestamoAudioVisualDTOS, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findAll() {
        List<PrestamoAudioVisual> prestamoAudioVisuals = prestamoAudioVisualRepository.findAll();
        if (!prestamoAudioVisuals.isEmpty()) {
            return getListResponseEntity(prestamoAudioVisuals);
        } else {
            log.warn("No hay datos en la tabla PrestamosAudioVisual");
            throw new MessageNotFoundException("No hay datos en la tabla PrestamosAudioVisual");
        }
    }

    @Override
    public ResponseEntity<Page<PrestamoAudioVisualDTO>> findAllP(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<PrestamoAudioVisual> prestamoAudioVisualPage = prestamoAudioVisualRepository.findAll(pageable);
        if (prestamoAudioVisualPage.hasContent()) {
            Page<PrestamoAudioVisualDTO> prestamoAudioVisualDTOPage = prestamoAudioVisualPage.map(prestamoAudioVisual -> {
                TypeMap<PrestamoAudioVisual, PrestamoAudioVisualDTO> typeMap = modelMapper.typeMap(PrestamoAudioVisual.class, PrestamoAudioVisualDTO.class);
                typeMap.addMapping(PrestamoAudioVisual::getInventarioAudioVisual, PrestamoAudioVisualDTO::setInventarioAudioVisualDTO);
                return typeMap.map(prestamoAudioVisual);
            });
            return new ResponseEntity<>(prestamoAudioVisualDTOPage, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla PrestamosAudioVisual");
            throw new MessageNotFoundException("No hay datos en la tabla PrestamosAudioVisual");
        }
    }



    @Override
    public ResponseEntity<PrestamoAudioVisualDTO> createIn(Integer idInventarioAudioVisual, String idRfid, String nota, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new MessageBadRequestException("El campo de cantidad es obligatorio, y no puede ser menor a 1");
        }
        InventarioAudioVisual inventarioAudioVisualLoad = inventarioAudioVisualRepository.findById(idInventarioAudioVisual)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El objeto de inventario con id %d no registra", idInventarioAudioVisual)));
        try {
            ResponseEntity<CodigoUDTO> response = new RestTemplate().exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    CodigoUDTO.class,
                    idRfid
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                CodigoUDTO codigoUDTO = response.getBody();
                assert codigoUDTO != null;
                String nombre = codigoUDTO.getPrimerNombre() + " " + codigoUDTO.getSegundoNombre();
                String apellido = codigoUDTO.getPrimerApellido() + " " + codigoUDTO.getSegundoApellido();
                String idUdec = codigoUDTO.getIdCodigoU();
                Integer validarExistencia = inventarioAudioVisualLoad.getDisponible();
                LocalDateTime fechaActual = LocalDateTime.now();
                PrestamoAudioVisual prestamoAudioVisual = PrestamoAudioVisual.builder()
                        .fechaPrestamo(fechaActual)
                        .cantidad(cantidad)
                        .nombre(nombre)
                        .apellido(apellido)
                        .idRfid(idRfid)
                        .idUdec(idUdec)
                        .nota(nota)
                        .inventarioAudioVisual(inventarioAudioVisualLoad)
                        .build();
                if (validarExistencia <= 0) {
                    log.warn("El objeto no cuenta con stock para realizar el prestamo");
                    throw new MessageConflictException("El objeto no cuenta con stock para realizar el prestamo");
                } else if (cantidad > validarExistencia) {
                    log.warn(String.format("El objeto no cuenta con suficiente stock, solo dispone con %d", validarExistencia));
                    throw new MessageConflictException(String.format("El objeto no cuenta con suficiente stock, solo dispone con %d", validarExistencia));
                } else {
                    Integer restarExistencia = validarExistencia - cantidad;
                    inventarioAudioVisualLoad.setDisponible(restarExistencia);
                    inventarioAudioVisualRepository.save(inventarioAudioVisualLoad);
                    PrestamoAudioVisual prestamoAudioVisualSave = prestamoAudioVisualRepository.save(prestamoAudioVisual);
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("{/id}")
                            .buildAndExpand(prestamoAudioVisualSave.getIdPrestamoAudioVisual())
                            .toUri();
                    log.info("Prestamo creado exitosamente");
                    TypeMap<PrestamoAudioVisual, PrestamoAudioVisualDTO> typeMap = modelMapper.typeMap(PrestamoAudioVisual.class, PrestamoAudioVisualDTO.class);
                    typeMap.addMapping(PrestamoAudioVisual::getInventarioAudioVisual, PrestamoAudioVisualDTO::setInventarioAudioVisualDTO);
                    PrestamoAudioVisualDTO prestamoAudioVisualDTO = typeMap.map(prestamoAudioVisualSave);
                    return ResponseEntity.created(location).body(prestamoAudioVisualDTO);
                }
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.warn(String.format("El carnet con código %s no existe en base de datos", idRfid));
                throw new MessageBadRequestException(String.format("El carnet con código %s no existe en base de datos", idRfid));
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn(String.format("La persona con código %s no existe", idRfid));
                throw new MessageNotFoundException(String.format("La persona con código %s no existe", idRfid));
            } else {
                log.warn("Error al obtener la información de la API externa");
                throw new MessageBadRequestException("Error al obtener la información de la API externa");
            }
        } catch (HttpClientErrorException.BadRequest e) {
            // Captura el error 400 Bad Request de la API externa y redirige el mensaje.
            log.warn(String.format("Error 400 Bad Request - %s", e.getResponseBodyAsString()));
            throw new MessageBadRequestException(String.format("Error 400 Bad Request - %s", e.getResponseBodyAsString()));
        } catch (HttpClientErrorException.NotFound e) {
            // Captura el error 404 Not Found de la API externa y redirige el mensaje.
            log.warn(String.format("Error 404 Not Found - %s", e.getResponseBodyAsString()));
            throw new MessageNotFoundException(String.format("Error 404 Not Found - %s", e.getResponseBodyAsString()));
        }
    }

    @Override
    public ResponseEntity<PrestamoAudioVisualDTO> createOut(Integer idPrestamoAudioVisual, String observaciones) {
        PrestamoAudioVisual prestamoAudioVisual = prestamoAudioVisualRepository.findById(idPrestamoAudioVisual)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El prestamo con ID %s no registra", idPrestamoAudioVisual)));
        InventarioAudioVisual inventarioAudioVisual = inventarioAudioVisualRepository.findById(prestamoAudioVisual.getInventarioAudioVisual().getIdAudioVisual())
                .orElseThrow(() -> new MessageNotFoundException(String.format("El objeto de inventario con id %d no registra", prestamoAudioVisual.getInventarioAudioVisual().getIdAudioVisual())));
        Integer sumarExistencia = inventarioAudioVisual.getDisponible() + prestamoAudioVisual.getCantidad();
        inventarioAudioVisual.setDisponible(sumarExistencia);
        LocalDateTime fechaActual = LocalDateTime.now();
        prestamoAudioVisual.setFechaDevolucion(fechaActual);
        prestamoAudioVisual.setObservaciones(observaciones);
        inventarioAudioVisualRepository.save(inventarioAudioVisual);
        prestamoAudioVisualRepository.save(prestamoAudioVisual);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(prestamoAudioVisual.getIdPrestamoAudioVisual())
                .toUri();
        log.info("Registro de devolución creado exitosamente");
        TypeMap<PrestamoAudioVisual, PrestamoAudioVisualDTO> typeMap = modelMapper.typeMap(PrestamoAudioVisual.class, PrestamoAudioVisualDTO.class);
        typeMap.addMapping(PrestamoAudioVisual::getInventarioAudioVisual, PrestamoAudioVisualDTO::setInventarioAudioVisualDTO);
        PrestamoAudioVisualDTO prestamoAudioVisualDTO = typeMap.map(prestamoAudioVisual);
        return ResponseEntity.created(location).body(prestamoAudioVisualDTO);
    }

    @Override
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findAllByIdRfid(String idRfid) {
        try {
            ResponseEntity<CodigoUDTO.RfidDTO> response = new RestTemplate().exchange(
                    apiUrlEBR,
                    HttpMethod.GET,
                    null,
                    CodigoUDTO.RfidDTO.class,
                    idRfid
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                List<PrestamoAudioVisual> prestamoAudioVisual = Optional.ofNullable(prestamoAudioVisualRepository.findAllByIdRfid(idRfid))
                        .orElseThrow(() -> new  MessageNotFoundException(String.format("No hay registro de prestamos para el usuario con ID de RFID %s", idRfid)));

                if (!prestamoAudioVisual.isEmpty()) {
                    return getListResponseEntity(prestamoAudioVisual);
                } else {
                    log.warn(String.format("No hay registro de prestamos para el usuario con ID de RFID %s", (idRfid)));
                    throw  new MessageNotFoundException(String.format("No hay registro de prestamos para el usuario con ID de RFID %s", (idRfid)));
                }
            } else {
                log.warn("Error al obtener la información de la API externa");
                throw new MessageBadRequestException("Error al obtener la información de la API externa");
            }
        } catch (HttpClientErrorException.NotFound e) {
            // Captura el error 404 Not Found de la API externa y redirige el mensaje.
            log.warn(String.format("Error 404 Not Found - %s", e.getResponseBodyAsString()));
            throw new MessageNotFoundException(String.format("Error 404 Not Found - %s", e.getResponseBodyAsString()));
        }
    }

    @Override
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findAllByIdUdec(String idUdec) {
        try {
            ResponseEntity<CodigoUDTO> response = new RestTemplate().exchange(
                    apiUrlEBU,
                    HttpMethod.GET,
                    null,
                    CodigoUDTO.class,
                    idUdec
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                List<PrestamoAudioVisual> prestamoAudioVisual = Optional.ofNullable(prestamoAudioVisualRepository.findAllByIdUdec(idUdec))
                        .orElseThrow(() -> new  MessageNotFoundException(String.format("No hay registro de prestamos para el usuario con ID de UDEC %s", idUdec)));

                if (!prestamoAudioVisual.isEmpty()) {
                    return getListResponseEntity(prestamoAudioVisual);
                } else {
                    log.warn(String.format("No hay registro de prestamos para el usuario con ID de UDEC %s", (idUdec)));
                    throw  new MessageNotFoundException(String.format("No hay registro de prestamos para el usuario con ID de UDEC %s", (idUdec)));
                }
            } else {
                log.warn("Error al obtener la información de la API externa");
                throw new MessageBadRequestException("Error al obtener la información de la API externa");
            }
        } catch (HttpClientErrorException.NotFound e) {
            // Captura el error 404 Not Found de la API externa y redirige el mensaje.
            log.warn(String.format("Error 404 Not Found - %s", e.getResponseBodyAsString()));
            throw new MessageNotFoundException(String.format("Error 404 Not Found - %s", e.getResponseBodyAsString()));
        }
    }

    @Override
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaPrestamo(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        List<PrestamoAudioVisual> prestamoAudioVisuals = prestamoAudioVisualRepository.findPrestamoAudioVisualsByFechaPrestamoBetween(fechaInicial, fechaFinal);
        String format = String.format("no hay prestamos registrados dentro de las fechas declaradas, desde: %tF %tR hasta %tF %tR", fechaInicial, fechaInicial, fechaFinal, fechaFinal);
        if (!prestamoAudioVisuals.isEmpty()) {
            return getListResponseEntity(prestamoAudioVisuals);
        } else {
            log.warn(format);
            throw new MessageNotFoundException(format);
        }
    }

    @Override
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaDevolucion(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        List<PrestamoAudioVisual> prestamoAudioVisuals = prestamoAudioVisualRepository.findPrestamoAudioVisualsByFechaDevolucionBetween(fechaInicial, fechaFinal);
        String format = String.format("no hay devoluciones registradas dentro de las fechas declaradas, desde: %tF %tR hasta %tF %tR", fechaInicial, fechaInicial, fechaFinal, fechaFinal);
        if (!prestamoAudioVisuals.isEmpty()) {
            return getListResponseEntity(prestamoAudioVisuals);
        } else {
            log.warn(format);
            throw new MessageNotFoundException(format);
        }
    }

    @Override
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByFechaDevolucionIsEmpty() {
        List<PrestamoAudioVisual> prestamoAudioVisuals = prestamoAudioVisualRepository.findByFechaDevolucionIsNull();
        if (!prestamoAudioVisuals.isEmpty()) {
            return getListResponseEntity(prestamoAudioVisuals);
        } else {
            log.warn("No hay prestamos pendientes de entrega");
            throw new MessageNotFoundException("No hay prestamos pendientes de entrega");
        }
    }

    @Override
    public ResponseEntity<List<PrestamoAudioVisualDTO>> findByInventarioAudioVisualNombreBeLike(String nombreObjeto) {
        List<PrestamoAudioVisual> prestamoAudioVisuals = prestamoAudioVisualRepository.findByInventarioAudioVisualNombreContainingIgnoreCase(nombreObjeto);
        if (!prestamoAudioVisuals.isEmpty()) {
            return getListResponseEntity(prestamoAudioVisuals);
        } else {
            log.warn(String.format("No hay prestamos sobre el objeto que contiene el nombre %s", nombreObjeto));
            throw new MessageBadRequestException(String.format("No hay prestamos sobre el objeto que contiene el nombre %s", nombreObjeto));
        }
    }

}
