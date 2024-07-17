package com.fof.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "counter-proposal")
@PrimaryKeyJoinColumn(name = "Id")

public class CounterProposalDTO {
    @Id
    @GeneratedValue
    private Integer Id;
    private Integer amount;
    private Integer returnEquityPercentage;
    private Integer returnUnitProfitPercentage;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "listingId", referencedColumnName = "listingId")
    private ListingDTO listingId;

    @ManyToOne
    @JoinColumn(name = "entrepreneurId", referencedColumnName = "id")
    private EntrepreneurDTO entrepreneurId;

    @ManyToOne
    @JoinColumn(name="investorId",referencedColumnName = "id")
    private InvestorDTO investorId;
}
