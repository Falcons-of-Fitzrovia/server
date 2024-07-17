package com.fof.server.repository;

import com.fof.server.enumeration.Status;
import com.fof.server.model.entity.IndividualInvestorDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IndividualInvestorRepository extends JpaRepository<IndividualInvestorDTO, Integer> {

    @Query(value = "SELECT MAX(individualinvestor_id) FROM individual_investor", nativeQuery = true)
    Integer getLastInsertedId();

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO investor_interested_sector (investor_id, sector_id) " +
            "SELECT :investor, :sectorId", nativeQuery = true)
    int saveInvestorSector(@Param("investor") Integer investor, @Param("sectorId") Integer sectorId);

    List<IndividualInvestorDTO> findByApprovalStatus(Status approvalStatus);

}
