package com.fof.server.repository;

import com.fof.server.model.entity.SubscriptionDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<SubscriptionDTO, Integer> {
}
