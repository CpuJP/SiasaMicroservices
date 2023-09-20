package com.siasa.prestamos.repository;

import com.siasa.prestamos.entity.InventarioAudioVisual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface InventarioAudioVisualRepository extends JpaRepository<InventarioAudioVisual, Integer>, JpaSpecificationExecutor<InventarioAudioVisual> {

    @Transactional(readOnly = true)
    List<InventarioAudioVisual> findByNombreContaining(String nombre);

    @Transactional(readOnly = true)
    boolean existsByNombre(String nombre);
}