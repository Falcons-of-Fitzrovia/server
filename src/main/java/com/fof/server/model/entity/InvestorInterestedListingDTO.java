package com.fof.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "investor-interested-listing")
public class InvestorInterestedListingDTO implements Serializable {

    @EmbeddedId
    private CompositeKey id;
    private Integer amountFinalized;
    private Integer returnEquityPercentage;
    private Integer returnUnitProfitPercentage;
    private Date finalizedDate;
    private String status;
    private String investorProofDocument;
    private String entrepreneurProofDocument;
    private String systemGeneratedDocument;
    private Date interestedDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class CompositeKey implements Serializable {
        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "investorId", referencedColumnName = "investorId")
        private InvestorDTO investorId;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "listingId", referencedColumnName = "listingId")
        private ListingDTO listingId;
}

}