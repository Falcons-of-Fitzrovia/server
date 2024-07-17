package com.fof.server.repository;

import com.fof.server.model.entity.ListingDTO;
import com.fof.server.model.entity.ListingSubscriptionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ListingSubscriptionRepository extends JpaRepository<ListingSubscriptionDTO, Integer> {

    //Find by listingId
    @Query("""
            SELECT l 
            FROM ListingSubscriptionDTO l 
            WHERE l.id = :listingId
            """)
    Optional<ListingSubscriptionDTO> findByListingId(ListingDTO listingId);
}
