package com.sachith.one_server.controller;

import com.sachith.one_server.dto.BaseResponse;
import com.sachith.one_server.dto.LoginRequest;
import com.sachith.one_server.dto.LoginResponse;
import com.sachith.one_server.dto.RegisterRequest;
import com.sachith.one_server.model.AppUser;
import com.sachith.one_server.repository.UserRepository;
import com.sachith.one_server.security.JwtUtil;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

//        This line triggers Spring Security's authentication process by verifying the provided username and password.
//        If the credentials are valid, it returns an Authentication object representing the authenticated user.
//        if not valid, it throws an exception.

        // Authenticate the user, the process begins here.
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String token = jwtUtil.generateToken(
                auth.getName(),
                auth.getAuthorities()
        );

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if(userRepository.existsByUsername(request.username())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}

