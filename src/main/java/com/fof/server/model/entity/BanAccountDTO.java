package com.fof.server.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ban-account")
public class BanAccountDTO {

    @Id
    @GeneratedValue
    private Integer banId;
    private String reason;
    private String duration;
    private Timestamp bannedTimeStamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserDTO userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adminId", referencedColumnName = "id")
    private AdminDTO adminId;

}
