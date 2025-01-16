package com.example.trananhthi.service;

import com.example.trananhthi.entity.RefreshToken;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.exception.TokenRefreshException;
import com.example.trananhthi.repository.RefreshTokenRepository;
import com.example.trananhthi.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(String email);

    RefreshToken verifyExpiration(RefreshToken token);
}
