package com.fof.server.repository;

import com.fof.server.model.entity.*;
import com.ventureverse.server.model.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface Investor_InterestedListingRepository extends JpaRepository<InvestorInterestedListingDTO, Integer> {

//    @Query("""
//            SELECT investorInterestedListingDTO
//            FROM InvestorInterestedListingDTO investorInterestedListingDTO
//            WHERE investorInterestedListingDTO.id.investorId.id = :id
//            AND investorInterestedListingDTO.amountFinalized IS NULL
//            """)
//    List<InvestorInterestedListingDTO> findByInvestorId(Integer id);

    @Query("""
            SELECT i
            FROM InvestorInterestedListingDTO i
            WHERE i.id.investorId.id = :investor
            AND i.finalizedDate IS NOT NULL
            """)
    List<InvestorInterestedListingDTO> findByInvestorId(Integer investor);

    @Query("""
            SELECT investorInterestedListingDTO
            FROM InvestorInterestedListingDTO investorInterestedListingDTO
            WHERE investorInterestedListingDTO.id.listingId = :id
            """)
    Optional<InvestorInterestedListingDTO> findByListing(ListingDTO id);

    @Query("""
            SELECT investorInterestedListingDTO
            FROM InvestorInterestedListingDTO investorInterestedListingDTO
            WHERE investorInterestedListingDTO.id.listingId.listingId = :id
            """)
    InvestorInterestedListingDTO findByListingId(Integer id);

// public interface Investor_InterestedListingRepository extends JpaRepository<InvestorInterestedListingDTO, Integer> {

//     @Query("""
//             SELECT investorInterestedListingDTO
//             FROM InvestorInterestedListingDTO investorInterestedListingDTO
//             WHERE investorInterestedListingDTO.id.investorId.id = :id
//             AND investorInterestedListingDTO.amountFinalized IS NULL
//             """)
//     List<InvestorInterestedListingDTO> findByInvestorId(Integer id);

//    @Query("""
//            SELECT investorInterestedListingDTO
//            FROM InvestorInterestedListingDTO investorInterestedListingDTO
//            WHERE investorInterestedListingDTO.id.listingId = :id
//            """)
//    Optional<InvestorInterestedListingDTO> findByListing(ListingDTO id);

//     @Query("""
//             SELECT investorInterestedListingDTO
//             FROM InvestorInterestedListingDTO investorInterestedListingDTO
//             WHERE investorInterestedListingDTO.id.listingId.listingId = :id
//             """)
//     InvestorInterestedListingDTO findByListingId(Integer id);

    @Query("""
            SELECT i\s
            FROM InvestorInterestedListingDTO i\s
            WHERE i.finalizedDate IS NOT NULL
            """)
    List<InvestorInterestedListingDTO> findCompletedListings();

    @Query("""
            SELECT i\s
            FROM InvestorInterestedListingDTO i\s
            WHERE i.finalizedDate IS NULL 
            AND i.id.investorId.id = :id
            """)
    List<InvestorInterestedListingDTO> findPendingListings(Integer id);

    @Query("""
            SELECT i\s
            FROM InvestorInterestedListingDTO i\s
            WHERE i.finalizedDate IS NULL 
            AND i.id.investorId.id = :id
            AND i.entrepreneurProofDocument IS NULL
            AND i.investorProofDocument IS NULL
            """)
    List<InvestorInterestedListingDTO> findPendingListingsOfInvestor(Integer id);

    @Query("""
            SELECT i\s
            FROM InvestorInterestedListingDTO i\s
            WHERE i.finalizedDate IS NOT NULL 
            AND i.id.listingId.entrepreneurId.id = :id
            """)
    List<InvestorInterestedListingDTO> findByEntrepreneurId(Integer id);

    @Query("""
            SELECT i\s
            FROM InvestorInterestedListingDTO i\s
            WHERE i.finalizedDate IS NULL
            AND i.id.listingId.listingId = :id
            """)
    List<InvestorInterestedListingDTO> findByPendingListingId(Integer id);

    @Query("SELECT investorInterestedListingDTO " +
            "FROM InvestorInterestedListingDTO investorInterestedListingDTO " +
            "WHERE investorInterestedListingDTO.id.listingId = :listingId " +  // Use 'listingId' instead of 'id'
            "AND investorInterestedListingDTO.id.investorId.id = :investorId")
    Optional<InvestorInterestedListingDTO> findByListingInvestor(@Param("listingId") ListingDTO listingId, @Param("investorId") Integer investorId);

    @Query("""
            SELECT i\s
            FROM InvestorInterestedListingDTO i\s
            WHERE i.status='Investor_Finalized'
            AND i.id.listingId.listingId = :id
            AND i.entrepreneurProofDocument IS NULL
            """)
    List<InvestorInterestedListingDTO> findByEntreprenuerListingId(Integer id);


    //Function to get the amount of investment completed for a listing.It is the sum of all the finalized amounts of the investors related to the listing
    @Query("""
            SELECT SUM(i.amountFinalized)
            FROM InvestorInterestedListingDTO i
            WHERE i.id.listingId = :listing AND i.status='Finalized'
            """)
    Integer getCompletedInvestment(ListingDTO listing);

    //Get the interested investors of a listing
    @Query("""
            SELECT i
            FROM InvestorInterestedListingDTO i
            WHERE i.id.listingId = :listing
            """)
    List<InvestorInterestedListingDTO> getInterestedInvestors(ListingDTO listing);

    @Query("""
            SELECT i\s
            FROM InvestorInterestedListingDTO i\s
            WHERE i.id.listingId.listingId = :id
            AND i.entrepreneurProofDocument IS NOT NULL
            AND i.investorProofDocument IS NOT NULL
            AND i.finalizedDate IS NULL
            """)
    List<InvestorInterestedListingDTO> finalizeListings(Integer id);

    @Query("""
            SELECT i.entrepreneurProofDocument
            FROM InvestorInterestedListingDTO i\s
            WHERE i.id.listingId.listingId = :listingId
            AND i.id.investorId.id = :investorId
            AND i.entrepreneurProofDocument IS NOT NULL
            """)
    String findByEntrepreneurFinalizeDoc(@Param("listingId") Integer listingId, @Param("investorId") Integer investorId);

    @Query("""
            SELECT i.investorProofDocument
            FROM InvestorInterestedListingDTO i\s
            WHERE i.id.listingId.listingId = :listingId
            AND i.id.investorId.id = :investorId
            AND i.investorProofDocument IS NOT NULL
            """)
    String findByListingInvestorId(@Param("listingId") Integer listingId, @Param("investorId") Integer investorId);

    @Query("""
            SELECT COUNT(e)
            FROM InvestorInterestedSectorDTO e
            WHERE e.id.investorId = :id
                                      
    """)
    int getCountByID(ListingDTO id);
    //Long getCountByID(ListingDTO listingDTO);


    @Query("""
            SELECT COUNT(e)
            FROM InvestorInterestedListingDTO e
            WHERE e.id.investorId = :id
    """)

    Long getCountByInvestorId(InvestorDTO id);

    @Query("""
            SELECT e
            FROM InvestorInterestedListingDTO e
            WHERE e.id.investorId = :id
    """)
    List<InvestorInterestedListingDTO> findAllByInvestorId(InvestorDTO id);



    @Query("""
            SELECT MIN(e.finalizedDate)
            FROM InvestorInterestedListingDTO e
            WHERE e.id.investorId = :individualInvestor
    """)
    Date getLastDate(IndividualInvestorDTO individualInvestor);

    @Query("""
            SELECT MIN(e.finalizedDate)
            FROM InvestorInterestedListingDTO e
            WHERE e.id.investorId = :enterpriseInvestor
    """)
    Date getLastDate1(EnterpriseInvestorDTO enterpriseInvestor);

    @Query("""
            SELECT e
            FROM InvestorInterestedListingDTO e
            WHERE e.id.listingId =:id
    """)
    List<InvestorInterestedListingDTO> findAllByListingId(ListingDTO id);

    //Get the investor interested listings of a listing
    @Query("""
            SELECT i
            FROM InvestorInterestedListingDTO i
            WHERE i.id.listingId = :listingDTO
            """)
    List<InvestorInterestedListingDTO> findByListingid(ListingDTO listingDTO);
    @Query("""
            SELECT i 
            FROM InvestorInterestedListingDTO i 
            WHERE i.id.investorId.id = :id
            """)
    List<InvestorInterestedListingDTO> findAllByInvestorId(Integer id);

    @Query("""
            SELECT i
            FROM InvestorInterestedListingDTO i 
            WHERE
            i.id.listingId.listingId = :listingId 
            AND i.id.investorId.id = :investorId
            """)
    InvestorInterestedListingDTO findByInvestorIdAndListingId(@Param("investorId") Integer investorId, @Param("listingId") int listingId);
    
//    @Query("SELECT investorInterestedListingDTO " +
//            "FROM InvestorInterestedListingDTO investorInterestedListingDTO " +
//            "WHERE investorInterestedListingDTO.id.listingId = :listingId " +  // Use 'listingId' instead of 'id'
//            "AND investorInterestedListingDTO.id.investorId.id = :investorId")
//    Optional<InvestorInterestedListingDTO> findByListingInvestor(@Param("listingId") ListingDTO listingId, @Param("investorId") Integer investorId);


    @Query("""
            SELECT i
            FROM InvestorInterestedListingDTO i 
            WHERE
            i.id.listingId.listingId = :listingId
            AND i.id.investorId.id = :investorId
            """)
    Optional<InvestorInterestedListingDTO> findByInvestorIdAndListingId2(@Param("investorId") Integer investorId, @Param("listingId") int listingId);
}
