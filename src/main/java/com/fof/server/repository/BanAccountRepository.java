package com.fof.server.repository;

import com.fof.server.model.entity.BanAccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanAccountRepository extends JpaRepository<BanAccountDTO, Integer> {
}
