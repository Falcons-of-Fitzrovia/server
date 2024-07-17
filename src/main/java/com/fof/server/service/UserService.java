package com.fof.server.service;

import com.fof.server.enumeration.Role;
import com.fof.server.exception.CustomErrorException;
import com.fof.server.model.entity.ComplainDTO;
import com.fof.server.repository.*;
import com.fof.server.enumeration.Status;
import com.fof.server.model.entity.ChatDTO;
import com.fof.server.model.entity.UserDTO;
import com.fof.server.model.normal.DetailsDTO;
import com.ventureverse.server.repository.*;
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
    private final EntrepreneurRepository entrepreneurRepository;
    private final EnterpriseInvestorRepository enterpriseInvestorRepository;
    private final IndividualInvestorRepository individualInvestorRepository;
    private final AdminRepository adminRepository;
    private final ComplainRepository complainRepository;
    private final ChatRepository chatRepository;

    public DetailsDTO getDetails(Integer id) {

        if (userRepository.findRoleById(id).equals(Role.ENTREPRENEUR)) {
            var user = entrepreneurRepository.findById(id).orElseThrow();

            return DetailsDTO.builder()
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .build();
        } else if (userRepository.findRoleById(id).equals(Role.ENTERPRISE_INVESTOR)) {

            var user = enterpriseInvestorRepository.findById(id).orElseThrow();

            return DetailsDTO.builder()
                    .businessName(user.getBusinessName())
                    .build();
        } else if (userRepository.findRoleById(id).equals(Role.INDIVIDUAL_INVESTOR)) {

            var user = individualInvestorRepository.findById(id).orElseThrow();

            return DetailsDTO.builder()
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .build();
        } else {
            var user = adminRepository.findById(id).orElseThrow();

            return DetailsDTO.builder()
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .email(user.getEmail())
                    .build();
        }
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

    public List<Map<String, String>> getTopcomplains() {
        List<ComplainDTO> complains = complainRepository.findAll();
        List<Map<String, String>> complainMap = new ArrayList<>();
        //sort complains by date
        for (ComplainDTO complain : complains) {
            String complainDate;
            if (complain.getDate() != null) {
                complainDate = complain.getDate();
            } else {
                complainDate = "null";
            }
            if (complain.getUserId().getRole().equals(Role.ENTREPRENEUR)) {
                var user = entrepreneurRepository.findById(complain.getUserId().getId()).orElseThrow();
                complainMap.add(Map.of(
                        "complainUser", user.getFirstname() + " " + user.getLastname(),
                        "id",(complain.getUserId().getId()).toString(),
                        "userRole","Entrepreneur",
                        "complainDate", complainDate,
                        "complainStatus", complain.getComplainType().toString(),
                        "complainDescription", complain.getDescription()
                ));
            } else if (complain.getUserId().getRole().equals(Role.ENTERPRISE_INVESTOR)) {
                var user = enterpriseInvestorRepository.findById(complain.getUserId().getId()).orElseThrow();
                complainMap.add(Map.of(
                        "complainUser", user.getBusinessName(),
                        "id",(complain.getUserId().getId()).toString(),
                        "userRole","Enterprise Investor",
                        "complainDate", complainDate,
                        "complainStatus", complain.getComplainType().toString(),
                        "complainDescription", complain.getDescription()
                ));
            } else if (complain.getUserId().getRole().equals(Role.INDIVIDUAL_INVESTOR)) {
                var user = individualInvestorRepository.findById(complain.getUserId().getId()).orElseThrow();
                complainMap.add(Map.of(
                        "complainUser", user.getFirstname() + " " + user.getLastname(),
                        "id",(complain.getUserId().getId()).toString(),
                        "userRole","Individual Investor",
                        "complainDate", complainDate,
                        "complainStatus", complain.getComplainType().toString(),
                        "complainDescription", complain.getDescription()
                ));
            }
        }
        return complainMap;
    }

    public List<DetailsDTO> getChats(Integer id) {
        var user = userRepository.findById(id).orElseThrow(() -> new CustomErrorException("User not found"));
        List<ChatDTO> Chats = chatRepository.findBySenderReceiver(user);

        List<DetailsDTO> Details = new ArrayList<>();

        for (ChatDTO Chat : Chats) {

            String rootDirectory = System.getProperty("user.dir");
            String profileUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/profileImages";
            String profileImage;

            if (Objects.equals(Chat.getSender().getId(), id)) {
                profileImage = Chat.getSender().getProfileImage();
            } else {
                profileImage = Chat.getReceiver().getProfileImage();
            }

            Path imagePath = Paths.get(profileUploadPath, profileImage);

            try {
                Details.add(DetailsDTO.builder()
                        .sender(Chat.getSender())
                        .receiver(Chat.getReceiver())
                        .roomOwnerImage(Files.readAllBytes(imagePath))
                        .message(Chat.getMessage())
                        .timestamp(Chat.getTimestamp())
                        .type(Chat.getType())
                        .status(Chat.getStatus())
                        .build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
            return Details;
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
