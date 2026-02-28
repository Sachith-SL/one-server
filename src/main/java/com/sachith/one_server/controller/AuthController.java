package com.sachith.one_server.controller;

import com.sachith.one_server.dto.*;
import com.sachith.one_server.exception.UnauthorizedException;
import com.sachith.one_server.model.AppUser;
import com.sachith.one_server.model.RefreshToken;
import com.sachith.one_server.repository.RefreshTokenRepository;
import com.sachith.one_server.repository.UserRepository;
import com.sachith.one_server.security.JwtUtil;
import com.sachith.one_server.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
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

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> register(@RequestBody RegisterRequest request) {

        if(userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body(BaseResponse.error("Username is already taken"));
        }

        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.addRole("USER");

        userRepository.save(user);

        return ResponseEntity.ok(BaseResponse.ok("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

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

        // 🔐 Set refresh token as HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from(
                        "refreshToken",
                        refreshToken.getToken()
                )
                .httpOnly(true)
                .secure(false) // true in production (HTTPS)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                        .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new TokenResponse(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(value = "refreshToken", required = false) String token,
            HttpServletResponse response
    ) {
        if(token == null) {
            throw new UnauthorizedException("No refresh token found");
        }

        refreshTokenService.deleteByToken(token);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok("Logged out");
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(
            @CookieValue("refreshToken") String refreshTokenValue) {

        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(refreshTokenValue);
        AppUser user = refreshToken.getAppUser();

        //issue authorities not mention here
        String newAccessToken =
                jwtUtil.generateToken(
                        user.getUsername(),
                        user.getAuthorities()
                );
        return ResponseEntity.ok(new TokenResponse(newAccessToken));
    }
}

