package com.fof.server.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "entrepreneur")
@PrimaryKeyJoinColumn(name = "entrepreneurId")
public class EntrepreneurDTO extends UserDTO {

    private String firstname;
    private String lastname;
    private String nic;
    private String gender;
    private String policeReport;
    private String incomeStatement;
    private String collaboratorDetails;
    private String felony;
    private String lawSuit;
    private String felonyDescription;
    private String businessName;
    private String businessContact;
    private String bfirstLineAddress;
    private String bsecondLineAddress;
    private String btown;
    private String bdistrict;
    private String businessWebsite;
    private String businessEmail;
    private String businessDescription;
    private String businessRegDoc;

}