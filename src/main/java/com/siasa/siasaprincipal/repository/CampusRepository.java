package com.siasa.siasaprincipal.repository;

import com.siasa.siasaprincipal.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
@EnableJpaRepositories
public interface CampusRepository extends JpaRepository<Campus, Integer>, JpaSpecificationExecutor<Campus> {

    @Transactional(readOnly = true)
    List<Campus> findByCodigoUIdCodigoU(String idCodigoU);

    @Transactional(readOnly = true)
    boolean existsByCodigoUIdCodigoU(String idCodigoU);
}