package com.labtech.backend.security.util;

import com.labtech.backend.constants.EncurtadorConstants;
import com.labtech.backend.entity.User;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final Environment env;


    public String  generateToken(Authentication authentication){
        String jwtToken;
        String secret = env.getProperty(EncurtadorConstants.JWT_SECRET_KEY
                , EncurtadorConstants.JWT_SECRET_DEFAULT_VALUE);
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var fetched_user = (User) authentication.getPrincipal();
        jwtToken = Jwts.builder().issuer("Encurtador").subject(fetched_user.getEmail())
                .claim("name",fetched_user.getName())
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date((new java.util.Date()).getTime() + 24 * 60 * 60 * 1000))
                .signWith(secretKey).compact();
        return jwtToken;
    }
}
