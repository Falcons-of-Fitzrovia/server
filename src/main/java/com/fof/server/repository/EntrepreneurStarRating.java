package com.fof.server.repository;

import com.fof.server.model.entity.EntreprenenrStarRatingDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrepreneurStarRating extends JpaRepository<EntreprenenrStarRatingDTO, Integer> {

//    EntreprenenrStarRatingDTO findById(EntrepreneurDTO entId, UserDTO userID);
}
