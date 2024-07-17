package com.fof.server.service;

import com.fof.server.model.entity.*;
import com.fof.server.model.normal.ListingRequestDTO;
import com.fof.server.repository.*;
import com.ventureverse.server.model.entity.*;
import com.fof.server.model.normal.ResponseDTO;
import com.ventureverse.server.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.exit;

@Service
@RequiredArgsConstructor
public class ListingService {

    public final ListingRepository listingRepository;
    private final EntrepreneurRepository entrepreneurRepository;
    private final IndustrySectorRepository industrySectorRepository;
    private final ListingIndustrySectorsRepository listingIndustrySectorsRepository;
    private final ListingImagesRepository listingImagesRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ListingSubscriptionRepository listingSubscriptionRepository;
    private final Investor_InterestedListingRepository investor_interestedListingRepository;
    private final CounterProposalRepository counterProposalRepository;

    public ResponseDTO addListing(HttpServletResponse response, ListingRequestDTO listingRequestDTO) {
        var entrepreneur = entrepreneurRepository.findById(listingRequestDTO.getEntrepreneurId()).orElseThrow();
        var subscription = subscriptionRepository.findById(listingRequestDTO.getSubscriptionType()).orElseThrow();

        var list = ListingDTO.builder()
                .title(listingRequestDTO.getTitle())
                .description(listingRequestDTO.getDescription())
                .thumbnail(listingRequestDTO.getThumbnail())
                .pitchingVideo(listingRequestDTO.getPitchingVideo())
                .intention(listingRequestDTO.getIntention())
                .businessStartDate(listingRequestDTO.getBusinessStartDate())
                .businessDuration(listingRequestDTO.getBusinessDuration())
                .lifetimeSales(listingRequestDTO.getLifetimeSales())
                .lastYearGrossIncome(listingRequestDTO.getLastYearGrossIncome())
                .lastYearNetIncome(listingRequestDTO.getLastYearNetIncome())
                .salesProjectionThisYear(listingRequestDTO.getSalesProjectionThisYear())
                .salesProjectionNextYear(listingRequestDTO.getSalesProjectionNextYear())
                .projectionMethod(listingRequestDTO.getProjectionMethod())
                .outsideSources(listingRequestDTO.getOutsideSources())
                .outsideSourceDescription(listingRequestDTO.getOutsideSourceDescription())
                .attemptsToGrow(listingRequestDTO.getAttemptsToGrow())
                .awards(listingRequestDTO.getAwards())
                .uniqueSellingProposition(listingRequestDTO.getUniqueSellingProposition())
                .stage(listingRequestDTO.getStage())
                .expectedAmount(listingRequestDTO.getExpectedAmount())
                .returnUnitProfitPercentage(listingRequestDTO.getReturnUnitProfitPercentage())
                .returnEquityPercentage(listingRequestDTO.getReturnEquityPercentage())
                .subscriptionType(subscription)
                .publishedDate(listingRequestDTO.getPublishedDate())
                .status(listingRequestDTO.getStatus())
                .entrepreneurId(entrepreneur)
                .build();
        listingRepository.save(list);

        // Get the listing id inserted to the listing table by the above code
        var listingId = listingRepository.findLastInsertedListing();

        var listingSectors = listingRequestDTO.getSectorId();
        for (Integer sectorId : listingSectors) {
            var sector = industrySectorRepository.findById(sectorId).orElseThrow();
            var listingSectorObject = ListingIndustrySectorsDTO.builder()
                    .id(new ListingIndustrySectorsDTO.CompositeKey(listingId, sector))
                    .build();
            listingIndustrySectorsRepository.save(listingSectorObject);
        }

        // Update the listing images table
        var listingImages = listingRequestDTO.getImages();
        for (String image : listingImages) {
            listingImagesRepository.save(ListingImagesDTO.builder()
                    .id(new ListingImagesDTO.CompositeKey(listingId, image))
                    .build());
        }
        return GlobalService.response("Success", "Listing added successfully");
    }

    // Function to get the listing details by id
    public ListingDTO getListing(int id) {
        return listingRepository.findById(id).orElseThrow();
    }

    // Function to get the subscriptiontype of a listing
    public ListingSubscriptionDTO getSubscriptionType(int id) {
        // Get the listigDTO object
        var listing = listingRepository.findById(id).orElseThrow();
        return listingSubscriptionRepository.findByListingId(listing).orElseThrow();
    }

    public List<Map<String, String>> getUserGains() {
        List<ListingDTO> listings = listingRepository.findAll();
        List<Map<String, String>> userMap = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);

