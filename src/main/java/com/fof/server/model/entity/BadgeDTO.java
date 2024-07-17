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
@Table(name = "badge")
public class BadgeDTO {

    @Id
    @GeneratedValue
    private Integer badgeId;
    private String badgeName;
    private String badgeImage;

}
