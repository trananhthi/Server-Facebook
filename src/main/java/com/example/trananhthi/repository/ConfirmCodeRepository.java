package com.example.trananhthi.repository;

import com.example.trananhthi.entity.ConfirmCode;
import com.example.trananhthi.entity.UserAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmCodeRepository extends CrudRepository<ConfirmCode,Long> {
    Optional<ConfirmCode> findByCode(String code);
    List<ConfirmCode> findAll();
    @Modifying
    void deleteByCode(String code);
    @Modifying
    void deleteAllByUserAccount(UserAccount userAccount);
}
