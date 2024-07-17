package com.fof.server.model.entity;

import com.fof.server.enumeration.Complain;
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
@Table(name = "complain")
public class ComplainDTO {

    @Id
    @GeneratedValue
    private Integer complainId;
    private String description;
    private String date;

    @Enumerated(EnumType.STRING)
    private Complain complainType = Complain.PENDING;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserDTO userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adminId", referencedColumnName = "id")
    private AdminDTO adminId;

    private String actionDescription;

}
