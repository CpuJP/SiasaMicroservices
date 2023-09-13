package com.siasa.siasaprincipal.repository;

import com.siasa.siasaprincipal.entity.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface LaboratorioRepository extends JpaRepository<Laboratorio, Integer>, JpaSpecificationExecutor<Laboratorio> {

    @Transactional(readOnly = true)
    List<Laboratorio> findByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = true)
    boolean existsByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = true)
    Optional<Laboratorio> findFirstByCodigoUIdCodigoUOrderByFechaIngresoDesc(String idCodigoU);

    @Transactional(readOnly = false)
    void deleteAllByCodigoUIdCodigoU(String idCodigoU);
}