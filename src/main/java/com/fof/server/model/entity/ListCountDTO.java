package com.fof.server.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListCountDTO {
    private Long entrepreneurId;
    private Long listing_count;
}
