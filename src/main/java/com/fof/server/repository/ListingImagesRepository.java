package com.fof.server.repository;

import com.fof.server.model.entity.ListingDTO;
import com.fof.server.model.entity.ListingImagesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ListingImagesRepository extends JpaRepository<ListingImagesDTO, Integer> {

    //Function to get the listing image names using the listing id
    @Query("""
            SELECT l.id.image 
            FROM ListingImagesDTO l 
            WHERE l.id.listingId = :id
            """)
    List<String> getListingImages(ListingDTO id);



}
