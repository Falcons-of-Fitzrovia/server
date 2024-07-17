package com.fof.server.service;

import com.fof.server.enumeration.Role;
import com.fof.server.model.normal.AuthenticationResponseDTO;
import com.fof.server.model.normal.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class GlobalService {

    private static final ResourceLoader resourceLoader = null;

    static AuthenticationResponseDTO authenticationResponse(String accessToken, Integer userId, Role role, byte[] profileImage) {
        return AuthenticationResponseDTO.builder()
                .accessToken(accessToken)
                .id(userId)
                .role(role)
                .profileImage(profileImage)
                .build();
    }

    static ResponseDTO response(String status, String message) {
        return ResponseDTO.builder()
                .status(status)
                .message(message)
                .build();
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String generateSaltedPassword(String password, String salt) {
        return password + salt;
    }

}
