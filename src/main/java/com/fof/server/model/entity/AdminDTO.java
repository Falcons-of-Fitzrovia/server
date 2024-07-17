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
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "adminId")
public class AdminDTO extends UserDTO {

    private String firstname;
    private String lastname;
    private String nic;
    private String gender;

}
