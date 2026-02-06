package com.sachith.one_server.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    //check
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    // check
    private Instant expiryDate;

    // getters & setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public AppUser getAppUser() { return user; }
    public void setAppUser(AppUser user) { this.user = user; }

    public Instant getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Instant expiryDate) { this.expiryDate = expiryDate; }
}

