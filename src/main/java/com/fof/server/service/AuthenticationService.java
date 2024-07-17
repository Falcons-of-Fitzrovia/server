package com.fof.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fof.server.assets.Templates;
import com.fof.server.enumeration.Role;
import com.fof.server.enumeration.TokenType;
import com.fof.server.exception.CustomErrorException;
import com.fof.server.model.entity.*;
import com.fof.server.model.normal.AuthenticationRequestDTO;
import com.fof.server.model.normal.AuthenticationResponseDTO;
import com.fof.server.model.normal.RegisterRequestDTO;
import com.fof.server.model.normal.ResponseDTO;
import com.fof.server.repository.*;
import com.fof.server.config.JwtService;
import com.fof.server.enumeration.Chat;
import com.fof.server.enumeration.Status;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Integer refreshExpiration;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final TokenRepository tokenRepository;
    private final ResetRepository resetRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final CredentialRepository credentialRepository;

    public ResponseDTO checkEmail(String email) {
        var user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return GlobalService.response("Error", "Email Already Exists");
        } else {
            return GlobalService.response("Success", "Email Available");
        }
    }
    public ResponseDTO checkEmailforId(String email, int Id){
        var user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            if(user.get().getId()==Id){
                return GlobalService.response("Success", "Email Existing");
            }
            else{
                return GlobalService.response("Error", "Email Already Exists");
            }
        } else {
            return GlobalService.response("Success", "Email Available");
        }
    }

    public ResponseDTO registerAdmin(RegisterRequestDTO registerRequestDTO) {

        // Generate a Random Salt
        var salt = GlobalService.generateSalt();

        var user = AdminDTO.builder()
                .email(registerRequestDTO.getEmail())
                .approvalStatus(Status.APPROVED)
                .profileImage("profileImage.jpg")
                .contactNumber(registerRequestDTO.getContactNumber())
                .firstLineAddress(registerRequestDTO.getFirstLineAddress())
                .secondLineAddress(registerRequestDTO.getSecondLineAddress())
                .town(registerRequestDTO.getTown())
                .district(registerRequestDTO.getDistrict())
                .role(Role.CO_ADMIN)
                .firstname(registerRequestDTO.getFirstname())
                .lastname(registerRequestDTO.getLastname())
                .gender(registerRequestDTO.getGender())
                .nic(registerRequestDTO.getNic())
                .build(); // Creates AdminDTO

        var savedUser = adminRepository.save(user); // Save the Record

        var credentials = CredentialDTO.builder()
                .user(savedUser)
                .username(savedUser.getEmail())
                .password(passwordEncoder.encode(GlobalService.generateSaltedPassword(registerRequestDTO.getPassword(), salt)))
                .salt(salt)
                .build(); // Creates CredentialsDTO

        var savedCredentials = credentialRepository.save(credentials); // Save the Record

        var accessToken = jwtService.generateToken(savedCredentials);
        saveUserToken(savedUser, accessToken);

        return GlobalService.response("Success", "Co-Admin Registration Successful");

    }

    public ResponseDTO authorize(String status, Integer id) {

        if (userRepository.findApprovalById(id).equals(Status.APPROVED)) {
            return GlobalService.response("Success", "User " + id + " Already Approved");
        }

        if (status.equals("decline")) {
            userRepository.findById(id).ifPresent(user -> {
                user.setApprovalStatus(Status.DELETED);
                userRepository.save(user);
            }
            );
            return GlobalService.response("Success", "Registration of User " + id + " is Declined");
        }

        var user = userRepository.findById(id).orElseThrow();
        var date = new Date();

        user.setApprovalStatus(Status.APPROVED);
        user.setRegisteredDate(date);
        userRepository.save(user);

        var credentials = credentialRepository.findByUsername(user.getEmail()).orElseThrow(() -> new CustomErrorException("Credentials Not Found"));

        var accessToken = jwtService.generateToken(credentials);
        saveUserToken(user, accessToken);

        // SEND EMAIL TO USER
        emailService.sendEmail(user.getEmail(), "Test", "string");

        return GlobalService.response("Success", "User " + id + " Approved");
    }

    public AuthenticationResponseDTO authenticate(HttpServletResponse response, AuthenticationRequestDTO authenticationRequest) {

        var salt = credentialRepository.findSaltByEmail(authenticationRequest.getEmail()).orElseThrow();

        if (userRepository.findApprovalByEmail(authenticationRequest.getEmail()).equals(Status.PENDING)) {
            throw new CustomErrorException("User Account Not Approved");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        GlobalService.generateSaltedPassword(authenticationRequest.getPassword(), salt)
                )
        );
        var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() -> new CustomErrorException("User Not Found"));
        var credentials = credentialRepository.findByUsername(user.getEmail()).orElseThrow(() -> new CustomErrorException("Credentials Not Found"));
        var accessToken = jwtService.generateToken(credentials);
        var refreshToken = jwtService.generateRefreshToken(credentials);

        updateUserToken(user, accessToken);
        creatCookie(response, refreshToken, refreshExpiration / 1000);

        //Get the user image of the user
        var profileImage=userRepository.getimage(user.getId());

        String rootDirectory = System.getProperty("user.dir");
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/profileImages";

        Path path = Paths.get(imageUploadPath,profileImage);

        user.setStatus(Chat.ONLINE);
        user.setLastLogin(null);
        userRepository.save(user);

        try {
            return GlobalService.authenticationResponse(
                    accessToken,
                    user.getId(),
                    user.getRole(),
                    Files.readAllBytes(path)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ResponseDTO forgotPassword(String email) {

        if (userRepository.findApprovalByEmail(email).equals(Status.PENDING)) {
            throw new CustomErrorException("User Account Not Approved");
        }

        var user = userRepository.findByEmail(email).orElseThrow(() -> new CustomErrorException("User Not Found"));
        var credentials = credentialRepository.findByUsername(user.getEmail()).orElseThrow(() -> new CustomErrorException("Credentials Not Found"));
        var token = jwtService.generateForgotPasswordToken(credentials);
        emailService.sendEmail(email, "Reset Password", Templates.forgetPasswordTemp("http://localhost:3000/reset-password/" + token));
        saveResetToken(user, token);
        return GlobalService.response("Success", "Email Sent");
    }

    public ResponseDTO resetPassword(String password, String token) {

        var reset = resetRepository.findByToken(token).orElseThrow(() -> new CustomErrorException("Token Not Found"));

        if (reset.isExpired()) {
            return GlobalService.response("Error", "Token Expired");
        }

        if (password.equals("Token Check")) {
            return GlobalService.response("Alert", "Token Valid");
        }

        var email = jwtService.extractEmail(token);
        var credential = credentialRepository.findByUsername(email).orElseThrow(() -> new CustomErrorException("Credentials Not Found"));
        credential.setPassword(passwordEncoder.encode(GlobalService.generateSaltedPassword(password, credential.getSalt())));
        credentialRepository.save(credential);

        reset.setExpired(true);
        resetRepository.save(reset);

        return GlobalService.response("Success", "Password Reset");
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String refreshToken = null;
        final String email;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null) {
            return;
        }

        email = jwtService.extractEmail(refreshToken);
        if (email != null) {
            var user = this.userRepository.findByEmail(email).orElseThrow(() -> new CustomErrorException("User Not Found"));
            var credentials = credentialRepository.findByUsername(user.getEmail()).orElseThrow(() -> new CustomErrorException("Credentials Not Found"));

            if (jwtService.isTokenValid(refreshToken, credentials)) {
                var accessToken = jwtService.generateToken(credentials);
                updateUserToken(user, accessToken);
                var authResponse = AuthenticationResponseDTO.builder()
                        .accessToken(accessToken)
                        .id(user.getId())
                        .role(user.getRole())
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }

    }

    public ResponseDTO logout(HttpServletRequest request, HttpServletResponse response) {

        String authHeader = request.getHeader("Authorization");
        String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return GlobalService.response("Error", "Logout Failed");
        }

        jwt = authHeader.substring(7);
        var email = jwtService.extractEmail(jwt);
        var user = this.userRepository.findByEmail(email).orElseThrow();
        revokeAllUserTokens(user);
        creatCookie(response, "", 0);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        user.setLastLogin(timestamp);
        user.setStatus(Chat.OFFLINE);
        userRepository.save(user);

        return GlobalService.response("Success", "");

    }

    private void creatCookie(HttpServletResponse response, String refreshToken, Integer MaxAge) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);  // Make the cookie accessible only through HTTP
        refreshTokenCookie.setMaxAge(MaxAge);  // Set the cookie's expiration time in seconds
        refreshTokenCookie.setPath("/");  // Set the cookie's path to the root
        response.addCookie(refreshTokenCookie);
    }

    private void saveUserToken(UserDTO user, String jwtToken) {
        var token = TokenDTO.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(true)
                .revoked(true)
                .build();
        tokenRepository.save(token);
    }

    private void updateUserToken(UserDTO user, String jwtToken) {
        var storedToken = tokenRepository.findByUser_Id(user.getId()).orElse(null);
        if (storedToken != null) {
            storedToken.setToken(jwtToken);
            storedToken.setExpired(false);
            storedToken.setRevoked(false);
            tokenRepository.save(storedToken);
        }
    }

    private void saveResetToken(UserDTO user, String jwtToken) {
        var token = ResetDTO.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.RESET_PASSWORD)
                .expired(false)
                .build();
        resetRepository.save(token);
    }

    private void revokeAllUserTokens(UserDTO user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    
}
