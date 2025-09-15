package com.example.exercise.demo.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    // private static final Integer EXPIRE_TIME = 1000 * 60 * 24;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expire-time}")
    private String expireTime;

    public String generateToken(Integer userId, String username, Collection<? extends GrantedAuthority> authorities) {

        // 放入自訂資訊
        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userId); // add userId to claims

        claims.put("roles", authorities.stream()
                .map(GrantedAuthority::getAuthority) // add roles to claims
                .toList());

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Integer.parseInt(expireTime)))
                .and()
                .signWith(getKey())
                .compact();

    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        // 每次 request 驗證 token 的 username 與是否過期
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject); // claim -> claim.getSubject()
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        // Function 接收 Claims 物件並 return T
        final Claims claims = Jwts.parser()
                                    .verifyWith(getKey()) // 用金鑰驗證 token
                                    .build()
                                    .parseSignedClaims(token)
                                    .getPayload();
        return claimResolver.apply(claims);
    }

    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date()); // claim -> claim.getExpiration()
    }

}
