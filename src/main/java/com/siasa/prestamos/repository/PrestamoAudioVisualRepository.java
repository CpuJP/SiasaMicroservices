package com.siasa.prestamos.repository;

import com.siasa.prestamos.entity.PrestamoAudioVisual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface PrestamoAudioVisualRepository extends JpaRepository<PrestamoAudioVisual, Integer>, JpaSpecificationExecutor<PrestamoAudioVisual> {

    @Transactional(readOnly = true)
    List<PrestamoAudioVisual> findPrestamoAudioVisualsByFechaPrestamoBetween(LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<PrestamoAudioVisual> findPrestamoAudioVisualsByFechaDevolucionBetween(LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<PrestamoAudioVisual> findAllByIdRfid(String idRfid);

    @Transactional(readOnly = true)
    List<PrestamoAudioVisual> findAllByIdUdec(String idUdec);

    @Transactional(readOnly = true)
    List<PrestamoAudioVisual> findByFechaDevolucionIsNull();

    @Transactional(readOnly = true)
    List<PrestamoAudioVisual> findByInventarioAudioVisualNombreContainingIgnoreCase(String nombre);
}