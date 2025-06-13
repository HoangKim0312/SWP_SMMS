package com.example.swp_smms.security;

import com.example.swp_smms.model.entity.AccessToken;
import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.RefreshToken;
import com.example.swp_smms.model.exception.SmmsException;
import com.example.swp_smms.repository.AccessTokenRepository;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final AccountRepository accountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(JwtTokenProvider.class);
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private long jwtAccessExpiration;
    
    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new SmmsException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException e) {
            throw new SmmsException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException e) {
            throw new SmmsException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new SmmsException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }
    }

    public String generateAccessToken(Authentication authentication) {
        String token = generateToken(authentication, jwtAccessExpiration);
        return token;
    }

    public String generateRefreshToken(Authentication authentication) {
        String token = generateToken(authentication, jwtRefreshExpiration);
        return token;
    }

    public String generateToken(Authentication authentication, long expiration) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expiration);
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new SmmsException(HttpStatus.INTERNAL_SERVER_ERROR, "Account not found"));

        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        String token = Jwts.builder()
                .setHeader(header)
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .claim("id", account.getAccountId().toString())
                .claim("role", authentication.getAuthorities().toArray()[0].toString())
                .claim("fullName", account.getFullName())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public String getUsernameFromJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        String username = claims.getSubject();
        return username;
    }

    public boolean isTokenValid(String jwt, String username) {
        String tokenUsername = getUsernameFromJwt(jwt);
        return tokenUsername.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void deleteExpiredOrRevokedTokens() {
        logger.info("Detecting deleting expired tokens task started");
        // TODO: Implement token cleanup logic when repository methods are available
        logger.info("Detecting deleting expired tokens task finished");
    }
} 