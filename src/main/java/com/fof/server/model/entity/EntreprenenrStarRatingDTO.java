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
@Table(name = "entreprenenr-star-rating")
public class EntreprenenrStarRatingDTO  implements Serializable  {

    @EmbeddedId
    private CompositeKey id;
    private Integer starRating;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class CompositeKey implements Serializable {

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "entrepreneurId", referencedColumnName = "entrepreneurId")
        private EntrepreneurDTO entrepreneurId;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "id", referencedColumnName = "id")
        private UserDTO id;


    }
}
