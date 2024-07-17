package com.fof.server.service;

import com.fof.server.repository.*;
import com.fof.server.enumeration.Status;
import com.fof.server.model.entity.UserDTO;
import com.fof.server.model.normal.DetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public DetailsDTO getDetails(Integer id) {
        var user = adminRepository.findById(id).orElseThrow();

        return DetailsDTO.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }

    public List<Map<String, String>> getUsers() {
        List<UserDTO> users= userRepository.findAll();
        List<Map<String, String>> complainMap = new ArrayList<>();
        for (UserDTO user : users) {
            String registeredDate;
            if (user.getRegisteredDate() != null) {
                registeredDate = user.getRegisteredDate().toString();
            } else {
                registeredDate = "null";
            }
            complainMap.add(Map.of(
                    "id", user.getId().toString(),
                    "userRole", user.getRole().toString(),
                    "approvalStatus", user.getApprovalStatus().toString(),
                    "registeredDate", registeredDate
            ));
        }
        return complainMap;
    }

    public List<Map<String, String>> getUsersSignup() {
        List<UserDTO> users = userRepository.findAll();
        List<Map<String, String>> userMap = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12); // Subtract 12 months from current date

        for (UserDTO user : users) {
            if (user.getApprovalStatus() == Status.APPROVED) {
                if (user.getRegisteredDate().after(calendar.getTime())) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
                    String registeredMonth = dateFormat.format(user.getRegisteredDate());

                    userMap.add(Map.of(
                            "id", user.getId().toString(),
                            "userRole", user.getRole().toString(),
                            "registeredMonth", registeredMonth
                    ));
                }
            }
        }

        return userMap;
    }

    public List<Map<String, String>> getUserRegistration() {
        List<UserDTO> users = userRepository.findAll();
        List<Map<String, String>> userMap = new ArrayList<>();
        for (UserDTO user : users) {
            String registeredDate;
            if (user.getRegisteredDate() != null) {
                registeredDate = user.getRegisteredDate().toString();
            } else {
                registeredDate = "null";
            }
            userMap.add(Map.of(
                    "id", user.getId().toString(),
                    "userRole", user.getRole().toString(),
                    "status", user.getApprovalStatus().toString(),
                    "registeredDate", registeredDate
            ));
        }

        return userMap;
    }

    public byte[] getProfileImage(Integer id) {

        String image = userRepository.getimage(id);

        String rootDirectory = System.getProperty("user.dir");
        String profileUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/profileImages";
        Path imagePath = Paths.get(profileUploadPath, image);

        try {
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
