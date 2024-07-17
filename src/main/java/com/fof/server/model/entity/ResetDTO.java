package com.fof.server.model.entity;

import com.fof.server.enumeration.TokenType;
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
@Table(name = "reset-token")
public class ResetDTO {

    @Id
    @GeneratedValue
    private Integer id;
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.RESET_PASSWORD;

    private boolean expired;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id" )
    private UserDTO user;

}
