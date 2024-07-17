package com.fof.server.repository;

import com.fof.server.model.entity.InvestorDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestorRepository extends JpaRepository<InvestorDTO, Integer> {
    @Override
    List<InvestorDTO> findAll();
    InvestorDTO findAllById(Integer id);
}
