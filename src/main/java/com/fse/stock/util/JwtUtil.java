package com.fse.stock.util;

import com.fse.stock.model.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class JwtUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${authentication.app.jwtSecret}")
    private String jwtSecret;

    public User getUserDetailsFromJwtToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        LOGGER.info("jwtClaims: [{}]" + claims);
        return new User(claims.getSubject(), (String) claims.get("roles"));
    }

    public boolean validateJwtToken(String authToken) {
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
        return true;
    }

    public String extractJWT(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader;
        }
        return null;
    }
}

