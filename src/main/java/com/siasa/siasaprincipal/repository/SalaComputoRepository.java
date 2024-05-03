package com.siasa.siasaprincipal.repository;

import com.siasa.siasaprincipal.entity.SalaComputo;
import org.springframework.cglib.core.Local;
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
public interface SalaComputoRepository extends JpaRepository<SalaComputo, Integer>, JpaSpecificationExecutor<SalaComputo> {

    @Transactional(readOnly = true)
    List<SalaComputo> findByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = true)
    boolean existsByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = true)
    Optional<SalaComputo> findFirstByCodigoUIdCodigoUOrderByFechaIngresoDesc(String idCodigoU);

    @Transactional(readOnly = false)
    void deleteAllByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = true)
    List<SalaComputo> findSalaComputosByFechaIngresoBetween(LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<SalaComputo> findSalaComputosByFechaSalidaBetween(LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<SalaComputo> findSalaComputosByCodigoUIdCodigoUAndFechaIngresoBetween(String idCodigoU, LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<SalaComputo> findSalaComputosByCodigoUIdCodigoUAndFechaSalidaBetween(String idCodigoU, LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<SalaComputo> findSalaComputosByCodigoURfidIdRfid(String idRfid);
}