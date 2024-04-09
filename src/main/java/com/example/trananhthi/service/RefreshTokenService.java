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
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAccountRepository userAccountRepository;
    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserAccountRepository userAccountRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken = new RefreshToken();
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(email);
        refreshToken.setUserAccount(userAccount.orElse(null));
        refreshToken.setExpiryDate(Instant.now().plusMillis(1000*60*60*72));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("RefreshTokenIsExpired", "Vui lòng đăng nhập lại");
        }

        return token;
    }
}
