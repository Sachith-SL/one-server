package com.sachith.one_server.controller;

import com.sachith.one_server.dto.*;
import com.sachith.one_server.exception.UnauthorizedException;
import com.sachith.one_server.model.AppUser;
import com.sachith.one_server.model.RefreshToken;
import com.sachith.one_server.repository.RefreshTokenRepository;
import com.sachith.one_server.repository.UserRepository;
import com.sachith.one_server.security.JwtUtil;
import com.sachith.one_server.service.RefreshTokenService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/one/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepo;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, UserRepository userRepository,
                          RefreshTokenRepository refreshTokenRepo,
                            RefreshTokenService refreshTokenService,
                          PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.refreshTokenRepo = refreshTokenRepo;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {

        // Authenticate the user, the process begins here.
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow();

        // 🔴 THIS MUST EXIST
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        String accessToken = jwtUtil.generateToken(
                auth.getName(),
                auth.getAuthorities()
        );

        return ResponseEntity.ok(new TokenResponse(accessToken,refreshToken.getToken()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if(userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.addRole("USER");

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/refresh")
    public TokenResponse refreshToken(@RequestHeader("Authorization") String header) {

        String refreshTokenValue = extract(header);

        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(refreshTokenValue);
        AppUser user = refreshToken.getAppUser();

        //issue authorities not mention here
        String newAccessToken =
                jwtUtil.generateToken(
                        user.getUsername(),
                        user.getAuthorities()
                );
        return new TokenResponse(newAccessToken, refreshTokenValue);
    }

    private String extract(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing refresh token");
        }
        return header.substring(7);
    }

}

