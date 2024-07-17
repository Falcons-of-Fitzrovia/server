package com.fof.server.controller;

import com.fof.server.enumeration.Complain;
import com.fof.server.model.entity.AdminDTO;
import com.fof.server.model.entity.ComplainDTO;
import com.fof.server.model.entity.UserDTO;
import com.fof.server.model.normal.DetailsDTO;
import com.fof.server.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class DemoController {

    private final DemoService demoService;


    @GetMapping("/details")
    public ResponseEntity<List<AdminDTO>> getDetails(
    ) {
        return ResponseEntity.ok(demoService.details());
    }


    @GetMapping("/pendingComplains")
    public ResponseEntity<List<ComplainDTO>> pendingComplains() {
        return ResponseEntity.ok(demoService.findByComplains1());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ComplainDTO>> getComplain() {
        List<ComplainDTO> pendingComplain = demoService.findByComplain(Complain.PENDING);
        if (pendingComplain.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pendingComplain);
    }

    @PutMapping ("/IgnoreComplain/{id}")
    public ResponseEntity<String> ignoreComplain(
            @PathVariable Integer id
    ) {
        ComplainDTO IgnoreComplain = demoService.checkComplains(id);

        if(IgnoreComplain !=null){
            return ResponseEntity.ok("Ignore complain done successfully");
        }
        else{
            return ResponseEntity.ok("Invalid Id");
        }
    }

    @PutMapping ("/ActionTaken/{id}")
    public ResponseEntity<String> acceptComplain(
            @RequestBody DetailsDTO complainRequest,
            @PathVariable Integer id
    ) {
     ComplainDTO IgnoreComplain = demoService.addComplain(id,complainRequest);
     if(IgnoreComplain !=null){
            return ResponseEntity.ok("  done successfully");
      }
       else{
         return ResponseEntity.ok("Invalid Id");
       }
    }

    @GetMapping("/EntrepreneurLeaderboard")
    public List<Object[]> entrepreneurLeaderboard() {
        List<Object[]> AllEnter = demoService.entrepreneurLeaderboard();

        return AllEnter;
    }


    @GetMapping("/InvestorLeaderboard")
    public List<Object[]> InvestorLeaderboard() {
        List<Object[]> AllObjects = demoService.investorLeaderboard();
        return AllObjects;
    }


    @GetMapping("/PastComplains/{id}")
    public List<ComplainDTO> InverstorViewComplains(
            @PathVariable UserDTO id
    ){
        List<ComplainDTO> Complains = demoService.InverstorViewComplains(id);

        return Complains;
    }


    @PutMapping("/MarkedComplains/{id}")
    public ResponseEntity<String> MarkedComplains(
            @PathVariable Integer id
    ){
        ComplainDTO MarkedComplains = demoService.MarkedComplains(id);

        if(MarkedComplains !=null){
            return ResponseEntity.ok("  done successfully");
        }
        else{
            return ResponseEntity.ok("Invalid Id");
        }


    }


//    @PutMapping("/GivingStarRating")
//    public ResponseEntity<String>  GivingStarRating(
//            @RequestBody EntreprenenrStarRatingDTO starRating
//       //     @PathVariable Integer id
//    ){
//        EntreprenenrStarRatingDTO Star = demoService.GivingStarRating(starRating);
//
//        if(Star !=null){
//            return ResponseEntity.ok("  done successfully");
//        }
//        else{
//            return ResponseEntity.ok("Invalid Id");
//        }
//    }


    @GetMapping("/GivingStarRatingBoth/{id}")
    public List<Object> LeaderBordBoth(
            @PathVariable Integer id
    )
    {
        return demoService.LeaderboardBothService(id);

    }

    @GetMapping("/GetInformation/{id}")
    public  DetailsDTO GetInformation(
            @PathVariable  Integer id
    ){

        DetailsDTO information = demoService.GetInformation(id);

        return information;
    }


















    }













