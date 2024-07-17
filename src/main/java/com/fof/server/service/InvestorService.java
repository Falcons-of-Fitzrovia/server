package com.fof.server.service;

import com.fof.server.enumeration.Status;
import com.fof.server.exception.CustomErrorException;
import com.fof.server.model.entity.*;
import com.fof.server.repository.*;
import com.ventureverse.server.model.entity.*;
import com.fof.server.model.normal.ResponseDTO;
import com.ventureverse.server.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@AllArgsConstructor
public class InvestorService {
    private final IndividualInvestorRepository individualInvestorRepository;
    private final InvestorInterestedSectorRepository investorInterestedSectorRepository;
    private final Investor_InterestedListingRepository investorInterestedListingRepository;
    private final ListingIndustrySectorsRepository listingIndustrySectorsRepository;
    private final ScheduleRepository scheduleRepository;
    private final CounterProposalRepository counterProposalRepository;
    private final EnterpriseInvestorRepository enterpriseInvestorRepository;
    private final Investor_InterestedListingRepository investor_interestedListingRepository;
    private final UserRepository userRepository;




    public IndividualInvestorDTO updateIndividualInvestor(IndividualInvestorDTO updatedInvestor, Integer id) {
        Integer individualInvestorId = updatedInvestor.getId();

        Optional<IndividualInvestorDTO> existingIndividualInvestoroptional = individualInvestorRepository.findById(id);

        if (existingIndividualInvestoroptional.isPresent()) {
            IndividualInvestorDTO existingIndividualInvestor = existingIndividualInvestoroptional.get();
            // Update the existing individual investor fields with the values from updatedAdmin
            existingIndividualInvestor.setFirstname(updatedInvestor.getFirstname());
            existingIndividualInvestor.setLastname(updatedInvestor.getLastname());
            existingIndividualInvestor.setFirstLineAddress(updatedInvestor.getFirstLineAddress());
            existingIndividualInvestor.setSecondLineAddress(updatedInvestor.getSecondLineAddress());
            existingIndividualInvestor.setTown(updatedInvestor.getTown());
            existingIndividualInvestor.setDistrict(updatedInvestor.getDistrict());
            existingIndividualInvestor.setContactNumber(updatedInvestor.getContactNumber());
            //save to backend
            return individualInvestorRepository.save(existingIndividualInvestor);
        }else{
            //return the existing individual investor
            return individualInvestorRepository.save(updatedInvestor);
        }
    }

    public List<IndividualInvestorDTO> findByApprovalStatus(Status status) {
        return individualInvestorRepository.findByApprovalStatus(status);
    }

    public IndividualInvestorDTO findById(Integer id) {
        return individualInvestorRepository.findById(id).orElse(null);
    }

    public List<String> findInterestedSectors(Integer id) {
        List<InvestorInterestedSectorDTO> interestedSectors = investorInterestedSectorRepository.findByInvestorId(id);
        List<String> sectorNames = new ArrayList<>();
        for (InvestorInterestedSectorDTO interestedSector : interestedSectors) {
            sectorNames.add(interestedSector.getId().getSectorId().getName());
        }
        return sectorNames;
    }

    public List<Integer> findInterestedSectorsId(Integer id) {
        List<InvestorInterestedSectorDTO> interestedSectors= investorInterestedSectorRepository.findByInvestorId(id);
        List<Integer> sectorIds = new ArrayList<>();
        for (InvestorInterestedSectorDTO interestedSector : interestedSectors) {
            sectorIds.add(interestedSector.getId().getSectorId().getSectorId());
        }
        return sectorIds;
    }

    public IndividualInvestorDTO getInvestorById(int i) {
        return individualInvestorRepository.findById(i).orElse(null);
    }

    public List<Map<String, String>> getUserInterest() {
        List<InvestorInterestedListingDTO> interests = investorInterestedListingRepository.findAll();
        List<Map<String, String>> userMap = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);

