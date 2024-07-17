package com.fof.server.model.normal;

import com.fof.server.enumeration.Role;
import com.fof.server.model.entity.EntrepreneurDTO;
import com.fof.server.model.entity.IndividualInvestorDTO;
import com.fof.server.enumeration.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    // ALL USERS
    private Integer id;
    private String email;
    private String password;
    private String contactNumber;
    private String firstLineAddress;
    private String secondLineAddress;
    private String town;
    private String district;

    @Enumerated(EnumType.STRING)
    private Status approvalStatus = Status.PENDING;

    @Enumerated(EnumType.STRING)
    private Role role;

    // ADMIN | ENTREPRENEUR |INDIVIDUAL INVESTOR
    private String firstname;
    private String lastname;
    private String nic;
    private String gender;

    // ENTREPRENEUR |INDIVIDUAL INVESTOR | ENTERPRISE INVESTOR
    private String policeReport;
    private String incomeStatement;
    private String collaboratorDetails;
    private String felony;
    private String lawSuit;
    private String felonyDescription;

    // INDIVIDUAL INVESTOR | ENTERPRISE INVESTOR
    private String financialDocument;


    // ENTERPRISE INVESTOR | ENTREPRENEUR
    private String businessName;
    private String businessRegistration;

    // ENTREPRENEUR
    private String businessContact;
    private String bfirstLineAddress;
    private String bsecondLineAddress;
    private String btown;
    private String bdistrict;
    private String businessWebsite;
    private String businessEmail;
    private String businessDescription;
    private String businessRegDoc;
    private List<Integer> sectorId;

    public List<RegisterRequestDTO> toRegisterRequestDTO(List<EntrepreneurDTO> pendingEntrepreneurs) {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        List<RegisterRequestDTO> registerRequestDTOList = new ArrayList<>();
        for (EntrepreneurDTO pendingEntrepreneur : pendingEntrepreneurs) {
            registerRequestDTO.setId(pendingEntrepreneur.getId());
            registerRequestDTO.setEmail(pendingEntrepreneur.getEmail());
            registerRequestDTO.setFirstname(pendingEntrepreneur.getFirstname());
            registerRequestDTO.setLastname(pendingEntrepreneur.getLastname());
            registerRequestDTO.setEmail(pendingEntrepreneur.getEmail());
            registerRequestDTO.setRole(pendingEntrepreneur.getRole());
            registerRequestDTOList.add(registerRequestDTO);
        }
        return registerRequestDTOList;
    }

    public List<RegisterRequestDTO> toInvesorRegisterRequestDTO(List<IndividualInvestorDTO> pendingRegisterRequests) {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        List<RegisterRequestDTO> registerRequestDTOList = new ArrayList<>();
        for (IndividualInvestorDTO pendingRegisterRequest : pendingRegisterRequests) {
            registerRequestDTO.setId(pendingRegisterRequest.getId());
            registerRequestDTO.setEmail(pendingRegisterRequest.getEmail());
            registerRequestDTO.setFirstname(pendingRegisterRequest.getFirstname());
            registerRequestDTO.setLastname(pendingRegisterRequest.getLastname());
            registerRequestDTO.setEmail(pendingRegisterRequest.getEmail());
            registerRequestDTO.setRole(pendingRegisterRequest.getRole());
            registerRequestDTOList.add(registerRequestDTO);
        }
        return registerRequestDTOList;
    }
}
