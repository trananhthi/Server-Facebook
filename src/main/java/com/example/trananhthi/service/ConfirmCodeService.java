package com.example.trananhthi.service;

import com.example.trananhthi.entity.ConfirmCode;
import com.example.trananhthi.entity.UserAccount;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ConfirmCodeService {
    Optional<ConfirmCode> findByCode(String code);

    @Transactional
    void deleteByCode(String code);

    @Transactional
    void deleteAllByUserAccount(UserAccount userAccount);

    ConfirmCode createConfirmCode(String email);

    boolean isExpiredCode(ConfirmCode code);
}
