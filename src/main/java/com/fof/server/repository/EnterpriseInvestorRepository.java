package com.fof.server.repository;

import com.fof.server.enumeration.Role;
import com.fof.server.enumeration.Status;
import com.fof.server.model.entity.EnterpriseInvestorDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnterpriseInvestorRepository extends JpaRepository<EnterpriseInvestorDTO, Integer> {
//    Functtion to get the last inserted id
    @Query(value = "SELECT MAX(enterpriseinvestor_id) FROM enterprise_investor", nativeQuery = true)
    Integer getLastInsertedId();


    //function to save a record into the database when investor is and sector id is given
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO investor_interested_sector (investor_id, sector_id) " +
            "SELECT :investor, :sectorId", nativeQuery = true)
    int saveInvestorSector(@Param("investor") Integer investor, @Param("sectorId") Integer sectorId);

    List<EnterpriseInvestorDTO> findByRole(Role role);
    @Query("""
    SELECT e
    FROM EnterpriseInvestorDTO e 
    WHERE e.id = :id
    
""")
    EnterpriseInvestorDTO findByenterpriseinvestorId(int id);

    List<EnterpriseInvestorDTO> findByApprovalStatus(Status approvalStatus);

}
