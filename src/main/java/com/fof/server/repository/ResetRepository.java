package com.fof.server.repository;

import com.fof.server.model.entity.ResetDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResetRepository extends JpaRepository<ResetDTO, Integer> {

    Optional<ResetDTO> findByToken(String token);


}
