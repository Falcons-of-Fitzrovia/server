package com.fof.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "business")
public class BusinessDTO {

    @Id
    @GeneratedValue
    private String businessName;
    private String businessRegistration;
    private String businessDescription;
    private String contactNo;
    private String firstLine;
    private String secondLine;
    private String town;
    private String district;
    private String businesswebsite;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "entrepreneurID", referencedColumnName = "id")
    private EntrepreneurDTO entrepreneurID;

}
