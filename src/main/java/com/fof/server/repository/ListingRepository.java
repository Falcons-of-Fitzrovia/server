package com.fof.server.repository;

import com.fof.server.model.entity.EntrepreneurDTO;
import com.fof.server.model.entity.InvestorInterestedListingDTO;
import com.fof.server.model.entity.ListingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ListingRepository extends JpaRepository<ListingDTO, Integer> {
    //Find the latest listingId in the database
    @Query("""
            SELECT l 
            FROM ListingDTO l 
            ORDER BY l.listingId 
            DESC LIMIT 1
            """)
    ListingDTO findLastInsertedListing();

    //Find the latest listing from an entrepreneur using the entrepreneurId
    @Query("""
            SELECT l 
            FROM ListingDTO l 
            WHERE l.entrepreneurId = :id 
            ORDER BY l.listingId 
            DESC LIMIT 1
            """)
    ListingDTO findLatestListing(EntrepreneurDTO id);

    @Query("""
            SELECT l.entrepreneurId
            FROM ListingDTO l 
            WHERE l.listingId = :id
            """)
    EntrepreneurDTO getEntrepreneur(Integer id);

    //Get the listings where it has an entry in the investor_interested_listing table and entrepreneur_proof_document and investor_proof_document is not null
    @Query("""
            SELECT l
            FROM ListingDTO l 
            WHERE l.listingId IN (
                SELECT i.id.listingId.listingId 
                FROM InvestorInterestedListingDTO i 
                WHERE i.id.listingId.listingId = l.listingId 
                AND i.entrepreneurProofDocument IS NOT NULL 
                AND i.investorProofDocument IS NOT NULL
                AND i.finalizedDate IS NULL
            )
            """)
    List<ListingDTO> getAllFinalizedListings();

    @Query("""
            SELECT l.status
            FROM ListingDTO l
            WHERE l.entrepreneurId = :id
            AND l.listingId = (
                SELECT MAX(l.listingId)
                FROM ListingDTO l
                WHERE l.entrepreneurId = :id
            )
            """)
    //Function to find the latest listing status
    String findLatestListingStatus(EntrepreneurDTO id);
    @Query("""
           SELECT entrepreneurId,count(*)   
            from ListingDTO 
           group by entrepreneurId
            """)
    List<Object[]> entrepreneurLeaderboard();

    @Query("""
            SELECT COUNT(e)
            FROM ListingDTO e
            WHERE e.entrepreneurId = :id
    """)
    Long getCountById(EntrepreneurDTO id);

    @Query("""
            SELECT l 
            FROM ListingDTO l 
            WHERE l.entrepreneurId.id= :id
            """)
    List<ListingDTO> findByEntrepreneurId(Integer id);


    List<ListingDTO> findAllByEntrepreneurId(EntrepreneurDTO entrepreneurId);

    List<InvestorInterestedListingDTO> findAllByListingId(ListingDTO listingId );
}
