package com.fof.server.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Rating")
public class RatingDTO {
    @Id
    @GeneratedValue
    private Integer entrepreneurId;
    private String UserId;
    private String StarRating;
}
