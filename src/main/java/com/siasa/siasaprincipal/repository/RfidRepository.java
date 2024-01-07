package com.siasa.siasaprincipal.repository;

import com.siasa.siasaprincipal.entity.Rfid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface RfidRepository extends JpaRepository<Rfid, String>, JpaSpecificationExecutor<Rfid> {

    @Transactional(readOnly = true)
    @Query("SELECT r FROM Rfid r LEFT JOIN CodigoU c ON r.idRfid = c.rfid.idRfid WHERE c.idCodigoU IS NULL")
    List<Rfid> findRfidWithoutCodigoU();
}