package com.siasa.prestamos.repository;

import com.siasa.prestamos.entity.InventarioMaterialDeportivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface InventarioMaterialDeportivoRepository extends JpaRepository<InventarioMaterialDeportivo, Integer>, JpaSpecificationExecutor<InventarioMaterialDeportivo> {

    @Transactional(readOnly = true)
    List<InventarioMaterialDeportivo> findByNombreContaining(String nombre);

    @Transactional(readOnly = true)
    boolean existsByNombre(String nombre);
}
