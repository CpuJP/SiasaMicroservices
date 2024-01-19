package com.siasa.prestamos.service;

import com.siasa.prestamos.dto.CodigoUDTO;
import com.siasa.prestamos.dto.PrestamoMaterialDeportivoDTO;
import com.siasa.prestamos.dto.PrestamoAudioVisualDTO;
import com.siasa.prestamos.entity.InventarioMaterialDeportivo;
import com.siasa.prestamos.entity.PrestamoAudioVisual;
import com.siasa.prestamos.entity.PrestamoMaterialDeportivo;
import com.siasa.prestamos.exception.MessageBadRequestException;
import com.siasa.prestamos.exception.MessageConflictException;
import com.siasa.prestamos.exception.MessageNotFoundException;
import com.siasa.prestamos.repository.InventarioMaterialDeportivoRepository;
import com.siasa.prestamos.repository.PrestamoMaterialDeportivoRepository;
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

@Service(value = "prestamoMaterialDeportivoService")
@Slf4j
public class PrestamoMaterialDeportivoServiceImpl implements PrestamoMaterialDeportivoService{

    private final InventarioMaterialDeportivoRepository inventarioMaterialDeportivoRepository;

    private final PrestamoMaterialDeportivoRepository prestamoMaterialDeportivoRepository;

    private final ModelMapper modelMapper;

