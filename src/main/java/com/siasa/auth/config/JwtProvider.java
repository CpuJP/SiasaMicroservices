package com.siasa.auth.config;

import com.siasa.auth.entity.Usuario;
import com.siasa.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.*;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecretEncoder;

    private final UserRepository userRepository;

    public JwtProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(jwtSecretEncoder);
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public String createToken(User user) {
//        // Crear los claims del token
//        Map<String, Object> claims = Jwts.claims().setSubject(user.getName());
//        claims.put("roles", )
//
//        // Definir la fecha de emisión y expiración del token
//        Date now = new Date();
//        Date exp = new Date(now.getTime() + 12 * 3600 * 1000); // 12 horas de expiración
//
//        // Construir el token JWT
//        return Jwts.builder()
//                .setHeaderParam("company_name", "SIASA Software")
//                .setHeaderParam("udec", "Facatativá")
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(exp)
//                .signWith(getSignKey(), SignatureAlgorithm.HS512)
//                .compact();
//    }

    public String createToken(String userName) {
        Usuario user = userRepository.findByName(userName)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Utiliza el método findByIdWithRoles para cargar los roles del usuario
        Usuario userWithRoles = userRepository.findByIdWithRoles(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User with roles not found"));

        Map<String, Object> claims = Jwts.claims().setSubject(userWithRoles.getName());
        claims.put("roles", userWithRoles.getRoles()); // Agregar roles al token

        Date now = new Date();
        Date exp = new Date(now.getTime() + 12 * 3600 * 1000);

        return Jwts.builder()
                .setHeaderParam("company_name", "SIASA Software")
                .setHeaderParam("udec", "Facatativá")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith( getSignKey(), SignatureAlgorithm.HS512)
                .compact();
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

    public List<String> getRolesFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Obtener los roles del claim 'roles' del token
            Object rolesObject = claims.get("roles");

            // Comprobar si el claim 'roles' está presente y es una lista
            if (rolesObject instanceof List) {
                return (List<String>) rolesObject;
            } else {
                return Collections.emptyList(); // o lanza una excepción si es necesario
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }
}
