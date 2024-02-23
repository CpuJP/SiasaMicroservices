package com.siasa.principalfailover.repository;

import com.siasa.principalfailover.entity.Rfid;
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

    @Transactional(readOnly = true)
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Rfid r LEFT JOIN CodigoU c ON r.idRfid = c.rfid.idRfid WHERE r.idRfid = :rfid")
    boolean isRfidLinkedToCodigoU(String rfid);
}