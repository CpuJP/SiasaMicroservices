package com.siasa.prestamos.repository;

import com.siasa.prestamos.entity.PrestamoAudioVisual;
import com.siasa.prestamos.entity.PrestamoMaterialDeportivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface PrestamoMaterialDeportivoRepository extends JpaRepository<PrestamoMaterialDeportivo, Integer>, JpaSpecificationExecutor<PrestamoMaterialDeportivo> {

    @Transactional(readOnly = true)
    List<PrestamoMaterialDeportivo> findPrestamoMaterialDeportivosByFechaPrestamoBetween(LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<PrestamoMaterialDeportivo> findPrestamoMaterialDeportivosByFechaDevolucionBetween(LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<PrestamoMaterialDeportivo> findAllByIdRfid(String idRfid);

    @Transactional(readOnly = true)
    List<PrestamoMaterialDeportivo> findAllByIdUdec(String idUdec);

    @Transactional(readOnly = true)
    List<PrestamoMaterialDeportivo> findByFechaDevolucionIsNull();

    @Transactional(readOnly = true)
    List<PrestamoMaterialDeportivo> findByInventarioMaterialDeportivoNombreContainingIgnoreCase(String nombre);
}