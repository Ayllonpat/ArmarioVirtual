package com.trianasalesianos.dam.ArmarioVirtual.security.jwt.acceso.service;

import com.trianasalesianos.dam.ArmarioVirtual.config.JwtProperties;
import com.trianasalesianos.dam.ArmarioVirtual.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String TOKEN_TYPE = "JWT";
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final JwtProperties jwtProperties;

    private JwtParser jwtParser;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public String generateAccessToken(Usuario user) {
        Date tokenExpirationDate =
                Date.from(
                        LocalDateTime
                                .now()
                                .plusMinutes(jwtProperties.getDuration())
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                );

        return Jwts.builder()
                .header().type(TOKEN_TYPE)
                .and()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(tokenExpirationDate)
                .signWith(secretKey)
                .compact();
    }

    public UUID getUserIdFromAccessToken(String token) {
        String sub = jwtParser.parseClaimsJws(token).getBody().getSubject();
        return UUID.fromString(sub);
    }

    public boolean validateAccessToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch(SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new JwtException(ex.getMessage());
        }
    }
}
