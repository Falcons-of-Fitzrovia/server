package com.fof.server.repository;

import com.fof.server.model.entity.TokenDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenDTO, Integer> {

    @Query("""
        SELECT t
        FROM TokenDTO t
        INNER JOIN UserDTO u
        ON t.user.id = u.id
        WHERE u.id = :userId and (t.expired = false or t.revoked = false)
    """)
    List<TokenDTO> findAllValidTokenByUser(Integer userId);

    Optional<TokenDTO> findByToken(String token);

    Optional<TokenDTO> findByUser_Id(Integer id);

    Optional<TokenDTO> findByExpired(boolean expired);

    void deleteByUser_Id(Integer id);


}
