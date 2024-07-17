package com.fof.server.model.normal;

import com.fof.server.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {
    
    private String accessToken;
    private Integer id;
    private Role role;
    private byte[] profileImage;

}
