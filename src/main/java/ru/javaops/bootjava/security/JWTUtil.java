package ru.javaops.bootjava.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt-secret-key}")
    private String secretKey;

    public String generateToken(String email) {
//        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
        Date expirationDate = Date.from(ZonedDateTime.now().plusYears(1).toInstant()); // для тестирования
        return JWT.create()
                .withSubject("Restaurant-voting")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String validateTokenAndGetEmail(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                .withSubject("Restaurant-voting")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}
