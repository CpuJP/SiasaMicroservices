package com.siasa.prestamos.repository;

import com.siasa.prestamos.entity.InventarioAudioVisual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface InventarioAudioVisualRepository extends JpaRepository<InventarioAudioVisual, Integer>, JpaSpecificationExecutor<InventarioAudioVisual> {
}