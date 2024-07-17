package com.fof.server.repository;

import com.fof.server.model.entity.EnterpriseInvestorDTO;
import com.fof.server.model.entity.IndividualInvestorDTO;
import com.fof.server.model.entity.InvestorInterestedSectorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface InvestorInterestedSectorRepository extends JpaRepository<InvestorInterestedSectorDTO, Integer> {

    @Query("SELECT i FROM InvestorInterestedSectorDTO i WHERE i.id.investorId.id = :investorId")
    List<InvestorInterestedSectorDTO> findByInvestorId(Integer investorId);
    @Query("""
            SELECT COUNT(e)
            FROM InvestorInterestedSectorDTO e
            WHERE e.id.investorId = :individualInvestor
    """)
    Integer getCountByInverstorId(IndividualInvestorDTO individualInvestor);

    @Query("""
            SELECT COUNT(e)
            FROM InvestorInterestedSectorDTO e
            WHERE e.id.investorId = :enterpriseInvestor
    """)
    Integer getCountByInverstorId1(EnterpriseInvestorDTO enterpriseInvestor);
}
