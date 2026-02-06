package com.sachith.one_server.controller;

import com.sachith.one_server.model.AppUser;
import com.sachith.one_server.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/one/v1/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{id}/role")
    public void assignRole(
            @PathVariable Long id,
            @RequestParam String role
    ) {
        AppUser user = userRepository.findById(id)
                .orElseThrow();

        user.addRole(role);
        userRepository.save(user);
    }
}
