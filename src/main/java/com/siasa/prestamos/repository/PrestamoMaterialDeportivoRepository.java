package com.siasa.prestamos.repository;

import com.siasa.prestamos.entity.PrestamoMaterialDeportivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PrestamoMaterialDeportivoRepository extends JpaRepository<PrestamoMaterialDeportivo, Integer>, JpaSpecificationExecutor<PrestamoMaterialDeportivo> {
}