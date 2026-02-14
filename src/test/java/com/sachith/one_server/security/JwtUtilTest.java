package com.sachith.one_server.security;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // Generate a valid Base64 secret for testing (same algorithm your app uses)
        String testSecret = Encoders.BASE64.encode(
                Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded());
        jwtUtil = new JwtUtil(testSecret);
    }

    // ── 1. Token generation returns a non-null, non-blank string ────────
    @Test
    void generateToken_shouldReturnNonBlankJwt() {
        String token = jwtUtil.generateToken("sachith", roles("ROLE_USER"));

        assertNotNull(token);
        assertFalse(token.isBlank());
        // JWT always has 3 dot-separated parts: header.payload.signature
        assertEquals(3, token.split("\\.").length,
                "JWT must have exactly 3 parts separated by dots");
    }

    // ── 2. Username survives a round-trip (generate → extract) ──────────
    @Test
    void extractUsername_shouldReturnOriginalUsername() {
        String token = jwtUtil.generateToken("sachith", roles("ROLE_ADMIN"));

        String extracted = jwtUtil.extractUsername(token);

        assertEquals("sachith", extracted);
    }

    // ── 3. Roles survive a round-trip ───────────────────────────────────
    @Test
    void extractRoles_shouldReturnAllAssignedRoles() {
        Collection<GrantedAuthority> authorities = roles("ROLE_USER", "ROLE_ADMIN");

        String token = jwtUtil.generateToken("sachith", authorities);
        List<String> extractedRoles = jwtUtil.extractRoles(token);

        assertEquals(2, extractedRoles.size());
        assertTrue(extractedRoles.contains("ROLE_USER"));
        assertTrue(extractedRoles.contains("ROLE_ADMIN"));
    }

    // ── 4. A tampered token must be rejected ────────────────────────────
    @Test
    void extractUsername_shouldThrowForTamperedToken() {
        String token = jwtUtil.generateToken("sachith", roles("ROLE_USER"));

        // Flip a character in the signature (last part) to simulate tampering
        String tampered = token.substring(0, token.length() - 4) + "XXXX";

        assertThrows(Exception.class, () -> jwtUtil.extractUsername(tampered),
                "Tampered token must be rejected");
    }

    // ── 5. A token signed with a DIFFERENT key must be rejected ─────────
    @Test
    void extractUsername_shouldThrowForTokenSignedWithDifferentKey() {
        // Create a SECOND JwtUtil with a different secret
        String otherSecret = Encoders.BASE64.encode(
                Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded());
        JwtUtil otherJwtUtil = new JwtUtil(otherSecret);

        String tokenFromOther = otherJwtUtil.generateToken("hacker", roles("ROLE_ADMIN"));

        assertThrows(Exception.class, () -> jwtUtil.extractUsername(tokenFromOther),
                "Token signed with a different key must be rejected");
    }

    // ── helper ──────────────────────────────────────────────────────────
    private List<GrantedAuthority> roles(String... roleNames) {
        return java.util.Arrays.stream(roleNames)
                .map(SimpleGrantedAuthority::new)
                .map(a -> (GrantedAuthority) a)
                .toList();
    }
}
