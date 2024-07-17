package com.fof.server.controller;

import com.fof.server.enumeration.Status;
import com.fof.server.model.entity.*;
import com.fof.server.model.normal.RegisterRequestDTO;
import com.fof.server.service.EntrepreneurService;
import com.fof.server.service.InvestorService;
import com.ventureverse.server.model.entity.*;
import com.fof.server.model.normal.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/investors")
@RequiredArgsConstructor
public class InvestorController {

    private final InvestorService investorService;
    private final EntrepreneurService entrepreneurService;


    @GetMapping("/pending")
    public ResponseEntity<List<RegisterRequestDTO>> getPendingUsers() {
        List<IndividualInvestorDTO> pendingRegisterRequests = investorService.findByApprovalStatus(Status.PENDING);
        if (pendingRegisterRequests.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        return ResponseEntity.ok(registerRequestDTO.toInvesorRegisterRequestDTO(pendingRegisterRequests));
    }

    @GetMapping("/pending-details/{id}")
    public ResponseEntity<IndividualInvestorDTO> getPendingUserDetails(@PathVariable Integer id) {
        IndividualInvestorDTO pendingRegisterRequest = investorService.findById(id);
        if (pendingRegisterRequest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pendingRegisterRequest);
    }

    @GetMapping("/interested-sectors/{id}")
    public ResponseEntity<List<String>> getInterestedSectors(@PathVariable Integer id) {
        List<String> interestedSectors = investorService.findInterestedSectors(id);
        if (interestedSectors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interestedSectors);
    }
    @GetMapping("/IndividualInvestor/view")
    public ResponseEntity<List<IndividualInvestorDTO>> getAllIndividualInvestors() {
        List<IndividualInvestorDTO> individualInvestors = investorService.getAllIndividualInvestors();
        return ResponseEntity.ok(individualInvestors);

    }
    @GetMapping("/EnterpriseInvestor/view")
    public ResponseEntity<List<EnterpriseInvestorDTO>> getAllEnterpriseInvestors() {
        List<EnterpriseInvestorDTO> enterpriseInvestors = investorService.getAllEnterpriseInvestors();
        return ResponseEntity.ok(enterpriseInvestors);

    }
    @GetMapping("/IndividualInvestor/view/{id}")
    public ResponseEntity<IndividualInvestorDTO> getIndividualInvestorById(@PathVariable Integer id) {
        IndividualInvestorDTO individualInvestor = investorService.getIndividualInvestorById(id);
        return ResponseEntity.ok(individualInvestor);

    }

    @PutMapping("/IndividualInvestors/update/{id}")
    public ResponseEntity<IndividualInvestorDTO> updateIndividualInvestors(@RequestBody IndividualInvestorDTO updatedIndividualInvestor, @PathVariable Integer id) {
        IndividualInvestorDTO individualInvestor = investorService.updateIndividualInvestor(updatedIndividualInvestor, id);
        return ResponseEntity.ok(individualInvestor);

    }
    @GetMapping("EnterpriseInvestor/view/{id}")
        public ResponseEntity<EnterpriseInvestorDTO> getEnterpriseInvestorById(@PathVariable Integer id) {
            EnterpriseInvestorDTO enterpriseInvestor = investorService.getEnterpriseInvestorById(id);
            return ResponseEntity.ok(enterpriseInvestor);

        }
@PutMapping("/EnterpriseInvestor/update/{id}")
    public ResponseEntity<EnterpriseInvestorDTO> updateEnterpriseInvestor(@RequestBody EnterpriseInvestorDTO updatedEnterpriseInvestor, @PathVariable Integer id) {
        EnterpriseInvestorDTO enterpriseInvestor = investorService.updateEnterpriseInvestor(updatedEnterpriseInvestor, id);
        return ResponseEntity.ok(enterpriseInvestor);

    }
    @PutMapping("/ban/{id}")
    public ResponseEntity<ResponseDTO> banInvestor(@PathVariable Integer id) {
        UserDTO individualInvestor = investorService.banInvestor(id);

        if (individualInvestor != null) {
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

    @GetMapping("/userInterest")
    public ResponseEntity<List<Map<String,String>>> getUserInterest() {
        List<Map<String,String>> interestedSectors = investorService.getUserInterest();
        if (interestedSectors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interestedSectors);
    }

    @GetMapping("/interestSectors")
    public ResponseEntity<List<Map<String,String>>> getInterestSectors() {
        List<Map<String,String>> interestedSectors = investorService.getInterestSectors();
        if (interestedSectors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interestedSectors);
    }

    @GetMapping("/InvestedAmount/{id}")
    public ResponseEntity<List<Map<String,String>>> getInvestedAmount(@PathVariable Integer id) {
        List<Map<String,String>> investedAmount = investorService.getInvestedAmount(id);
        if (investedAmount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(investedAmount);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<List<Map<String,String>>> getProjects(@PathVariable Integer id) {
        List<Map<String,String>> projects = investorService.getProjects(id);
        if (projects.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/schedules/{id}")
    public ResponseEntity<List<Map<String,String>>> getMeetings(@PathVariable Integer id){
        List<Map<String,String>> meetings= investorService.getMeetings(id);
        if(meetings.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(meetings);
    }

    @GetMapping("/interestedListings/{id}")
    public ResponseEntity<List<Map<String,String>>> getInterestedListings(@PathVariable Integer id){
        List<Map<String,String>> interestedListings= investorService.getInterestedListings(id);
        if(interestedListings.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interestedListings);
    }

    @GetMapping("/interestListings/{id}")
    public ResponseEntity<List<InvestorInterestedListingDTO>> getInvestorById(@PathVariable Integer id) {
        List<InvestorInterestedListingDTO> interestedListings = investorService.getListings(id);
        if (interestedListings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interestedListings);
    }

    @GetMapping("/getcounters/{id}")
    public ResponseEntity<List<CounterProposalDTO>> getCounters(@PathVariable Integer id) {
        List<CounterProposalDTO> counters = investorService.getCounters(id);

        //Get all interested listings for the investor
        List<InvestorInterestedListingDTO> interestedListings = investorService.getPendingListings(id);

        //For each counter, check if the listing is in the interested listings.If it is, remove it from the list
        for (int i = 0; i < counters.size(); i++) {
            for (int j = 0; j < interestedListings.size(); j++) {
                if (counters.get(i).getListingId() == interestedListings.get(j).getId().getListingId()) {
                    counters.remove(i);
                }
            }
        }

        if (counters.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(counters);
    }

    @PostMapping("/upload")
    public ResponseEntity <ResponseDTO> uploadFile(
            @RequestParam("agreement") MultipartFile agreement
    ) {
        String rootDirectory = System.getProperty("user.dir");

        // Example paths for saving images and videos
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images";

        try {
            // Save images
            String agreementFileName = agreement.getOriginalFilename();
            Path agreementFilePath = Paths.get(imageUploadPath, agreementFileName);
            Files.write(agreementFilePath, agreement.getBytes());

            return ResponseEntity.ok(new ResponseDTO("Success", "Files uploaded successfully."));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO("Failure", "Error uploading files."));
        }
    }

    @PutMapping("/finalizeListing/{id}")
    public ResponseEntity<ResponseDTO> updateListings(
            @PathVariable List<Integer> id,
            @RequestBody InvestorInterestedListingDTO investorInterestedListingDTO
    ) {
        return ResponseEntity.ok(investorService.updateListing(id, investorInterestedListingDTO));
    }

    @GetMapping("/interested-sectors-Ids/{id}")
    public ResponseEntity<List<Integer>> getInterestedSectorsIds(@PathVariable Integer id) {
        List<Integer> interestedSectors = investorService.findInterestedSectorsId(id);
        if (interestedSectors.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(interestedSectors);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateIndividualInvestor(@RequestBody IndividualInvestorDTO updatedInvestor, @PathVariable Integer id) {
        IndividualInvestorDTO updatedIndividualInvestor = investorService.updateIndividualInvestor(updatedInvestor,id);
        if (updatedIndividualInvestor != null) {
            return ResponseEntity.ok("Individual Investor Updated Successfully");
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/getInvestorPic/{id}")
    public ResponseEntity<List<byte[]>> getInvestorPic(@PathVariable Integer id) throws IOException {
        List<byte[]> img = new ArrayList<>();
        String InvestorPic = entrepreneurService.getEntrepreneurPic(id);

        String rootDirectory = System.getProperty("user.dir");
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/profileImages";

        Path InvestorPath = Paths.get(imageUploadPath,InvestorPic);
        img.add(Files.readAllBytes(InvestorPath));

        if (InvestorPic.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(img);
    }
}
