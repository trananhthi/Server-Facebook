package com.example.trananhthi.repository;
import com.example.trananhthi.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount,String> {
    Optional<UserAccount> findByEmail(String email);

    List<UserAccount> findByNameContaining(String name);

    @Query("SELECT ua FROM UserAccount ua WHERE ua.id IN :userIds")
    List<UserAccount> findAllByIdIn(List<String> userIds);

    Optional<UserAccount> findById(String id);

    @Modifying
    void deleteUserAccountsByStatusAndCreatedAtBefore(String status, LocalDateTime createdTime);

    @Modifying
    @Transactional
    @Query("UPDATE UserAccount ua SET ua.status = :status WHERE ua.email = :email")
    void updateStatusByEmail(@Param("email") String email,String status);
}



