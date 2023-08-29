package com.siasa.siasaprincipal.repository;

import com.siasa.siasaprincipal.entity.CodigoU;
import com.siasa.siasaprincipal.entity.Rfid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface CodigoURepository extends JpaRepository<CodigoU, String>, JpaSpecificationExecutor<CodigoU> {

    @Transactional(readOnly = true)
    Optional<CodigoU> findByRfidIdRfid(String rfid);

    @Transactional(readOnly = true)
    boolean existsByRfidIdRfid(String rfidId);

}