    public PrestamoMaterialDeportivoServiceImpl(InventarioMaterialDeportivoRepository inventarioMaterialDeportivoRepository, PrestamoMaterialDeportivoRepository prestamoMaterialDeportivoRepository, ModelMapper modelMapper) {
        this.inventarioMaterialDeportivoRepository = inventarioMaterialDeportivoRepository;
        this.prestamoMaterialDeportivoRepository = prestamoMaterialDeportivoRepository;
        this.modelMapper = modelMapper;
    }

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.url.exists.rfid}")
    private String apiUrlEBR;

    @Value("${api.url.exists.udec}")
    private String apiUrlEBU;

    private ResponseEntity<List<PrestamoMaterialDeportivoDTO>> getListResponseEntity(List<PrestamoMaterialDeportivo> prestamoMaterialDeportivos) {
        List<PrestamoMaterialDeportivoDTO> prestamoMaterialDeportivoDTOS = prestamoMaterialDeportivos.stream()
                .map(prestamoMaterialDeportivo -> {
                    TypeMap<PrestamoMaterialDeportivo, PrestamoMaterialDeportivoDTO> typeMap = modelMapper.typeMap(PrestamoMaterialDeportivo.class, PrestamoMaterialDeportivoDTO.class);
                    typeMap.addMapping(PrestamoMaterialDeportivo::getInventarioMaterialDeportivo, PrestamoMaterialDeportivoDTO::setInventarioMaterialDeportivoDTO);
                    return typeMap.map(prestamoMaterialDeportivo);
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(prestamoMaterialDeportivoDTOS, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findALl() {
        List<PrestamoMaterialDeportivo> prestamoMaterialDeportivos = prestamoMaterialDeportivoRepository.findAll();
        if (!prestamoMaterialDeportivos.isEmpty()) {
            return getListResponseEntity(prestamoMaterialDeportivos);
        } else {
            log.warn("No hay datos en la tabla PrestamosMaterialDeportivo");
            throw new MessageNotFoundException("No hay datos en la tabla PrestamosMaterialDeportivo");
        }
    }

    @Override
    public ResponseEntity<Page<PrestamoMaterialDeportivoDTO>> findAllP(int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<PrestamoMaterialDeportivo> prestamoMaterialDeportivoPage = prestamoMaterialDeportivoRepository.findAll(pageable);
        if (prestamoMaterialDeportivoPage.hasContent()) {
            Page<PrestamoMaterialDeportivoDTO> prestamoMaterialDeporitvoDTOPage = prestamoMaterialDeportivoPage.map(prestamoMaterialDeportivo -> {
                TypeMap<PrestamoMaterialDeportivo, PrestamoMaterialDeportivoDTO> typeMap = modelMapper.typeMap(PrestamoMaterialDeportivo.class, PrestamoMaterialDeportivoDTO.class);
                typeMap.addMapping(PrestamoMaterialDeportivo::getInventarioMaterialDeportivo, PrestamoMaterialDeportivoDTO::setInventarioMaterialDeportivoDTO);
                return typeMap.map(prestamoMaterialDeportivo);
            });
            return new ResponseEntity<>(prestamoMaterialDeporitvoDTOPage, HttpStatus.OK);
        } else {
            log.warn("No hay datos en la tabla PrestamosAudioVisual");
            throw new MessageNotFoundException("No hay datos en la tabla PrestamosAudioVisual");
        }
    }

    @Override
    public ResponseEntity<PrestamoMaterialDeportivoDTO> createIn(Integer idInventarioMaterialDeporitvo, String idRfid, String nota, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new MessageBadRequestException("El campo de cantidad es obligatorio, y no puede ser menor a 1");
        }
        InventarioMaterialDeportivo inventarioMaterialDeportivo = inventarioMaterialDeportivoRepository.findById(idInventarioMaterialDeporitvo)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El objeto de inventario con id %d no registra", idInventarioMaterialDeporitvo)));
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
                Integer validarExistencia = inventarioMaterialDeportivo.getDisponible();
                LocalDateTime fechaActual = LocalDateTime.now();
                PrestamoMaterialDeportivo prestamoMaterialDeportivo = PrestamoMaterialDeportivo.builder()
                        .fechaPrestamo(fechaActual)
                        .cantidad(cantidad)
                        .nombre(nombre)
                        .apellido(apellido)
                        .idRfid(idRfid)
                        .idUdec(idUdec)
                        .nota(nota)
                        .inventarioMaterialDeportivo(inventarioMaterialDeportivo)
                        .build();
                if (validarExistencia <= 0) {
                    log.warn("El objeto no cuenta con stock para realizar el prestamo");
                    throw new MessageConflictException("El objeto no cuenta con stock para realizar el prestamo");
                } else if (cantidad > validarExistencia) {
                    log.warn(String.format("El objeto no cuenta con suficiente stock, solo dispone con %d", validarExistencia));
                    throw new MessageConflictException(String.format("El objeto no cuenta con suficiente stock, solo dispone con %d", validarExistencia));
                } else {
                    Integer restarExistencia = validarExistencia - cantidad;
                    inventarioMaterialDeportivo.setDisponible(restarExistencia);
                    inventarioMaterialDeportivoRepository.save(inventarioMaterialDeportivo);
                    PrestamoMaterialDeportivo prestamoMaterialDeportivoSave = prestamoMaterialDeportivoRepository.save(prestamoMaterialDeportivo);
                    URI location = ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("{/id}")
                            .buildAndExpand(prestamoMaterialDeportivoSave.getIdPrestamoMaterialDeportivo())
                            .toUri();
                    log.info("Prestamo creado exitosamente");
                    TypeMap<PrestamoMaterialDeportivo, PrestamoMaterialDeportivoDTO> typeMap = modelMapper.typeMap(PrestamoMaterialDeportivo.class, PrestamoMaterialDeportivoDTO.class);
                    typeMap.addMapping(PrestamoMaterialDeportivo::getInventarioMaterialDeportivo, PrestamoMaterialDeportivoDTO::setInventarioMaterialDeportivoDTO);
                    PrestamoMaterialDeportivoDTO prestamoMaterialDeportivoDTO = typeMap.map(prestamoMaterialDeportivoSave);
                    return ResponseEntity.created(location).body(prestamoMaterialDeportivoDTO);
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
    public ResponseEntity<PrestamoMaterialDeportivoDTO> createOut(Integer idPrestamoMaterialDeportivo, String observaciones) {
        PrestamoMaterialDeportivo prestamoMaterialDeportivo = prestamoMaterialDeportivoRepository.findById(idPrestamoMaterialDeportivo)
                .orElseThrow(() -> new MessageNotFoundException(String.format("El prestamo con ID %s no registra", idPrestamoMaterialDeportivo)));
        InventarioMaterialDeportivo inventarioMaterialDeportivo = inventarioMaterialDeportivoRepository.findById(prestamoMaterialDeportivo.getInventarioMaterialDeportivo().getIdMaterialDeportivo())
                .orElseThrow(() -> new MessageNotFoundException(String.format("El objeto de inventario con id %d no registra", prestamoMaterialDeportivo.getInventarioMaterialDeportivo().getIdMaterialDeportivo())));
        Integer sumarExistencia = inventarioMaterialDeportivo.getDisponible() + prestamoMaterialDeportivo.getCantidad();
        inventarioMaterialDeportivo.setDisponible(sumarExistencia);
        LocalDateTime fechaActual = LocalDateTime.now();
        prestamoMaterialDeportivo.setFechaDevolucion(fechaActual);
        prestamoMaterialDeportivo.setObservaciones(observaciones);
        inventarioMaterialDeportivoRepository.save(inventarioMaterialDeportivo);
        prestamoMaterialDeportivoRepository.save(prestamoMaterialDeportivo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(prestamoMaterialDeportivo.getIdPrestamoMaterialDeportivo())
                .toUri();
        log.info("Registro de devolución creado exitosamente");
        TypeMap<PrestamoMaterialDeportivo, PrestamoMaterialDeportivoDTO> typeMap = modelMapper.typeMap(PrestamoMaterialDeportivo.class, PrestamoMaterialDeportivoDTO.class);
        typeMap.addMapping(PrestamoMaterialDeportivo::getInventarioMaterialDeportivo, PrestamoMaterialDeportivoDTO::setInventarioMaterialDeportivoDTO);
        PrestamoMaterialDeportivoDTO prestamoMaterialDeportivoDTO = typeMap.map(prestamoMaterialDeportivo);
        return ResponseEntity.created(location).body(prestamoMaterialDeportivoDTO);
    }

    @Override
    public ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findAllByIdRfid(String idRfid) {
        try {
            ResponseEntity<CodigoUDTO.RfidDTO> response = new RestTemplate().exchange(
                    apiUrlEBR,
                    HttpMethod.GET,
                    null,
                    CodigoUDTO.RfidDTO.class,
                    idRfid
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                List<PrestamoMaterialDeportivo> prestamoMaterialDeportivos = Optional.ofNullable(prestamoMaterialDeportivoRepository.findAllByIdRfid(idRfid))
                        .orElseThrow(() -> new  MessageNotFoundException(String.format("No hay registro de prestamos para el usuario con ID de RFID %s", idRfid)));

                if (!prestamoMaterialDeportivos.isEmpty()) {
                    return getListResponseEntity(prestamoMaterialDeportivos);
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
    public ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findAllByIdUdec(String idUdec) {
        try {
            ResponseEntity<CodigoUDTO> response = new RestTemplate().exchange(
                    apiUrlEBU,
                    HttpMethod.GET,
                    null,
                    CodigoUDTO.class,
                    idUdec
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                List<PrestamoMaterialDeportivo> prestamoMaterialDeportivos = Optional.ofNullable(prestamoMaterialDeportivoRepository.findAllByIdUdec(idUdec))
                        .orElseThrow(() -> new  MessageNotFoundException(String.format("No hay registro de prestamos para el usuario con ID de UDEC %s", idUdec)));

                if (!prestamoMaterialDeportivos.isEmpty()) {
                    return getListResponseEntity(prestamoMaterialDeportivos);
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
    public ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByFechaPrestamo(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        List<PrestamoMaterialDeportivo> prestamoMaterialDeportivos = prestamoMaterialDeportivoRepository.findPrestamoMaterialDeportivosByFechaPrestamoBetween(fechaInicial, fechaFinal);
        String format = String.format("no hay prestamos registrados dentro de las fechas declaradas, desde: %tF %tR hasta %tF %tR", fechaInicial, fechaInicial, fechaFinal, fechaFinal);
        if (!prestamoMaterialDeportivos.isEmpty()) {
            return getListResponseEntity(prestamoMaterialDeportivos);
        } else {
            log.warn(format);
            throw new MessageNotFoundException(format);
        }
    }

    @Override
    public ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByFechaDevolucion(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        List<PrestamoMaterialDeportivo> prestamoMaterialDeportivos = prestamoMaterialDeportivoRepository.findPrestamoMaterialDeportivosByFechaDevolucionBetween(fechaInicial, fechaFinal);
        String format = String.format("no hay devoluciones registradas dentro de las fechas declaradas, desde: %tF %tR hasta %tF %tR", fechaInicial, fechaInicial, fechaFinal, fechaFinal);
        if (!prestamoMaterialDeportivos.isEmpty()) {
            return getListResponseEntity(prestamoMaterialDeportivos);
        } else {
            log.warn(format);
            throw new MessageNotFoundException(format);
        }
    }

    @Override
    public ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByFechaDevolucionIsEmpty() {
        List<PrestamoMaterialDeportivo> prestamoMaterialDeportivos = prestamoMaterialDeportivoRepository.findByFechaDevolucionIsNull();
        if (!prestamoMaterialDeportivos.isEmpty()) {
            return getListResponseEntity(prestamoMaterialDeportivos);
        } else {
            log.warn("No hay prestamos pendientes de entrega");
            throw new MessageNotFoundException("No hay prestamos pendientes de entrega");
        }
    }

    @Override
    public ResponseEntity<List<PrestamoMaterialDeportivoDTO>> findByInventarioMaterialDeportivoNombreBeLike(String nombreObjeto) {
        List<PrestamoMaterialDeportivo> prestamoMaterialDeportivos = prestamoMaterialDeportivoRepository.findByInventarioMaterialDeportivoNombreContainingIgnoreCase(nombreObjeto);
        if (!prestamoMaterialDeportivos.isEmpty()) {
            return getListResponseEntity(prestamoMaterialDeportivos);
        } else {
            log.warn(String.format("No hay prestamos sobre el objeto que contiene el nombre %s", nombreObjeto));
            throw new MessageBadRequestException(String.format("No hay prestamos sobre el objeto que contiene el nombre %s", nombreObjeto));
        }
    }
}
