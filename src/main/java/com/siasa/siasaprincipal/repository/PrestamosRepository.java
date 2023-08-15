package com.siasa.siasaprincipal.repository;

import com.siasa.siasaprincipal.entity.Prestamos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface PrestamosRepository extends JpaRepository<Prestamos, Integer>, JpaSpecificationExecutor<Prestamos> {
}