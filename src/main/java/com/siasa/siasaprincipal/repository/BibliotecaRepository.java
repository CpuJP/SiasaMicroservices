package com.siasa.siasaprincipal.repository;

import com.siasa.siasaprincipal.entity.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface BibliotecaRepository extends JpaRepository<Biblioteca, Integer>, JpaSpecificationExecutor<Biblioteca> {

    @Transactional(readOnly = true)
    List<Biblioteca> findByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = true)
    boolean existsByCodigoUIdCodigoU(String idCodigoU);
}