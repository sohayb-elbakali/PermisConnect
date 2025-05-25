package com.perm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import com.perm.models.user.Utilisateur;
import com.perm.models.user.Admin;
import com.perm.models.user.Moniteur;
import com.perm.models.user.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey jwtSecret;
    private final long jwtExpirationInMs;

    public JwtTokenProvider(
            @Value("${app.jwt.expiration:86400000}") long jwtExpirationInMs) {
        this.jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public String generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = userDetails.getUtilisateur();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(utilisateur.getEmail())
                .claim("roles", authorities)
                .claim("nom", utilisateur.getNom())
                .claim("prenom", utilisateur.getPrenom())
                .claim("telephone", utilisateur.getTelephone())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(jwtSecret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        // Create a concrete implementation based on the role
        Utilisateur utilisateur;
        String role = authorities.iterator().next().getAuthority();
        switch (role) {
            case "ROLE_ADMIN":
                utilisateur = new Admin();
                break;
            case "ROLE_MONITEUR":
                utilisateur = new Moniteur();
                break;
            case "ROLE_CLIENT":
                utilisateur = new Client();
                break;
            default:
                throw new IllegalStateException("Unexpected role: " + role);
        }

        // Set common properties
        utilisateur.setEmail(claims.getSubject());
        utilisateur.setNom((String) claims.get("nom"));
        utilisateur.setPrenom((String) claims.get("prenom"));
        utilisateur.setTelephone((String) claims.get("telephone"));

        CustomUserDetails principal = new CustomUserDetails(utilisateur);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
