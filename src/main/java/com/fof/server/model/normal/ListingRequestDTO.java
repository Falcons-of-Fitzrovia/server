package com.fof.server.model.normal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListingRequestDTO {
    private String title;
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
    private String outsideSourceDescription;
    private String attemptsToGrow;
    private String awards;
    private String uniqueSellingProposition;
    private String stage;
    private Integer expectedAmount;
    private Integer returnUnitProfitPercentage;
    private Integer returnEquityPercentage;
    private Integer subscriptionType;
    private Timestamp publishedDate;
    private String status;
    private Integer entrepreneurId;
    //List of sectorIDs
    private List<Integer> sectorId;

    //List of images
    private List<String> images;
}
