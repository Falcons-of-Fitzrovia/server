package com.fof.server.model.normal;

import com.fof.server.enumeration.Chat;
import com.fof.server.enumeration.Role;
import com.fof.server.model.entity.UserDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class DetailsDTO {

    // ALL USERS
    private String email;
    private String contactNumber;
    private String firstLineAddress;
    private String secondLineAddress;
    private String town;
    private String district;
    private byte[] profileImage;

    // ADMIN | ENTREPRENEUR |INDIVIDUAL INVESTOR
    private String firstname;
    private String lastname;
    private String nic;
    private String gender;

    // ENTREPRENEUR |INDIVIDUAL INVESTOR | ENTERPRISE INVESTOR
    private String policeReport;
    private String incomeStatement;
    private String collaboratorDetails;
    private String felony;
    private String lawSuit;
    private String felonyDescription;

    // INDIVIDUAL INVESTOR | ENTERPRISE INVESTOR
    private String financialDocument;


    // ENTERPRISE INVESTOR | ENTREPRENEUR
    private String businessName;
    private String businessRegistration;

    // ENTREPRENEUR
    private String businessContact;
    private String bfirstLineAddress;
    private String bsecondLineAddress;
    private String btown;
    private String bdistrict;
    private String businessWebsite;
    private String businessEmail;
    private String businessDescription;
    private String businessRegDoc;
    private List<Integer> sectorId;

    //HOME
    private String title;
    private String description;
    private byte[] thumbnail;
    private String video;
    private Integer expectedAmount;
    private Integer returnEquityPercentage;
    private Integer returnUnitProfitPercentage;
    private Float investmentPercentage;
    private String investorName;
    private String investorQuote;
    private String investorType;
    
    //COMPLAIN
    private Integer complainId;
    private Integer adminId;
    private String actionDescription;

    //Schedule
    private Integer meetingId;
    private String date;
    private String time;
    private Integer entrepreneurId;
    private Integer investorId;

    //CHAT
    private Integer id;
    private UserDTO sender;
    private UserDTO receiver;
    private String roomOwnerName;
    private byte[] roomOwnerImage;
    @Enumerated(EnumType.STRING)
    private Role roomOwnerRole;
    @Enumerated(EnumType.STRING)
    private Chat roomOwnerStatus;
    private Timestamp roomOwnerLastLogin;
    private String message;
    private Timestamp timestamp;
    @Enumerated(EnumType.STRING)
    private Chat type;
    @Enumerated(EnumType.STRING)
    private Chat status;
}
