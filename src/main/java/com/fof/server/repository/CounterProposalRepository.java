package com.fof.server.repository;

import com.fof.server.model.entity.CounterProposalDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CounterProposalRepository extends JpaRepository<CounterProposalDTO, Integer> {
   @Query("""
            SELECT c
            FROM CounterProposalDTO c
            WHERE c.entrepreneurId.id = :id
            """)
    List<CounterProposalDTO> findByEntrepreneurId(Integer id);

    @Query("""
            SELECT c
            FROM CounterProposalDTO c
            WHERE c.listingId.listingId = :id
            """)
    List<CounterProposalDTO> findByListingId(Integer id);

    @Query("""
            SELECT c
            FROM CounterProposalDTO c
            WHERE c.investorId.id = :id
            """)
    List<CounterProposalDTO> findByInvestorId(Integer id);

    @Query("""
            SELECT c
            FROM CounterProposalDTO c
            WHERE c.listingId.listingId = :listingid
            AND c.investorId.id = :investorid
            """)
    Optional<CounterProposalDTO> findByListingInvestorId(Integer listingid, Integer investorid);


}
