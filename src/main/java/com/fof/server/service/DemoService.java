package com.fof.server.service;

import com.fof.server.enumeration.Complain;
import com.fof.server.enumeration.Role;
import com.fof.server.model.entity.*;
import com.fof.server.model.normal.DetailsDTO;
import com.fof.server.repository.*;
import com.ventureverse.server.model.entity.*;
import com.ventureverse.server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DemoService {
    private final UserRepository userRepository;
    private final EnterpriseInvestorRepository enterpriseInvestorRepository;
    private final IndividualInvestorRepository individualInvestorRepository;
    private final AdminRepository adminRepository;
    private final ComplainRepository complainRepository;
    private final EntrepreneurRepository entrepreneurRepository;
    private final ListingRepository listingRepository;
    private final Investor_InterestedListingRepository investorInterestedListingRepository;
    private final InvestorRepository investorRepository;
    private final InvestorInterestedSectorRepository investorInterestedSectorRepository;

    // private final EntrepreneurStarRating entrepreneurStarRating;

    public List<AdminDTO> details() {

        return adminRepository.findAll();
    }

    public List<ComplainDTO> findByComplains1() {
        return complainRepository.findAll();
    }

    public List<ComplainDTO> findByComplain(Complain complain) {
        return complainRepository.findByComplainType(complain);
    }


    public ComplainDTO checkComplains(Integer complainId) {
        var complain = complainRepository.findByComplainId(complainId);
        if (complain.isPresent()) {
            ComplainDTO existingComplain = complain.get();
            existingComplain.setComplainType(Complain.REJECTED);
            return complainRepository.save(existingComplain);
        } else {
            return null;
        }
    }

    public ComplainDTO addComplain(Integer complainId, DetailsDTO complainRequest) {

        var complain = complainRepository.findByComplainId(complainId);

        AdminDTO getAdminOptional = adminRepository.findByAdminId(complainRequest.getAdminId());

        if (complain.isPresent()) {

            ComplainDTO existingComplain = complain.get();

            existingComplain.setActionDescription(complainRequest.getActionDescription());
            existingComplain.setAdminId(getAdminOptional);
            existingComplain.setComplainType(Complain.SOLVED);

            return complainRepository.save(existingComplain);
        } else {
            return null;
        }
    }


    public List<Object[]> entrepreneurLeaderboard() {
        List<EntrepreneurDTO> AllEntrepreneur = entrepreneurRepository.findAll();
        List<Object[]> entrepreneurContainers = new ArrayList<>();


        for (EntrepreneurDTO entrepreneurDTO : AllEntrepreneur) {
            Object[] container = new Object[6];
            int count = 0;
            int Id = entrepreneurDTO.getId();
            container[0] = Id;
            container[3] = entrepreneurDTO.getFirstname();
            container[4] = entrepreneurDTO.getLastname();
            container[5] = entrepreneurDTO.getEmail();
            long ListCount = listingRepository.getCountById(entrepreneurDTO);
            container[1] = ListCount;


            List<ListingDTO> EntrepreneursListings = listingRepository.findAllByEntrepreneurId(entrepreneurDTO);

            for (ListingDTO listingDTO : EntrepreneursListings) {
                int TotalListInterestCount = investorInterestedListingRepository.getCountByID(listingDTO);
                count += TotalListInterestCount;
            }
            container[2] = count;

            entrepreneurContainers.add(container);
        }

        Collections.sort(entrepreneurContainers, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] o1, Object[] o2) {
                Integer count1 = (Integer) o1[2];
                Integer count2 = (Integer) o2[2];
                return count2.compareTo(count1);
            }
        });

        return entrepreneurContainers;
    }


    public List<Object[]> investorLeaderboard() {
        List<InvestorDTO> AllInvestors = investorRepository.findAll();
        List<Object[]> investorsContainers = new ArrayList<>();

        for (InvestorDTO investorDTO : AllInvestors) {
            Object[] container1 = new Object[6];
            int amount = 0;
            int Id = investorDTO.getId();

            container1[0] = Id;
            EnterpriseInvestorDTO Inverstor = enterpriseInvestorRepository.findByenterpriseinvestorId(Id);
            if (Inverstor != null) {
                container1[3] = Inverstor.getBusinessName();
                UserDTO Inverstor1 = userRepository.getReferenceById(Id);
                container1[5] = Inverstor1.getEmail();
            } else {
                IndividualInvestorDTO Inverstor1 = individualInvestorRepository.getReferenceById(Id);
                container1[3] = Inverstor1.getFirstname();
                container1[4] = Inverstor1.getLastname();
                container1[5] = Inverstor1.getEmail();

            }

            long ListCount = investorInterestedListingRepository.getCountByInvestorId(investorDTO);
            container1[1] = ListCount;

            List<InvestorInterestedListingDTO> InvestorsListings = investorInterestedListingRepository.findAllByInvestorId(investorDTO);

            for (InvestorInterestedListingDTO InterestedListingDTO : InvestorsListings) {

                if (InterestedListingDTO.getAmountFinalized() != null) {
                    amount += InterestedListingDTO.getAmountFinalized();
                }
            }
            container1[2] = amount;

            investorsContainers.add(container1);
        }

        Collections.sort(investorsContainers, (o1, o2) -> {
            Integer count1 = (Integer) o1[2];
            Integer count2 = (Integer) o2[2];
            return count2.compareTo(count1);
        });


        return investorsContainers;
    }

    public List<ComplainDTO> InverstorViewComplains(UserDTO id) {
        List<ComplainDTO> Complains = complainRepository.findByUserId(id);
        List<ComplainDTO> filteredComplains = new ArrayList<>();

        for (ComplainDTO complain : Complains) {
            if (!(Complain.MARKED).equals(complain.getComplainType())) {
                filteredComplains.add(complain);
            }
        }

        return filteredComplains;
    }

    public ComplainDTO MarkedComplains(Integer id) {

        var complain = complainRepository.findById(id);


        if (complain.isPresent()) {

            ComplainDTO existingComplain = complain.get();

            existingComplain.setComplainType(Complain.MARKED);
            return complainRepository.save(existingComplain);
        } else {
            return null;
        }


    }


    public List<Object> LeaderboardBothService(Integer id) {
        UserDTO user = userRepository.findByUserID(id);

        List<Integer> array1 = new ArrayList<>();
        List<UserDTO> array2 = new ArrayList<>();
        List<Object> ALL = new ArrayList<>();


        // System.out.println(user.getRole());
        if (user.getRole().equals(Role.ENTREPRENEUR)) {
            int array_count = 0;

            double Total_Total_Sale_Projections_This_Year = 0.0;
            double Total_Total_Sale_Projections_Next_Year = 0.0;
            double Total_Net_Income_Last_Year = 0.0;
            double Total_Gross_Income_Last_Year = 0.0;
            double Total_Total_Lifetime_Sales = 0.0;

            List<EntrepreneurDTO> allEntrepreneur = entrepreneurRepository.findAll();

            for (EntrepreneurDTO EntrepreneurOneDTO : allEntrepreneur) {
                List<ListingDTO> EnterLists = listingRepository.findAllByEntrepreneurId(EntrepreneurOneDTO);
                long ListCount = listingRepository.getCountById(EntrepreneurOneDTO);

                double Total_Sale_Projections_This_Year = 0.0;
                double Total_Sale_Projections_Next_Year = 0.0;
                double Net_Income_Last_Year = 0.0;
                double Gross_Income_Last_Year = 0.0;
                double Total_Lifetime_Sales = 0.0;

                int Score = 0;
                double Total_Funding_Raised = 0.0;
                double Project_Success_Rate = 0.0;
                double FUll_Total_NEED_AMOUNT = 0.0;

                int flag = 0;

                for (ListingDTO ListOneDTO : EnterLists) {
                    if (ListOneDTO.getSalesProjectionThisYear() != null) {
                        Total_Sale_Projections_This_Year = Total_Sale_Projections_This_Year + ListOneDTO.getSalesProjectionThisYear();
                        Total_Total_Sale_Projections_This_Year = Total_Total_Sale_Projections_This_Year + Total_Sale_Projections_This_Year;
                    }
                    if (ListOneDTO.getSalesProjectionNextYear() != null) {
                        Total_Sale_Projections_Next_Year = Total_Sale_Projections_Next_Year + ListOneDTO.getSalesProjectionNextYear();
                        Total_Total_Sale_Projections_Next_Year = Total_Total_Sale_Projections_Next_Year + Total_Sale_Projections_Next_Year;
                    }
                    if (ListOneDTO.getLastYearNetIncome() != null) {
                        Net_Income_Last_Year = Net_Income_Last_Year + ListOneDTO.getLastYearNetIncome();
                        Total_Net_Income_Last_Year = Total_Net_Income_Last_Year + Net_Income_Last_Year;
                    }
                    if (ListOneDTO.getLastYearGrossIncome() != null) {
                        Gross_Income_Last_Year = Gross_Income_Last_Year + ListOneDTO.getLastYearGrossIncome();
                        Total_Gross_Income_Last_Year = Total_Gross_Income_Last_Year + Gross_Income_Last_Year;
                    }
                    if (ListOneDTO.getLifetimeSales() != null) {
                        Total_Lifetime_Sales = Total_Lifetime_Sales + ListOneDTO.getLifetimeSales();
                        Total_Total_Lifetime_Sales = Total_Total_Lifetime_Sales + Total_Lifetime_Sales;
                    }
                    if (ListOneDTO.getAttemptsToGrow() != null) {
                        Score += 1;
                    }

                    //           If (Raised Money from Outside Sources == Yes) then
                    //           Score += 2
                    if (ListOneDTO.getOutsideSources() == "Yes") {
                        if (flag == 0) {
                            Score = Score + 2;
                            flag = 1;
                        }

                    }

                    // If (Received Awards or Accolades == True) then
                    // Score += 2
                    if (ListOneDTO.getAwards() != null) {
                        Score += 4;
                    }

                    //Scores equals to
//           If (Total Funding Raised > X) then
//           Score += 10
                    if (ListOneDTO.getExpectedAmount() != null) {
                        FUll_Total_NEED_AMOUNT = FUll_Total_NEED_AMOUNT + ListOneDTO.getExpectedAmount();

                    }

                    List<InvestorInterestedListingDTO> interestedListing = investorInterestedListingRepository.findAllByListingId(ListOneDTO);
                    for (InvestorInterestedListingDTO listInterestedDTO : interestedListing) {
                        if (listInterestedDTO.getAmountFinalized() != null) {
                            Total_Funding_Raised += listInterestedDTO.getAmountFinalized();
                        }
                    }

                    //           If (Project Success Rate > Y%) then
//           Score += 8
                    if (Total_Funding_Raised / FUll_Total_NEED_AMOUNT * 100 > 50 && FUll_Total_NEED_AMOUNT != 0) {
                        Score = Score + 8;
                    }

                }

//If (Sale Projections for This Year > P) then
// Score += 3
                if (Total_Sale_Projections_This_Year / Total_Total_Sale_Projections_This_Year * 100 > 30 && Total_Total_Sale_Projections_This_Year != 0) {
                    Score += 3;
                }
//If (Sale Projections for Next Year > Q) then
//           Score += 3
                if (Total_Sale_Projections_Next_Year / Total_Total_Sale_Projections_Next_Year * 100 > 30 && Total_Total_Sale_Projections_Next_Year > 0) {
                    Score += 3;
                }
//           If (Net Income Last Year > N) then
//         Score += 2
                if (Net_Income_Last_Year > 1000000) {
                    Score += 3;
                }
//           If (Gross Income Last Year > M) then
//           Score += 2
                if (Gross_Income_Last_Year > 1000000) {
                    Score += 3;
                }
//           If (Total Lifetime Sales > W) then
//           Score += 4
                if (Total_Lifetime_Sales > 20) {
                    Score += 3;
                }
//
//           If (Successful Business Building Attempts > R) then
//           Score += 3


                array1.add(Score);
                array2.add(EntrepreneurOneDTO);
                array_count = array_count + 1;


            }

            int n = array1.size();
            boolean swapped;
            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    if (array1.get(j + 1) > array1.get(j)) {
                        // Swap arr[j] and arr[j+1]
                        int temp = array1.get(j);
                        array1.set(j, array1.get(j + 1));
                        array1.set(j + 1, temp);


                        UserDTO temp1 = array2.get(j);
                        array2.set(j, array2.get(j + 1));
                        array2.set(j + 1, temp1);

                        swapped = true;
                    }
                }
                // If no two elements were swapped in the inner loop, the array is already sorted
                if (!swapped) {
                    break;
                }
            }

            ALL.add(array2);
            ALL.add(array1);
            return ALL;


        } else if (user.getRole().equals(Role.INDIVIDUAL_INVESTOR) || user.getRole().equals(Role.ENTERPRISE_INVESTOR)) {

            int array_count = 0;


            if (user.getRole().equals(Role.INDIVIDUAL_INVESTOR)) {

                List<IndividualInvestorDTO> allINDIVIDUAL_INVESTOR = individualInvestorRepository.findAll();

                for (IndividualInvestorDTO individualInvestor : allINDIVIDUAL_INVESTOR) {

                    double Total_Investments = 0.0;
                    int Score = 0;
                    long Count = 0l;
                    int Investment_Success_Rate = 0;

                    List<InvestorInterestedListingDTO> InterstedList = investorInterestedListingRepository.findAllByInvestorId(individualInvestor);
                    long ListCount = listingRepository.count();

                    for (InvestorInterestedListingDTO Lists : InterstedList) {
                        Count = Count + 1;
                        if (Lists.getAmountFinalized() != null) {
                            Total_Investments = Total_Investments + Lists.getAmountFinalized();
                        }
                        if (Lists.getStatus() == "APPROVED") {
                            Investment_Success_Rate = Investment_Success_Rate + 1;

                        }


                    }


//        If (Total Investments > B) then
//        Score += 10

                    if (Total_Investments <= 1000000) {
                        Score = Score + 10;
                    }

//
//        If (Investment Success Rate > C%) then
//        Score += 8
                    if (Count > 0) {
                        if (Investment_Success_Rate / Count * 100 > 40) {
                            Score = Score + 8;
                        }
                    }
//
//        If (Portfolio Diversification > D) then
//        Score += 6
                    Integer TypeCount = investorInterestedSectorRepository.getCountByInverstorId(individualInvestor);
                    Score = Score + TypeCount;


//        If (Active Participation == True) then
//        Score += 5
                    if (Count > 1) {
                        if (Count / ListCount * 100 > 40) {
                            Score += 5;
                        }
                    }


                    array1.add(Score);
                    array2.add(individualInvestor);
                    array_count = array_count + 1;


                }


            }


            if (user.getRole().equals(Role.ENTERPRISE_INVESTOR)) {


                List<EnterpriseInvestorDTO> EnterpriseInvestor = enterpriseInvestorRepository.findAll();

                for (EnterpriseInvestorDTO enterpriseInvestor : EnterpriseInvestor) {

                    double Total_Investments = 0.0;
                    int Score = 0;
                    long Count = 0l;
                    int Investment_Success_Rate = 0;

                    List<InvestorInterestedListingDTO> InterstedList = investorInterestedListingRepository.findAllByInvestorId(enterpriseInvestor);
                    long ListCount = listingRepository.count();

                    for (InvestorInterestedListingDTO Lists : InterstedList) {
                        Count = Count + 1;
                        if (Lists.getAmountFinalized() != null) {
                            Total_Investments = Total_Investments + Lists.getAmountFinalized();
                        }

                        if (Lists.getStatus() == "APPROVED") {
                            Investment_Success_Rate = Investment_Success_Rate + 1;

                        }


                    }


//        If (Total Investments > B) then
//        Score += 10

                    if (Total_Investments <= 1000000) {
                        Score = Score + 10;
                    }

//
//        If (Investment Success Rate > C%) then
//        Score += 8
                    if (Count > 0) {
                        if (Investment_Success_Rate / Count * 100 > 40) {
                            Score = Score + 8;
                        }
                    }
//
//        If (Portfolio Diversification > D) then
//        Score += 6
                    Integer TypeCount = investorInterestedSectorRepository.getCountByInverstorId1(enterpriseInvestor);
                    Score = Score + TypeCount;
//


//        If (Active Participation == True) then
//        Score += 5
                    if (Count > 1) {
                        if (Count / ListCount * 100 > 40) {
                            Score += 5;
                        }
                    }


                    array1.add(Score);
                    array2.add(enterpriseInvestor);
                    array_count = array_count + 1;


                }


            }

            //setting the sorting Algo

            int n = array1.size();
            boolean swapped;
            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    if (array1.get(j + 1) > array1.get(j)) {
                        // Swap arr[j] and arr[j+1]
                        int temp = array1.get(j);
                        array1.set(j, array1.get(j + 1));
                        array1.set(j + 1, temp);


                        UserDTO temp1 = array2.get(j);
                        array2.set(j, array2.get(j + 1));
                        array2.set(j + 1, temp1);

                        swapped = true;
                    }
                }
                // If no two elements were swapped in the inner loop, the array is already sorted
                if (!swapped) {
                    break;
                }
            }

            ALL.add(array2);
            ALL.add(array1);
            return ALL;

        }
        return null;
    }

    public DetailsDTO GetInformation(Integer id) {
        UserDTO user = userRepository.findByUserID(id);

        if (user.getRole().equals(Role.ENTREPRENEUR)) {
            EntrepreneurDTO Entrepreneur = entrepreneurRepository.getReferenceById(id);

            return DetailsDTO.builder()
                    .firstname(Entrepreneur.getFirstname())
                    .lastname(Entrepreneur.getLastname())
                    .bfirstLineAddress(Entrepreneur.getBfirstLineAddress())
                    .bsecondLineAddress(Entrepreneur.getBsecondLineAddress())
                    .btown(Entrepreneur.getBtown())
                    .bdistrict(Entrepreneur.getBdistrict())
                    .gender(Entrepreneur.getGender())
                    .contactNumber(Entrepreneur.getContactNumber())
                    .email(Entrepreneur.getBusinessEmail())
                    .build();

        } else if (user.getRole().equals(Role.INDIVIDUAL_INVESTOR)) {
            IndividualInvestorDTO INDIVIDUAL_INVESTOR = individualInvestorRepository.getReferenceById(id);

            return DetailsDTO.builder()
                    .firstname(INDIVIDUAL_INVESTOR.getFirstname())
                    .lastname(INDIVIDUAL_INVESTOR.getLastname())
                    .gender(INDIVIDUAL_INVESTOR.getGender())

                    .build();

        } else {
            EnterpriseInvestorDTO EnterpriseInvestor = enterpriseInvestorRepository.getReferenceById(id);

            return DetailsDTO.builder()
                    .firstname(EnterpriseInvestor.getBusinessName())
                    .build();
        }


    }

//    public EntreprenenrStarRatingDTO GivingStarRating(EntreprenenrStarRatingDTO starRating) {
//        EntrepreneurDTO entId= starRating.getId().getEntrepreneurId();
//        UserDTO userID = starRating.getId().getId();
//
//
//        EntreprenenrStarRatingDTO star = entrepreneurStarRating.findById(entId,userID);
//
////        AdminDTO getAdminOptional = adminRepository.findByAdminId(complainRequest.getAdminId());
//
//        if (star.isPresent()) {
//            star.setStarRating(starRating.starRating());
//            return entrepreneurStarRating.save(star);
//        }
//        else
//        {
//            return entrepreneurStarRating.save(starRating);
//
//        }
//
//
//    }

//    public Integer getStarCount(Integer id) {
//        EntrepreneurDTO  entrepreneur=
//     }
}





