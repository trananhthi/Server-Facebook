package com.example.trananhthi.repository;
import com.example.trananhthi.entity.UserAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount,Long> {
    List<UserAccount> findByEmail(String email);
    Optional<UserAccount> findById(Long id);
    @Modifying
    void deleteUserAccountsByStatusAndTimeCreatedBefore(String status, Date timeCreated);
    @Modifying
    @Query("UPDATE UserAccount ua SET ua.status = :status WHERE ua.email = :email")
    void updateStatusByEmail(@Param("email") String email,String status);
}



