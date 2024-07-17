package com.fof.server.repository;

import com.fof.server.model.entity.BusinessDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<BusinessDTO, Integer> {
}
