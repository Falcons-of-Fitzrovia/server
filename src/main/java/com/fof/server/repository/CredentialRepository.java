package com.fof.server.repository;


import com.fof.server.model.entity.CredentialDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<CredentialDTO, Integer> {

    @Query("""
        SELECT c.salt
        FROM CredentialDTO c
        WHERE c.user.email = :email
    """)
    Optional<String> findSaltByEmail(String email);

    Optional<CredentialDTO> findByUsername(String email);

}
