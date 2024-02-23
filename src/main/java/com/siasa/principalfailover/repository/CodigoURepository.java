package com.siasa.principalfailover.repository;

import com.siasa.principalfailover.entity.CodigoU;
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