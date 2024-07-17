package com.fof.server.repository;

import com.fof.server.model.entity.InvestorDTO;
import com.fof.server.model.entity.InvestorInterestedListingDTO;
import com.fof.server.model.entity.ListingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvestorInterestedListingRepository extends JpaRepository<InvestorInterestedListingDTO, Integer> {

    @Query("""
        SELECT sum(i.amountFinalized)
        FROM InvestorInterestedListingDTO i
        WHERE i.id.listingId = :listing
    """)
    Integer findFinalizedAmount(ListingDTO listing);

    @Query("""
        SELECT distinct (i.id.investorId)
        FROM InvestorInterestedListingDTO i
    """)
    List<InvestorDTO> findInvestors();
}
