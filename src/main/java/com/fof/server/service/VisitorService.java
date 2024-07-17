package com.fof.server.service;

import com.fof.server.enumeration.Role;
import com.fof.server.model.normal.DetailsDTO;
import com.fof.server.repository.EnterpriseInvestorRepository;
import com.fof.server.repository.IndividualInvestorRepository;
import com.fof.server.repository.InvestorInterestedListingRepository;
import com.fof.server.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final ListingRepository listingRepository;
    private final InvestorInterestedListingRepository investorInterestedListingRepository;
    private final IndividualInvestorRepository individualInvestorRepository;
    private final EnterpriseInvestorRepository enterpriseInvestorRepository;

    public List<Object> home() throws IOException {

        List<DetailsDTO> listingDetails = new ArrayList<DetailsDTO>();
        List<DetailsDTO> investorDetails = new ArrayList<DetailsDTO>();

        var Listings = listingRepository.findAll();

        String rootDirectory = System.getProperty("user.dir");
        String imageUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/thumbnails";
        String profileUploadPath = rootDirectory + "/src/main/resources/static/uploads/images/profileImages";

        Listings.forEach(listing -> {
            Path imagePath = Paths.get(imageUploadPath, listing.getThumbnail());
            float percentage;
            var amount = investorInterestedListingRepository.findFinalizedAmount(listing);
            if ( amount == null) {
                percentage = 0;
            } else {
                percentage = (float) amount / listing.getExpectedAmount() * 100;
            }

            try {
                listingDetails.add(DetailsDTO.builder()
                        .businessName(listing.getEntrepreneurId().getBusinessName())
                        .title(listing.getTitle())
                        .description(listing.getDescription())
                        .returnEquityPercentage(listing.getReturnEquityPercentage())
                        .expectedAmount(listing.getExpectedAmount())
                        .returnUnitProfitPercentage(listing.getReturnUnitProfitPercentage())
                        .thumbnail(Files.readAllBytes(imagePath))
                        .video(listing.getPitchingVideo())
                        .investmentPercentage(percentage)
                        .build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        listingDetails.sort((o1, o2) -> {
            return o2.getInvestmentPercentage().compareTo(o1.getInvestmentPercentage());
        });

        var investors = investorInterestedListingRepository.findInvestors();

        investors.forEach(investor -> {

            var name = "";
            var type = "";

            if (investor.getRole().equals(Role.INDIVIDUAL_INVESTOR)){
                name = individualInvestorRepository.findById(investor.getId()).orElseThrow().getFirstname() + " " + individualInvestorRepository.findById(investor.getId()).orElseThrow().getLastname();
                type = "Individual Investor";
            } else if (investor.getRole().equals(Role.ENTERPRISE_INVESTOR)) {
                name = enterpriseInvestorRepository.findById(investor.getId()).orElseThrow().getBusinessName();
                type = "Enterprise Investor";
            }

            Path profilePath = Paths.get(profileUploadPath, investor.getProfileImage());

            try {
                investorDetails.add(DetailsDTO.builder()
                        .investorName(name)
                        .investorQuote("We focus on ergonomics and meeting you where you work. It's only a keystroke away.We focus on ergonomics and meeting you where you work. It's only a keystroke away.")
                        .investorType(type)
                        .profileImage(Files.readAllBytes(profilePath))
                        .build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<Object> home = new ArrayList<Object>();
        home.add(listingDetails);
        home.add(investorDetails);

        return home;

    }

}
