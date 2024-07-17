package com.fof.server.controller;

import com.fof.server.enumeration.Status;
import com.fof.server.model.entity.*;
import com.fof.server.model.normal.RegisterRequestDTO;
import com.ventureverse.server.model.entity.*;
import com.fof.server.model.normal.ResponseDTO;
import com.fof.server.service.EntrepreneurService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entrepreneurs")
@RequiredArgsConstructor
public class EntrepreneurController {

    private final EntrepreneurService entrepreneurService;

    @GetMapping("/pending")
    public ResponseEntity<List<RegisterRequestDTO>> getPendingUsers() {
        List<EntrepreneurDTO> pendingEntrepreneurs = entrepreneurService.findByApprovalStatus(Status.PENDING);
        if (pendingEntrepreneurs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        return ResponseEntity.ok(registerRequestDTO.toRegisterRequestDTO(pendingEntrepreneurs));
    }

    @GetMapping("/pending-details/{id}")
    public ResponseEntity<EntrepreneurDTO> getPendingUserDetails(@PathVariable Integer id) {
        EntrepreneurDTO pendingEntrepreneur = entrepreneurService.findById(id);
        if (pendingEntrepreneur == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pendingEntrepreneur);
    }

    @PostMapping("/addcomplaint/{id}")
    public ResponseEntity<ResponseDTO> addComplaint(
            @PathVariable Integer id,
            @RequestBody ComplainDTO complainDTO,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(entrepreneurService.addComplain(response, complainDTO));
    }

    @GetMapping("/getcomplains")
    public ResponseEntity<List<Map<String, String>>> getComplains() {
        List<Map<String, String>> complains = entrepreneurService.getComplains();
        if (complains.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(complains);
    }

    @GetMapping("/listingsCounter/{id}")
    public ResponseEntity<List<Map<String, String>>> getCounterListings(@PathVariable Integer id) {
        List<Map<String, String>> schedules = entrepreneurService.getListings(id);
        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<List<Map<String, String>>> getMeetings(@PathVariable Integer id) {
        List<Map<String, String>> schedules = entrepreneurService.getSchedules(id);
        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/listingsInterests/{id}")
    public ResponseEntity<List<Map<String, String>>> getInterests(@PathVariable Integer id) {
        List<Map<String, String>> interests = entrepreneurService.getInterests(id);
        if (interests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(interests);
    }

    @GetMapping("/completedListings/{id}")
    public ResponseEntity<List<Map<String, String>>> getCompletedListings(@PathVariable Integer id) {
        List<Map<String, String>> completedListings = entrepreneurService.getCompletedListings(id);
        if (completedListings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(completedListings);
    }
    
    @GetMapping("/finalizeListings/{id}")
    public ResponseEntity<List<InvestorInterestedListingDTO>> finalizeListings(@PathVariable Integer id) {
        List<InvestorInterestedListingDTO> finalizedListings = entrepreneurService.finalizeListings(id);
        if (finalizedListings == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(finalizedListings);
    }

    @PutMapping("/updatefinalizeListing/{id}")
    public ResponseEntity<ResponseDTO> updateListings(
            @PathVariable("id") Integer id,
            @RequestBody InvestorInterestedListingDTO investorInterestedListingDTO
    ) {
        return ResponseEntity.ok(entrepreneurService.updateListing(id, investorInterestedListingDTO));
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<List<Map<String, String>>> getOffers(@PathVariable Integer id) {
        List<Map<String, String>> offers = entrepreneurService.getOffers(id);
        if (offers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(offers);
    }


    @GetMapping("/view")
    public ResponseEntity<List<EntrepreneurDTO>> getAllApprovedEntrepreneurs() {
        List<EntrepreneurDTO> approvedEntrepreneurs = entrepreneurService.getAllApprovedEntrepreneurs();
        return ResponseEntity.ok(approvedEntrepreneurs);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<EntrepreneurDTO> getEntrepreneurDetails(@PathVariable Integer id) {
        EntrepreneurDTO findEntrepreneur = entrepreneurService.getEntrepreneurById(id);
        if (findEntrepreneur == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(findEntrepreneur);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEntrepreneur(@RequestBody EntrepreneurDTO updatedEntrepreneur , @PathVariable Integer id) {
        EntrepreneurDTO updatedEntrepreneur1 = entrepreneurService.updateEntrepreneur(updatedEntrepreneur, id);

        if (updatedEntrepreneur1 != null) {
            return ResponseEntity.ok("Entrepreneur updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/ban/{id}")
    public ResponseEntity<ResponseDTO> banEntrepreneur(@PathVariable Integer id) {
        UserDTO bannedEntrepreneur = entrepreneurService.banEntrepreneur(id);

        if (bannedEntrepreneur != null) {
            return ResponseEntity.ok(
                    ResponseDTO.builder()
                            .status("200")
                            .message("User " + id + " Banned Successfully")
                            .build()
            );
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //put mapping for updating entrepreneur profile
    // @PutMapping("/update/{id}")
    // public ResponseEntity<String> updateEntrepreneur(@RequestBody EntrepreneurDTO updatedEntrepreneur, @PathVariable Integer id) {
    //     EntrepreneurDTO updatedEntrepreneurDTO = entrepreneurService.updateEntrepreneur(updatedEntrepreneur,id);
    //     if (updatedEntrepreneurDTO != null) {
    //         return ResponseEntity.ok("Updated");
    //     }
    //     return ResponseEntity.notFound().build();
    // }



    @GetMapping("/getEntrepreneurPic/{id}")
    public ResponseEntity<List<byte[]>> getEntrepreneurPic(@PathVariable List<Integer> id) throws IOException {
        List<byte[]> img = new ArrayList<>();
        for ( Integer i : id) {
            String entrepreneurPic = entrepreneurService.getEntrepreneurPic(i);

            String rootDirectory = System.getProperty("user.dir");
            String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/profileImages";

            Path entrepreneurPath = Paths.get(imageUploadPath,entrepreneurPic);
            img.add(Files.readAllBytes(entrepreneurPath));
        }
        if (img.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(img);
    }

    @GetMapping("/getInvestorName/{id}")
    public ResponseEntity<List<String>> getInvestorName(@PathVariable List<Integer> id) {
        List<String> investorName = entrepreneurService.getInvestorName(id);
        if (investorName.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(investorName);
    }

    @GetMapping("/getListingDetails/{id}")
    public ResponseEntity<ListingDTO> getListingDetails(@PathVariable Integer id) {
        ListingDTO listingDTO = entrepreneurService.getListingDetails(id);
        if (listingDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(listingDTO);
    }
}