        for(InvestorInterestedListingDTO interest : interests) {
            if(interest.getInterestedDate().after(calendar.getTime())){
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
                String publishDate = dateFormat.format(interest.getInterestedDate());
                Map<String, String> user = Map.of(
                        "id", String.valueOf(interest.getId().getInvestorId().getId()),
                        "inerestedDate", publishDate
                );
                userMap.add(user);
            }
        }
        return userMap;
    }

    // public List<Map<String, String>> getUserInterest() {
    //     List<InvestorInterestedListingDTO> interests = investor_interestedListingRepository.findAll();
    //     List<Map<String, String>> userMap = new ArrayList<>();

    //     Calendar calendar = Calendar.getInstance();
    //     calendar.add(Calendar.MONTH, -12);
    //     return userMap;
    // }

    public List<Map<String, String>> getInvestedAmount(Integer id) {
        List<InvestorInterestedListingDTO> interests = investorInterestedListingRepository.findByInvestorId(id);
        List<Map<String, String>> userMap = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);

        for(InvestorInterestedListingDTO interest : interests) {
            if(interest.getInterestedDate().after(calendar.getTime())){
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
                String publishDate = dateFormat.format(interest.getFinalizedDate());
                Map<String, String> user = Map.of(
                        "id", String.valueOf(interest.getId().getInvestorId().getId()),
                        "finalizeDate", publishDate,
                        "amount", String.valueOf(interest.getAmountFinalized())
                );
                userMap.add(user);
            }
        }
        return userMap;
    }

    public List<Map<String, String>> getProjects(Integer id) {
        List<InvestorInterestedListingDTO> interests = investorInterestedListingRepository.findAllByInvestorId(id);
        List<Map<String, String>> userMap = new ArrayList<>();

        for(InvestorInterestedListingDTO interest : interests) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
                //handle null pointer exception for finalized date
                String FinalizeDate = "";
                if(interest.getFinalizedDate() != null){
                    FinalizeDate = dateFormat.format(interest.getFinalizedDate());
                }else {
                    FinalizeDate= "Not Finalized";
                }
                String publishDate = dateFormat.format(interest.getInterestedDate());
                Map<String, String> user = Map.of(
                        "listingId", String.valueOf(interest.getId().getListingId().getListingId()),
                        "interestedDate", publishDate,
                        "finalizeDate", FinalizeDate
                );
                userMap.add(user);
        }
        return userMap;
    }

    public List<Map<String, String>> getInterestSectors() {
        List<InvestorInterestedListingDTO> interests = investorInterestedListingRepository.findAll();

        Map<Integer, List<Integer>> listingInvestorMap = new HashMap<>();
        for(InvestorInterestedListingDTO interest : interests){
            int listingId = interest.getId().getListingId().getListingId();
            int investorId = interest.getId().getInvestorId().getId();
            if(listingInvestorMap.containsKey(listingId)){
                List<Integer> investors = listingInvestorMap.get(listingId);
                investors.add(investorId);
                listingInvestorMap.put(listingId, investors);
            }else{
                List<Integer> investors = new ArrayList<>();
                investors.add(investorId);
                listingInvestorMap.put(listingId, investors);
            }
        }

        List<Map<String, String>> userMap = new ArrayList<>();

        for(Map.Entry<Integer, List<Integer>> entry : listingInvestorMap.entrySet()){
            int listingId = entry.getKey();
            List<Integer> investors = entry.getValue();

            //for each investor get the sector
            for(Integer investorId : investors){
                //check whether that is completed or not
                InvestorInterestedListingDTO interest = investorInterestedListingRepository.findByInvestorIdAndListingId(investorId, listingId);
                if(interest.getFinalizedDate() == null){
                    continue;
                }
                int amount = interest.getAmountFinalized();

                List<ListingIndustrySectorsDTO> sectors = listingIndustrySectorsRepository.findAll();
                for(ListingIndustrySectorsDTO sector : sectors){
                    if(sector.getId().getListingId().getListingId() == listingId){
                        Map<String, String> user = Map.of(
                                "listingId", String.valueOf(listingId),
                                "sectorName", sector.getId().getSectorId().getName(),
                                "amount", String.valueOf(amount)
                        );
                        userMap.add(user);
                    }
                }
            }

        }
        return userMap;
    }

    // public List<Map<String, String>> getInterestSectors() {
    //     List<InvestorInterestedListingDTO> interests = investor_interestedListingRepository.findAll();

    //     Map<Integer, List<Integer>> listingInvestorMap = new HashMap<>();
    //     for(InvestorInterestedListingDTO interest : interests){
    //         int listingId = interest.getId().getListingId().getListingId();
    //         int investorId = interest.getId().getInvestorId().getId();
    //         if(listingInvestorMap.containsKey(listingId)){
    //             List<Integer> investors = listingInvestorMap.get(listingId);
    //             investors.add(investorId);
    //             listingInvestorMap.put(listingId, investors);
    //         }else{
    //             List<Integer> investors = new ArrayList<>();
    //             investors.add(investorId);
    //             listingInvestorMap.put(listingId, investors);
    //         }
    //     }

    //     List<Map<String, String>> userMap = new ArrayList<>();

    //     for(Map.Entry<Integer, List<Integer>> entry : listingInvestorMap.entrySet()){
    //         int listingId = entry.getKey();
    //         List<Integer> investors = entry.getValue();

    //         //for each investor get the sector
    //         for(Integer investorId : investors){
    //             //check whether that is completed or not
    //             InvestorInterestedListingDTO interest = investor_interestedListingRepository.findByInvestorIdAndListingId(investorId, listingId);
    //             if(interest.getFinalizedDate() == null){
    //                 continue;
    //             }
    //             int amount = interest.getAmountFinalized();

    //             List<ListingIndustrySectorsDTO> sectors = listingIndustrySectorsRepository.findAll();
    //             for(ListingIndustrySectorsDTO sector : sectors){
    //                 if(sector.getId().getListingId().getListingId() == listingId){
    //                     Map<String, String> user = Map.of(
    //                             "listingId", String.valueOf(listingId),
    //                             "sectorName", sector.getId().getSectorId().getName(),
    //                             "amount", String.valueOf(amount)
    //                     );
    //                     userMap.add(user);
    //                 }
    //             }
    //         }

    //     }
    //     return userMap;
    // }

    public List<Map<String, String>> getMeetings(Integer id) {
        List<ScheduleDTO> meetings=scheduleRepository.findMeetings(id);

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

    public List<InvestorInterestedListingDTO> getListings(Integer id) {
        return investor_interestedListingRepository.findPendingListingsOfInvestor(id);
    }

    // public List<InvestorInterestedListingDTO> getListings(Integer id) {
    //     return investor_interestedListingRepository.findByInvestorId(id);
    // }

    public List<InvestorInterestedListingDTO> getListingsByListingId(ListingDTO listingDTO) {
        return investor_interestedListingRepository.findByListingid(listingDTO);
    }


    public ResponseDTO updateListing(List<Integer> id, InvestorInterestedListingDTO investorInterestedListingDTO) {
        //make the listingid a type of listingDTO
        var Listingid= id.get(0);
        var investorId= id.get(1);
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setListingId(Listingid);

        InvestorDTO investorDTO = new InvestorDTO();
        investorDTO.setId(investorId);

        Optional<InvestorInterestedListingDTO> listing= investor_interestedListingRepository.findByListingInvestor(listingDTO,investorId);
        if (listing.isPresent()) {
            InvestorInterestedListingDTO oldListing = listing.get();
            oldListing.setAmountFinalized(investorInterestedListingDTO.getAmountFinalized());
            oldListing.setReturnEquityPercentage(investorInterestedListingDTO.getReturnEquityPercentage());
            oldListing.setReturnUnitProfitPercentage(investorInterestedListingDTO.getReturnUnitProfitPercentage());
            oldListing.setInvestorProofDocument(investorInterestedListingDTO.getInvestorProofDocument());
            oldListing.setStatus(investorInterestedListingDTO.getStatus());
            investor_interestedListingRepository.save(oldListing);
            return GlobalService.response("Success","Listing updated Successfully");
        } else {
            Optional<CounterProposalDTO> counterProposal= counterProposalRepository.findByListingInvestorId(Listingid,investorId);
            if(counterProposal.isPresent()){
                InvestorInterestedListingDTO newListing = new InvestorInterestedListingDTO();
                newListing.setId(new InvestorInterestedListingDTO.CompositeKey(investorDTO, listingDTO));
                newListing.setAmountFinalized(investorInterestedListingDTO.getAmountFinalized());
                newListing.setReturnEquityPercentage(investorInterestedListingDTO.getReturnEquityPercentage());
                newListing.setReturnUnitProfitPercentage(investorInterestedListingDTO.getReturnUnitProfitPercentage());
                newListing.setInvestorProofDocument(investorInterestedListingDTO.getInvestorProofDocument());
                newListing.setStatus(investorInterestedListingDTO.getStatus());
                newListing.setInterestedDate(counterProposal.get().getDate());
                investor_interestedListingRepository.save(newListing);
                //delete the counter proposal
                counterProposalRepository.delete(counterProposal.get());
                return GlobalService.response("Success","Listing updated Successfully");
            }else{
                return GlobalService.response("Error","Listing not found");
            }
        }
    }

    public String getdoc(Integer id) {
        return investor_interestedListingRepository.findByListingId(id).getInvestorProofDocument();
    }

    public List<CounterProposalDTO> getCounters(Integer id) {
        return counterProposalRepository.findByInvestorId(id);
    }
    
    public List<Map<String, String>> getInterestedListings(Integer id) {
        List<InvestorInterestedListingDTO> listings=investor_interestedListingRepository.findPendingListings(id);
        List<CounterProposalDTO> proposals=counterProposalRepository.findAll();
        List<InvestorInterestedListingDTO> completedListings=investor_interestedListingRepository.findByInvestorId(id);

        //take completed listing ids to a list
        List<Integer> completedListingIds = new ArrayList<>();
        for(InvestorInterestedListingDTO completedListing : completedListings){
            completedListingIds.add(completedListing.getId().getListingId().getListingId());
        }

        List<Map<String, String>> userMap = new ArrayList<>();
        for(InvestorInterestedListingDTO listing:listings) {
            float equity=0;
            float profit=0;
            if(listing.getReturnEquityPercentage()!=null){
                equity=listing.getReturnEquityPercentage();
            }
            if(listing.getReturnUnitProfitPercentage()!=null){
                profit=listing.getReturnUnitProfitPercentage();
            }
            Map<String, String> user = Map.of(
                    "Entrepreneur", listing.getId().getListingId().getEntrepreneurId().getFirstname()+" "+listing.getId().getListingId().getEntrepreneurId().getLastname(),
                    "id",listing.getId().getListingId().getEntrepreneurId().getId().toString(),
                    "amount", listing.getId().getListingId().getExpectedAmount().toString(),
                    "type","Interested",
                    "equity",String.valueOf(equity),
                    "profit",String.valueOf(profit),
                    "date",listing.getInterestedDate().toString()
            );
            userMap.add(user);
        }
        for(CounterProposalDTO proposal:proposals) {
            if(!completedListingIds.contains(proposal.getListingId().getListingId())){
                float equity=0;
                float profit=0;
                if(proposal.getReturnEquityPercentage()!=null){
                    equity=proposal.getReturnEquityPercentage();
                }
                if(proposal.getReturnUnitProfitPercentage()!=null){
                    profit=proposal.getReturnUnitProfitPercentage();
                }
                Map<String, String> user = Map.of(
                        "Entrepreneur", proposal.getEntrepreneurId().getFirstname() + " " + proposal.getEntrepreneurId().getLastname(),
                        "id", proposal.getEntrepreneurId().getId().toString(),
                        "amount", proposal.getAmount().toString(),
                        "type", "Counter",
                        "equity", String.valueOf(equity),
                        "profit", String.valueOf(profit),
                        "date", proposal.getDate().toString()
                );
                userMap.add(user);
            }
        }
        return userMap;
    }
    
    public List<IndividualInvestorDTO> getAllIndividualInvestors() {
        return individualInvestorRepository.findByApprovalStatus(Status.APPROVED);
    }

    public List<EnterpriseInvestorDTO> getAllEnterpriseInvestors() {
        return enterpriseInvestorRepository.findByApprovalStatus(Status.APPROVED);
    }

    public IndividualInvestorDTO getIndividualInvestorById(Integer id) {
        return individualInvestorRepository.findById(id).orElse(null);
    }

    //Individual Investor Update
//    public IndividualInvestorDTO updateIndividualInvestor(IndividualInvestorDTO updatedIndividualInvestor, Integer id) {
//        Optional<IndividualInvestorDTO> existingIndividualInvestorOptional = individualInvestorRepository.findById(id);
//
//        IndividualInvestorDTO existingIndividualInvestor = individualInvestorRepository.findById(id).orElse(null);
//
//        if (existingIndividualInvestor != null) {
//            existingIndividualInvestor.setFirstname(updatedIndividualInvestor.getFirstname());
//            existingIndividualInvestor.setLastname(updatedIndividualInvestor.getLastname());
//            existingIndividualInvestor.setEmail(updatedIndividualInvestor.getEmail());
//            existingIndividualInvestor.setNic(updatedIndividualInvestor.getNic());
//            existingIndividualInvestor.setContactNumber(updatedIndividualInvestor.getContactNumber());
//
//        }
//        if (existingIndividualInvestor != null) {
//            return individualInvestorRepository.save(existingIndividualInvestor);
//        } else {
//            return null;
//        }
//    }


    public EnterpriseInvestorDTO getEnterpriseInvestorById(Integer id) {
        return enterpriseInvestorRepository.findById(id).orElse(null);
    }

    public EnterpriseInvestorDTO updateEnterpriseInvestor(EnterpriseInvestorDTO updatedEnterpriseInvestor, Integer id) {
        Optional<EnterpriseInvestorDTO> existingEnterpriseInvestorOptional = enterpriseInvestorRepository.findById(id);

        EnterpriseInvestorDTO existingEnterpriseInvestor = enterpriseInvestorRepository.findById(id).orElse(null);

        if (existingEnterpriseInvestor != null) {
            existingEnterpriseInvestor.setEmail(updatedEnterpriseInvestor.getEmail());
            existingEnterpriseInvestor.setContactNumber(updatedEnterpriseInvestor.getContactNumber());
            existingEnterpriseInvestor.setBusinessName(updatedEnterpriseInvestor.getBusinessName());
            existingEnterpriseInvestor.setFirstLineAddress(updatedEnterpriseInvestor.getFirstLineAddress());
            existingEnterpriseInvestor.setSecondLineAddress(updatedEnterpriseInvestor.getSecondLineAddress());
            existingEnterpriseInvestor.setTown(updatedEnterpriseInvestor.getTown());

            enterpriseInvestorRepository.save(existingEnterpriseInvestor);
            return existingEnterpriseInvestor;

        }
        else{
            return null;
        }
    }

    public UserDTO banInvestor(Integer id) {
        UserDTO user = userRepository.findById(id).orElseThrow(() -> new CustomErrorException("User Not Found"));
        user.setApprovalStatus(Status.BANNED);
        return userRepository.save(user);
    }
    
    // public UserDTO banIndividualInvestor(Integer id) {
    //     UserDTO user  = userRepository.findByUserID(id);
    //     user.setApprovalStatus(Status.PENDING);
    //     userRepository.save(user);
    //     return user;
    // }
    
    public String getadmindoc(Integer investorId, Integer listingId) {
        return investor_interestedListingRepository.findByListingInvestorId(listingId,investorId);
    }
    public IndividualInvestorDTO save(IndividualInvestorDTO investor) {
        return individualInvestorRepository.save(investor);
    }

    public long countIndividualInvestors (){
            return individualInvestorRepository.count();
    }


//        for(InvestorInterestedListingDTO interest : interests) {
//            if(interest.getInterestedDate().after(calendar.getTime())){
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
//                String publishDate = dateFormat.format(interest.getInterestedDate());
//                Map<String, String> user = Map.of(
//                        "id", String.valueOf(interest.getId().getInvestorId().getId()),
//                        "inerestedDate", publishDate
//                );
//                userMap.add(user);
//            }
//        }
//        return userMap;
//    }

    //Get the investor interested listing by investor id where finalized date is null
//    public List<InvestorInterestedListingDTO> getPendingListings(Integer id) {
//        return investor_interestedListingRepository.findPendingListings(id);
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -12);
//
//        for(InvestorInterestedListingDTO interest : interests) {
//            if(interest.getInterestedDate().after(calendar.getTime())){
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
//                String publishDate = dateFormat.format(interest.getInterestedDate());
//                Map<String, String> user = Map.of(
//                        "id", String.valueOf(interest.getId().getInvestorId().getId()),
//                        "inerestedDate", publishDate
//                );
//                userMap.add(user);
//            }
//        }
//        return userMap;
//    }

    

    

    

    

    public List<InvestorInterestedListingDTO> getPendingListings(Integer id) {
        return investor_interestedListingRepository.findPendingListings(id);
    }
}
