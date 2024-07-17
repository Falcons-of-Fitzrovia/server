package com.fof.server.controller;

import com.fof.server.model.entity.AdminDTO;
import com.fof.server.model.normal.AuthenticationRequestDTO;
import com.fof.server.model.normal.AuthenticationResponseDTO;
import com.fof.server.model.normal.RegisterRequestDTO;
import com.fof.server.model.normal.ResponseDTO;
import com.fof.server.repository.AdminRepository;
import com.fof.server.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private final AdminRepository adminRepository;

    @GetMapping("/details/{id}")
    public ResponseEntity<Optional<AdminDTO>> checkUsername(@PathVariable Integer id) {
        return ResponseEntity.ok(adminRepository.findById(id));
    }

    @GetMapping("/checkEmail/{email}")
    public ResponseEntity<ResponseDTO> checkEmail(@PathVariable String email) {
        return ResponseEntity.ok(authenticationService.checkEmail(email));
    }

    @GetMapping("/UpdatecheckEmail/{email}/{id}")
    public ResponseEntity<ResponseDTO> checkEmailforId(@PathVariable String email,@PathVariable Integer id) {
        return ResponseEntity.ok(authenticationService.checkEmailforId(email,id));
    }


    @GetMapping("checkBusinessEmail/{email}")
    public ResponseEntity<ResponseDTO> checkBusinessEmail(@PathVariable String email) {
        return ResponseEntity.ok(authenticationService.checkBusinessEmail(email));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<ResponseDTO> register(
            @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        return ResponseEntity.ok(authenticationService.registerAdmin(registerRequestDTO));
    }

    @PostMapping("/register/entrepreneur")
    public ResponseEntity<ResponseDTO> registerEntrepreneur(
            @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        return ResponseEntity.ok(authenticationService.registerEntrepreneur(registerRequestDTO));
    }

    @PostMapping("/register/individualInvestor")
    public ResponseEntity<ResponseDTO> registerIndividualInvestor(
            @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        return ResponseEntity.ok(authenticationService.registerIndividualInvestor(registerRequestDTO));
    }

    @PostMapping("/register/enterpriseInvestor")
    public ResponseEntity<ResponseDTO> registerEnterpriseInvestor(
            @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        return ResponseEntity.ok(authenticationService.registerEnterpriseInvestor(registerRequestDTO));
    }

    @PostMapping("/authorize/{status}/{id}")
    public ResponseEntity<ResponseDTO> register(
            @PathVariable String status,
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(authenticationService.authorize(status, id));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            HttpServletResponse response,
            @RequestBody AuthenticationRequestDTO authenticationRequestDTO
    ) throws IOException {
        return ResponseEntity.ok(authenticationService.authenticate(response, authenticationRequestDTO));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO> forgotPassword(
            @RequestBody AuthenticationRequestDTO authenticationRequestDTO
    ) {
        return ResponseEntity.ok(authenticationService.forgotPassword(authenticationRequestDTO.getEmail()));
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<ResponseDTO> resetPassword(
            @RequestBody AuthenticationRequestDTO authenticationRequestDTO,
            @PathVariable String token
    ) {
        return ResponseEntity.ok(authenticationService.resetPassword(authenticationRequestDTO.getPassword(), token));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(authenticationService.logout(request, response));
    }

    @GetMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