        for (ListingDTO listing : listings) {
            if (listing.getPublishedDate().after(calendar.getTime())) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
                String publishDate = dateFormat.format(listing.getPublishedDate());

                Map<String, String> map = Map.of(
                        "id", listing.getListingId().toString(),
                        "publishedDate", publishDate,
                        "subscriptionprice", listing.getSubscriptionType().getPrice().toString());
                userMap.add(map);
            }
        }
        return userMap;
    }

    public List<ListingDTO> getAllListings() {
        var basicDetails = listingRepository.findAll();
        return basicDetails;
    }

//    public List<Map<String, String>> getAllListings() {
//        List<ListingDTO> listings = listingRepository.findAll();
//        List<InvestorInterestedListingDTO> completedListings = investor_interestedListingRepository
//                .findCompletedListings();
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -12);
//
//        List<Map<String, String>> userMap = new ArrayList<>();
//        List<Integer> completedListingIds = new ArrayList<>();
//        for (InvestorInterestedListingDTO completedListing : completedListings) {
//            if (completedListing.getFinalizedDate().after(calendar.getTime())) {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
//                String finalizedDate = dateFormat.format(completedListing.getFinalizedDate());
//                completedListingIds.add(completedListing.getId().getListingId().getListingId());
//                Map<String, String> map = Map.of(
//                        "id", completedListing.getId().getListingId().getListingId().toString(),
//                        "date", finalizedDate,
//                        "status", "Completed");
//                userMap.add(map);
//            }
//        }
//        // Listings that are in progress
//        for (ListingDTO listing : listings) {
//            if (!completedListingIds.contains(listing.getListingId())
//                    && listing.getPublishedDate().after(calendar.getTime())) {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
//                String publishDate = dateFormat.format(listing.getPublishedDate());
//                Map<String, String> map = Map.of(
//                        "id", listing.getListingId().toString(),
//                        "date", publishDate,
//                        "status", "In Progress");
//                userMap.add(map);
//            }
//        }
//        return userMap;
//    }

    public List<Map<String, String>> getAllListings2() {
        List<ListingDTO> listings = listingRepository.findAll();
        List<InvestorInterestedListingDTO> completedListings = investor_interestedListingRepository
                .findCompletedListings();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);

        List<Map<String, String>> userMap = new ArrayList<>();
        List<Integer> completedListingIds = new ArrayList<>();
        for (InvestorInterestedListingDTO completedListing : completedListings) {
            if (completedListing.getFinalizedDate().after(calendar.getTime())) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
                String finalizedDate = dateFormat.format(completedListing.getFinalizedDate());
                completedListingIds.add(completedListing.getId().getListingId().getListingId());
                Map<String, String> map = Map.of(
                        "id", completedListing.getId().getListingId().getListingId().toString(),
                        "date", finalizedDate,
                        "status", "Completed");
                userMap.add(map);
            }
        }
        // Listings that are in progress
        for (ListingDTO listing : listings) {
            if (!completedListingIds.contains(listing.getListingId())
                    && listing.getPublishedDate().after(calendar.getTime())) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
                String publishDate = dateFormat.format(listing.getPublishedDate());
                Map<String, String> map = Map.of(
                        "id", listing.getListingId().toString(),
                        "date", publishDate,
                        "status", "In Progress");
                userMap.add(map);
            }
        }
        return userMap;
    }

    // Function to get the latest listing of an entrepreneur
    public ListingDTO getLatestListing(int id) {
        // Get the entrepreneur object
        var entrepreneur = entrepreneurRepository.findById(id).orElseThrow();

        // Get the latest listing of the entrepreneur
        var listing = listingRepository.findLatestListing(entrepreneur);
        if (listing == null) {
            exit(0);
        }
        return listing;
    }

    // Function to get the listing from the listing id
    public ListingDTO getListingFromListingId(int id) {
        return listingRepository.findById(id).orElseThrow();
    }

    // Function to get the listing video
    public String getVideo(int id) {
        // Get the listing object
        var listing = listingRepository.findById(id).orElseThrow();
        return listing.getPitchingVideo();
    }

    public InvestorInterestedListingDTO finalizeListing(Integer id) {
        return investor_interestedListingRepository.findByListingId(id);
    }

    public ResponseDTO updateDate(Integer id, Integer investorid, InvestorInterestedListingDTO i) {
        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setListingId(id);

        Optional<InvestorInterestedListingDTO> listing = investor_interestedListingRepository
                .findByInvestorIdAndListingId2(investorid, id);
        if (listing.isPresent()) {
            InvestorInterestedListingDTO oldListing = listing.get();
            oldListing.setFinalizedDate(i.getFinalizedDate());
            oldListing.setStatus("Finalized");
            investor_interestedListingRepository.save(oldListing);
            return GlobalService.response("Success", "Listing updated successfully");
        } else {
            return GlobalService.response("Error", "Listing not found");
        }
    }

    public List<String> getListingImages(ListingDTO i) {
        return listingImagesRepository.getListingImages(i);
    }

    public ResponseDTO addInterestedListing(List<Integer> listingIds) {
        var listingid = listingIds.get(0);
        var investorid = listingIds.get(1);
        var returnEquityPercentage = listingIds.get(2);
        var returnUnitProfitPercentage = listingIds.get(3);

        ListingDTO listingDTO = new ListingDTO();
        listingDTO.setListingId(listingid);

        InvestorDTO investorDTO = new InvestorDTO();
        investorDTO.setId(investorid);

        Date date = new Date();

        InvestorInterestedListingDTO investorInterestedListingDTO = new InvestorInterestedListingDTO();
        investorInterestedListingDTO.setId(new InvestorInterestedListingDTO.CompositeKey(investorDTO, listingDTO));
        investorInterestedListingDTO.setInterestedDate(date);
        investorInterestedListingDTO.setStatus("PENDING");
        investorInterestedListingDTO.setReturnEquityPercentage(returnEquityPercentage);
        investorInterestedListingDTO.setReturnUnitProfitPercentage(returnUnitProfitPercentage);

        investor_interestedListingRepository.save(investorInterestedListingDTO);

        if (investor_interestedListingRepository.findByListingId(listingid) != null) {
            return GlobalService.response("Success", "Interested listing added successfully");
        } else {
            return GlobalService.response("Error", "Interested listing not added");
        }
    }

    public ResponseDTO counterProposal(CounterProposalDTO counterProposalDTO) {
        ListingDTO listingDTO = counterProposalDTO.getListingId();
        EntrepreneurDTO EntrepreneurDTO = listingRepository.getEntrepreneur(listingDTO.getListingId());
        Date date = new Date();

        CounterProposalDTO counterProposalDTO1 = new CounterProposalDTO();
        counterProposalDTO1.setListingId(listingDTO);
        counterProposalDTO1.setEntrepreneurId(EntrepreneurDTO);
        counterProposalDTO1.setInvestorId(counterProposalDTO.getInvestorId());
        counterProposalDTO1.setDate(date);
        counterProposalDTO1.setReturnEquityPercentage(counterProposalDTO.getReturnEquityPercentage());
        counterProposalDTO1.setReturnUnitProfitPercentage(counterProposalDTO.getReturnUnitProfitPercentage());
        counterProposalDTO1.setAmount(counterProposalDTO.getAmount());

        counterProposalRepository.save(counterProposalDTO1);

        return GlobalService.response("Success", "Counter proposal added successfully");
    }

    public List<String> getListingSectors(ListingDTO listing) {
        return listingIndustrySectorsRepository.getListingSectors(listing);
    }

    public Integer getCompletedInvestment(ListingDTO listing) {
        return investor_interestedListingRepository.getCompletedInvestment(listing);
    }

    public List<ListingDTO> getAllFinalizedListings() {
        return listingRepository.getAllFinalizedListings();
    }

    public List<InvestorInterestedListingDTO> getInterestedInvestors(ListingDTO listing) {
        return investor_interestedListingRepository.getInterestedInvestors(listing);
    }

    public ResponseDTO deleteListing(int id) {
        // Get the listing object
        var listing = listingRepository.findById(id).orElseThrow();
        listing.setStatus("DELETED");
        listingRepository.save(listing);
        return GlobalService.response("Success", "Listing deleted successfully");
    }

    public ResponseDTO checkActiveListing(Integer id) {
        // Get the entrepreneur object related to the entrepreneur id
        var entrepreneur = entrepreneurRepository.findById(id).orElseThrow();

        // Find the latest listing object relevant to the user id
        String status = listingRepository.findLatestListingStatus(entrepreneur);

        if (status.equals("Active")) {
            return GlobalService.response("Success", "Active listing found");
        } else {
            return GlobalService.response("Error", "Active listing not found");
        }

    }

    public ResponseDTO updateListing(Integer id, ListingDTO listingDTO) {
        ListingDTO listingDTOfinal = new ListingDTO();
        listingDTOfinal.setListingId(id);

        // Get the subscription object related to the subscription id
        var subscription = subscriptionRepository.findById(listingDTO.getSubscriptionType().getSubscriptionId())
                .orElseThrow();

        Optional<ListingDTO> listing = listingRepository.findById(id);
        if (listing.isPresent()) {
            ListingDTO oldListing = listing.get();
            oldListing.setPublishedDate(listingDTO.getPublishedDate());
            oldListing.setSubscriptionType(subscription);
            listingRepository.save(oldListing);
            return GlobalService.response("Success", "Listing updated successfully");
        } else {
            return GlobalService.response("Error", "Listing not found");
        }
    }

    public long countListings() {
        return listingRepository.count();
    }

    public List<InvestorInterestedListingDTO> finalizeListings(Integer id) {
        return investor_interestedListingRepository.finalizeListings(id);
    }

    public void updateListing(ListingDTO listingDTO) {
        listingRepository.save(listingDTO);
    }
    
}