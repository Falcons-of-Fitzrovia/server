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
@Table(name = "listing-subscription")
public class ListingSubscriptionDTO implements Serializable{

    @EmbeddedId
    private CompositeKey id;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class CompositeKey implements Serializable{
        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "subscriptionId", referencedColumnName = "subscriptionId")
        private SubscriptionDTO subscriptionId;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "listingId", referencedColumnName = "listingId")
        private ListingDTO listingId;
    }

}
