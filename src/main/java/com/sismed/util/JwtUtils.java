package com.sismed.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    @Value("${backend.app.jwt.secret}")
    private String secret;
    @Value("${backend.app.jwt.tokenExpirationTime}")
    private int tokenExpirationTime;

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        final Date currentDate = new Date();
        final Date tokenExpirationDate = new Date((currentDate).getTime() + this.tokenExpirationTime);

        return Jwts.builder()
                .setSubject((principal.getUsername()))
                .setIssuedAt(currentDate)
                .setExpiration(tokenExpirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
        } catch (MalformedJwtException e) {
        } catch (ExpiredJwtException e) {
        } catch (UnsupportedJwtException e) {
        } catch (IllegalArgumentException e) {
        }

        return false;
    }

}
