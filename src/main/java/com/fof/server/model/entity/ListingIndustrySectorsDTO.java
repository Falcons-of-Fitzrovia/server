package com.fof.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="listing_industry_sectors")
public class ListingIndustrySectorsDTO {

    @EmbeddedId
    private ListingIndustrySectorsDTO.CompositeKey id;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class CompositeKey implements Serializable {
        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "listingId", referencedColumnName = "listingId")
        private ListingDTO listingId;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "sectorId", referencedColumnName = "sectorId")
        private IndustrySectorDTO sectorId;
    }
}