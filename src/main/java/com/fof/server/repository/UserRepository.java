package com.fof.server.repository;

import com.fof.server.enumeration.Role;
import com.fof.server.enumeration.Status;
import com.fof.server.model.entity.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDTO, Integer> {

    @Query("""
        SELECT u.approvalStatus
        FROM UserDTO u
        WHERE u.id = :id
    """)
    Status findApprovalById(Integer id);

    @Query("""
        SELECT u.approvalStatus
        FROM UserDTO u
        WHERE u.email = :email
    """)
    Status findApprovalByEmail(String email);

    Optional<UserDTO> findByEmail(String email);

    @Query("""
        SELECT u.profileImage
        FROM UserDTO u
        WHERE u.id = :id
    """)
    String getimage(Integer id);


}
