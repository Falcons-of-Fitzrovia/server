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
@Table(name = "investor_interested_sector")
public class InvestorInterestedSectorDTO implements Serializable {

    @EmbeddedId
    private CompositeKey id;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class CompositeKey implements Serializable {
        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "investorId", referencedColumnName = "investorId")
        private InvestorDTO investorId;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "sectorId", referencedColumnName = "sectorId")
        private IndustrySectorDTO sectorId;
    }

}
