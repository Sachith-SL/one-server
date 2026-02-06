package com.sachith.one_server.service;

import com.sachith.one_server.exception.UnauthorizedException;
import com.sachith.one_server.model.AppUser;
import com.sachith.one_server.model.RefreshToken;
import com.sachith.one_server.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRY_DAYS = 7;

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RefreshToken createRefreshToken(AppUser user) {
        RefreshToken token = new RefreshToken();
        token.setAppUser(user);
        //check
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRY_DAYS * 86400));
        return repository.save(token);
    }

    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new UnauthorizedException("Refresh token expired");
        }
        return refreshToken;
    }
}

