package com.fof.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "listing")
public class ListingDTO {

    @Id
    @GeneratedValue
    private Integer listingId;
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    private String thumbnail;
    private String pitchingVideo;
    private String intention;
    private Timestamp businessStartDate;
    private Integer businessDuration;
    private Integer lifetimeSales;
    private Integer lastYearGrossIncome;
    private Integer lastYearNetIncome;
    private Integer salesProjectionThisYear;
    private Integer salesProjectionNextYear;
    private String projectionMethod;
    private String outsideSources;
    @Column(columnDefinition = "text")
    private String outsideSourceDescription;
    private String attemptsToGrow;
    private String awards;
    private String uniqueSellingProposition;
    private String stage;
    private Integer expectedAmount;
    private Integer returnUnitProfitPercentage;
    private Integer returnEquityPercentage;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="subscriptionId", referencedColumnName = "subscriptionId")
    private SubscriptionDTO subscriptionType;


    private Timestamp publishedDate;
    private String status;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "entrepreneurId", referencedColumnName = "id")
    private EntrepreneurDTO entrepreneurId;


}