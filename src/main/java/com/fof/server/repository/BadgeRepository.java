package com.fof.server.repository;

import com.fof.server.model.entity.BadgeDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<BadgeDTO, Integer> {
}
