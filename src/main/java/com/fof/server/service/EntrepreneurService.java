package com.fof.server.service;

import com.fof.server.enumeration.Complain;
import com.fof.server.enumeration.Role;
import com.fof.server.enumeration.Status;
import com.fof.server.exception.CustomErrorException;
import com.fof.server.model.entity.*;
import com.fof.server.repository.*;
import com.ventureverse.server.model.entity.*;
import com.fof.server.model.normal.ResponseDTO;
import com.ventureverse.server.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EntrepreneurService {

    private final EntrepreneurRepository entrepreneurRepository;
    private final ComplainRepository complainRepository;
    private final UserRepository userRepository;
    private final Investor_InterestedListingRepository investor_interestedListingRepository;
    private final IndividualInvestorRepository individualInvestorRepository;
    private final EnterpriseInvestorRepository enterpriseInvestorRepository;
    private final CounterProposalRepository counterProposalRepository;
    private final ScheduleRepository scheduleRepository;
    private final ListingRepository listingRepository;
    private final ListingIndustrySectorsRepository listingIndustrySectorsRepository;

    public List<EntrepreneurDTO> findByApprovalStatus(Status status) {
        return entrepreneurRepository.findByApprovalStatus(status);
    }

    public EntrepreneurDTO findById(Integer id) {
        return entrepreneurRepository.findById(id).orElse(null);
    }

    public EntrepreneurDTO getEntrepreneurById(Integer id) {
        return entrepreneurRepository.findById(id).orElse(null);
    }

    public ResponseDTO addComplain(HttpServletResponse response, ComplainDTO complainDTO) {
        UserDTO user = userRepository.findById(complainDTO.getUserId().getId()).orElse(null);
        if (user == null) {
            return GlobalService.response("Error", "User not found");
        }
        ComplainDTO complain = ComplainDTO.builder()
                .description(complainDTO.getDescription())
                .date(complainDTO.getDate())
                .userId(user) // Set the loaded UserDTO entity
                .complainType(Complain.PENDING)
                .build();

        complainRepository.save(complain);
        return GlobalService.response("Success", "Complain added successfully");
    }

    public List <Map<String, String>> getComplains() {
        List<ComplainDTO> complains = complainRepository.findAll();
        List<Map<String, String>> complainMap = new ArrayList<>();
        for (ComplainDTO complain : complains) {
            UserDTO user = userRepository.findById(complain.getUserId().getId()).orElse(null);
            if (user == null) {
                return null;
            }else{
                complainMap.add(Map.of(
                        "complainType", complain.getComplainType().toString(),
                        "userRole", user.getRole().toString(),
                        "date", complain.getDate().toString()
                ));
            }
        }
        return complainMap;
    }
    
    public List<InvestorInterestedListingDTO> finalizeListings(Integer id) {
        return investor_interestedListingRepository.findByEntreprenuerListingId(id);
    }

    public ResponseDTO updateListing(Integer Listingid, InvestorInterestedListingDTO investorInterestedListingDTO) {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setListingId(Listingid);

        Optional<InvestorInterestedListingDTO> listing= investor_interestedListingRepository.findByListing(listingDTO);
        if (listing.isPresent()) {
            InvestorInterestedListingDTO oldListing = listing.get();
            oldListing.setEntrepreneurProofDocument(investorInterestedListingDTO.getEntrepreneurProofDocument());
            investor_interestedListingRepository.save(oldListing);
            return GlobalService.response("Success","Listing updated Successfully");
        }else{
            return GlobalService.response("Failed","Listing not found");
        }
    }

    public List<Map<String, String>> getListings(Integer id) {
        List<InvestorInterestedListingDTO> listings = investor_interestedListingRepository.findByEntrepreneurId(id);
        List<CounterProposalDTO> counterProposals = counterProposalRepository.findByEntrepreneurId(id);
        List<Map<String, String>> listingMap = new ArrayList<>();
        for(InvestorInterestedListingDTO listing:listings) {
            float equity=0;
            float profit=0;
            String investorName="";
            if(listing.getReturnEquityPercentage()!=null){
                equity=listing.getReturnEquityPercentage();
            }
            if(listing.getReturnUnitProfitPercentage()!=null){
                profit=listing.getReturnUnitProfitPercentage();
            }
            Role userRole = listing.getId().getInvestorId().getRole();
            if(userRole==Role.INDIVIDUAL_INVESTOR){
                investorName=individualInvestorRepository.findById(listing.getId().getInvestorId().getId()).orElse(null).getFirstname()+" "+individualInvestorRepository.findById(listing.getId().getInvestorId().getId()).orElse(null).getLastname();
            }else {
                investorName = enterpriseInvestorRepository.findById(listing.getId().getInvestorId().getId()).orElse(null).getBusinessName();
            }
            Map<String, String> user = Map.of(
                    "id", listing.getId().getInvestorId().getId().toString(),
                    "Investor", investorName,
                    "amount", listing.getId().getListingId().getExpectedAmount().toString(),
                    "type","Interested",
                    "equity",String.valueOf(equity),
                    "profit",String.valueOf(profit),
                    "date",listing.getInterestedDate().toString(),
                    "title",listing.getId().getListingId().getTitle()
            );
            listingMap.add(user);
        }
        for(CounterProposalDTO proposal:counterProposals){
            float equity=0;
            float profit=0;
            String investorName="";
            if(proposal.getReturnEquityPercentage()!=null){
                equity=proposal.getReturnEquityPercentage();
            }
            if(proposal.getReturnUnitProfitPercentage()!=null){
                profit=proposal.getReturnUnitProfitPercentage();
            }
            Role userRole = proposal.getInvestorId().getRole();
            if(userRole==Role.INDIVIDUAL_INVESTOR){
                investorName=individualInvestorRepository.findById(proposal.getInvestorId().getId()).orElse(null).getFirstname()+" "+individualInvestorRepository.findById(proposal.getInvestorId().getId()).orElse(null).getLastname();
            }else {
                investorName = enterpriseInvestorRepository.findById(proposal.getInvestorId().getId()).orElse(null).getBusinessName();
            }
            Map<String, String> user = Map.of(
                    "Investor", investorName,
                    "id", proposal.getInvestorId().getId().toString(),
                    "amount", proposal.getAmount().toString(),
                    "type","Counter",
                    "equity",String.valueOf(equity),
                    "profit",String.valueOf(profit),
                    "date",proposal.getDate().toString(),
                    "title",proposal.getListingId().getTitle()
            );
            listingMap.add(user);
        }
        return listingMap;
    }

    public List<Map<String, String>> getSchedules(Integer id) {
        List<ScheduleDTO> meetings=scheduleRepository.findEntrepreneurMeetings(id);

        List<Map<String, String>> userMap = new ArrayList<>();
        for (ScheduleDTO meeting:meetings) {
            Map<String, String> user = Map.of(
                    "date",meeting.getDate().toString(),
                    "time",meeting.getTime().toString(),
                    "title",meeting.getTitle()
            );
            userMap.add(user);
        }
        return userMap;

    }

    public List<Map<String, String>> getInterests(Integer id) {
        List<ListingDTO> listings = listingRepository.findByEntrepreneurId(id);
        List<Map<String, String>> listingMap = new ArrayList<>();

        for(ListingDTO listing:listings) {
            //sectors that investor is interested in
            List<ListingIndustrySectorsDTO> sectors = listingIndustrySectorsRepository.findByListingId(listing.getListingId());
            for (ListingIndustrySectorsDTO sector:sectors) {
                Map<String, String> user = Map.of(
                        "sector", sector.getId().getSectorId().getSectorName(),
                        "amount", listing.getExpectedAmount().toString()
                );
                listingMap.add(user);
            }
        }
        return listingMap;
    }


    public List<Map<String, String>> getCompletedListings(Integer id) {
        List<InvestorInterestedListingDTO> listings = investor_interestedListingRepository.findByEntrepreneurId(id);
        List<Map<String, String>> listingMap = new ArrayList<>();

        for(InvestorInterestedListingDTO listing:listings){
            List<ListingIndustrySectorsDTO> sectors = listingIndustrySectorsRepository.findByListingId(listing.getId().getListingId().getListingId());
            for (ListingIndustrySectorsDTO sector:sectors) {
                Map<String, String> user = Map.of(
                        "sector", sector.getId().getSectorId().getSectorName(),
                        "amount", listing.getId().getListingId().getExpectedAmount().toString()
                );
                listingMap.add(user);
            }
        }
        return listingMap;
    }

    public String getdoc(Integer id) {
        return investor_interestedListingRepository.findByListingId(id).getEntrepreneurProofDocument();
    }

    public List<Map<String, String>> getOffers(Integer id) {
        List<InvestorInterestedListingDTO> listing=investor_interestedListingRepository.findByPendingListingId(id);
        List<CounterProposalDTO> proposal=counterProposalRepository.findByListingId(id);

        List<Map<String, String>> listingMap = new ArrayList<>();
        for(InvestorInterestedListingDTO list:listing){
            float equity=0;
            float profit=0;
            String investorName="";
            if(list.getReturnEquityPercentage()!=null){
                equity=list.getReturnEquityPercentage();
            }
            if(list.getReturnUnitProfitPercentage()!=null){
                profit=list.getReturnUnitProfitPercentage();
            }
            Role userRole = list.getId().getInvestorId().getRole();
            if(userRole==Role.INDIVIDUAL_INVESTOR){
                investorName=individualInvestorRepository.findById(list.getId().getInvestorId().getId()).orElse(null).getFirstname()+" "+individualInvestorRepository.findById(list.getId().getInvestorId().getId()).orElse(null).getLastname();
            }else {
                investorName = enterpriseInvestorRepository.findById(list.getId().getInvestorId().getId()).orElse(null).getBusinessName();
            }

            Map<String, String> user = Map.of(
                    "id", list.getId().getInvestorId().getId().toString(),
                    "Investor", investorName,
                    "amount", list.getId().getListingId().getExpectedAmount().toString(),
                    "type","Interested",
                    "equity",String.valueOf(equity),
                    "profit",String.valueOf(profit)
            );
            listingMap.add(user);
        }
        for(CounterProposalDTO prop:proposal){
            float equity=0;
            float profit=0;
            String investorName="";
            if(prop.getReturnEquityPercentage()!=null){
                equity=prop.getReturnEquityPercentage();
            }
            if(prop.getReturnUnitProfitPercentage()!=null){
                profit=prop.getReturnUnitProfitPercentage();
            }
            Role userRole = prop.getInvestorId().getRole();
            if(userRole==Role.INDIVIDUAL_INVESTOR){
                investorName=individualInvestorRepository.findById(prop.getInvestorId().getId()).orElse(null).getFirstname()+" "+individualInvestorRepository.findById(prop.getInvestorId().getId()).orElse(null).getLastname();
            }else {
                investorName = enterpriseInvestorRepository.findById(prop.getInvestorId().getId()).orElse(null).getBusinessName();
            }
            Map<String, String> user = Map.of(
                    "id", prop.getInvestorId().getId().toString(),
                    "Investor", investorName,
                    "amount", prop.getAmount().toString(),
                    "type","Counter",
                    "equity",String.valueOf(equity),
                    "profit",String.valueOf(profit)
            );
            listingMap.add(user);
        }
        return listingMap;
    }
    
    public List<EntrepreneurDTO> getAllApprovedEntrepreneurs() {
        return entrepreneurRepository.findByApprovalStatus(Status.APPROVED);
    }

    public EntrepreneurDTO updateEntrepreneur(EntrepreneurDTO updatedEntrepreneur, Integer id) {
        Optional<EntrepreneurDTO> existingEntrepreneurOptional = entrepreneurRepository.findById(id);

        if (existingEntrepreneurOptional.isPresent()) {
            EntrepreneurDTO existingEntrepreneur = existingEntrepreneurOptional.get();

            // Update the fields of the existing entrepreneur with data from updatedEntrepreneur
            existingEntrepreneur.setFirstname(updatedEntrepreneur.getFirstname());
            existingEntrepreneur.setLastname(updatedEntrepreneur.getLastname());
            existingEntrepreneur.setEmail(updatedEntrepreneur.getEmail());
            existingEntrepreneur.setNic(updatedEntrepreneur.getNic());
            existingEntrepreneur.setContactNumber(updatedEntrepreneur.getContactNumber());
            existingEntrepreneur.setBusinessName(updatedEntrepreneur.getBusinessName());
            existingEntrepreneur.setBusinessEmail(updatedEntrepreneur.getBusinessEmail());

            // Save the updated existing entrepreneur to the repository
            entrepreneurRepository.save(existingEntrepreneur);

            // Return the updated entrepreneur
            return existingEntrepreneur;
        } else {
            return null; // Or you can throw an exception indicating that the entrepreneur with the given ID was not found
        }
    }

    
    // public EntrepreneurDTO updateEntrepreneur(EntrepreneurDTO updatedEntrepreneur, Integer id) {
    //     Integer entrepreneurId = updatedEntrepreneur.getId();
    //     Optional<EntrepreneurDTO> existingEntrepreneuroptional = entrepreneurRepository.findById(id);

    //     if(existingEntrepreneuroptional.isPresent()){
    //         EntrepreneurDTO existingEntrepreneur = existingEntrepreneuroptional.get();
    //         // Update the existing entrepreneur fields with the values from updatedAdmin
    //         existingEntrepreneur.setFirstname(updatedEntrepreneur.getFirstname());
    //         existingEntrepreneur.setLastname(updatedEntrepreneur.getLastname());
    //         existingEntrepreneur.setFirstLineAddress(updatedEntrepreneur.getFirstLineAddress());
    //         existingEntrepreneur.setSecondLineAddress(updatedEntrepreneur.getSecondLineAddress());
    //         existingEntrepreneur.setTown(updatedEntrepreneur.getTown());
    //         existingEntrepreneur.setDistrict(updatedEntrepreneur.getDistrict());
    //         existingEntrepreneur.setContactNumber(updatedEntrepreneur.getContactNumber());
    //         //save to backend
    //         return entrepreneurRepository.save(existingEntrepreneur);
    //     }else{
    //         //keep the existing entrepreneur
    //         return entrepreneurRepository.save(updatedEntrepreneur);
    //     }
    // }


    public UserDTO banEntrepreneur(Integer id) {
        UserDTO user = userRepository.findById(id).orElseThrow(() -> new CustomErrorException("User Not Found"));
        user.setApprovalStatus(Status.BANNED);
        return userRepository.save(user);
    }

    public long countEntrepreneurs() {
        return entrepreneurRepository.count();
    }

    public String getadmindoc(Integer listingId,Integer investorId) {
       return investor_interestedListingRepository.findByEntrepreneurFinalizeDoc(listingId,investorId);
    }


    public String getEntrepreneurPic(Integer id) {
        return userRepository.getimage(id);
    }

    public ListingDTO getListingDetails(Integer id) {
        return listingRepository.findById(id).orElse(null);
    }

    public List<String> getInvestorName(List<Integer> id) {
        List<String> name=new ArrayList<>();
        for(Integer i:id){
            Role userRole = userRepository.findById(i).orElse(null).getRole();
            if(userRole==Role.INDIVIDUAL_INVESTOR){
                name.add(individualInvestorRepository.findById(i).orElse(null).getFirstname()+" "+individualInvestorRepository.findById(i).orElse(null).getLastname());
            }else {
                name.add(enterpriseInvestorRepository.findById(i).orElse(null).getBusinessName());
            }
        }
        return name;
    }  


}
