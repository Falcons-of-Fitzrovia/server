package com.fof.server.repository;

import com.fof.server.enumeration.Status;
import com.fof.server.model.entity.EntrepreneurDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntrepreneurRepository extends JpaRepository<EntrepreneurDTO, Integer> {
    //check whether there are two business emails or not
    Optional<EntrepreneurDTO> findByBusinessEmail(String businessEmail);

    Optional<EntrepreneurDTO> findById(Integer id);


    List<EntrepreneurDTO> findByApprovalStatus(Status ApprovalStatus);

    //Function to get the entrepreneur by the userId
    EntrepreneurDTO findAllById(Integer id);



}

