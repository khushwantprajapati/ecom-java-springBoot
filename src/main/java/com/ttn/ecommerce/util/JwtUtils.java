package com.ttn.ecommerce.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;


@Service
public class JwtUtils {

    @Value("${jwt.token.validity.minutes}")
    private Long expire;

    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String email, int expireInMinutes)
            throws IllegalArgumentException, JWTCreationException {

        Long currentTimeInMills = System.currentTimeMillis();
        currentTimeInMills += (long) expireInMinutes * 60 * 1000;
        Date expiry = new Date(currentTimeInMills);

        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(expiry)
                .withIssuer("BootCamp")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String email) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("BootCamp")
                .build();
        DecodedJWT jwt = verifier.verify(email);
        return jwt.getClaim("email").asString();

    }

    public String getTokenThroughRequest(HttpServletRequest request) throws JWTVerificationException {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean isTokenExpired(String token) throws JWTVerificationException {
        DecodedJWT jwt = JWT.decode(token);
        Date expiration = jwt.getExpiresAt();
        return expiration.before(new Date());
    }


}
