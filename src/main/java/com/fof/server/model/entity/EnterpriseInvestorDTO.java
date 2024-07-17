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
@Table(name = "enterprise_investor")
@PrimaryKeyJoinColumn(name = "enterpriseinvestorId")
public class EnterpriseInvestorDTO extends InvestorDTO {

    private String businessName;
    private String businessRegistration;

}
