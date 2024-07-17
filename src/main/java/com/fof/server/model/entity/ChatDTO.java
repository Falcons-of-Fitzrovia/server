package com.fof.server.model.entity;

import com.fof.server.enumeration.Chat;
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
@Table(name = "chat")
public class ChatDTO {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne()
    @JoinColumn(name = "sender", referencedColumnName = "id")
    private UserDTO sender;

    @ManyToOne()
    @JoinColumn(name = "receiver", referencedColumnName = "id")
    private UserDTO receiver;

    private String message;
    private Timestamp timestamp;

    @Enumerated(EnumType.STRING)
    private Chat type;

    @Enumerated(EnumType.STRING)
    private Chat status;

}
