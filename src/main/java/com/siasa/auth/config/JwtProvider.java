package com.siasa.auth.config;

import com.siasa.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecretEncoder;

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(jwtSecretEncoder);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(User user) {
        Map<String, Object> claims = Jwts.claims().setSubject(user.getName());
        claims.put("id", user.getId());
        claims.put("udec", "Facatativá");
        Date now = new Date();
        Date exp = new Date(now.getTime() + 12 * 3600 * 1000);
        return Jwts.builder()
                .setHeaderParam("company_name", "SIASA Software")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getSignKey(), SignatureAlgorithm.HS512).compact();
    }

    public void validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parse(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public String getUsernameFromToken(String token) {

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parse(token)
                    .getBody()
                    .toString();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token ");
        }
    }
}
