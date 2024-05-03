package com.siasa.principalfailover.repository;

import com.siasa.principalfailover.entity.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface BibliotecaRepository extends JpaRepository<Biblioteca, Integer>, JpaSpecificationExecutor<Biblioteca> {

    @Transactional(readOnly = true)
    List<Biblioteca> findByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = true)
    boolean existsByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = false)
    void deleteAllByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = true)
    List<Biblioteca> findBibliotecasByFechaIngresoBetween(LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<Biblioteca> findBibliotecasByCodigoUIdCodigoUAndFechaIngresoBetween(String idCodigoU, LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    @Transactional(readOnly = true)
    List<Biblioteca> findBibliotecasByCodigoURfidIdRfid(String idRfid);
}