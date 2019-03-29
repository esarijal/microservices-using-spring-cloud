package com.mitrais.microservices.netflixzuulapigatewayserver.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtTokenService implements Clock, ITokenService {
    private static final String DOT = ".";
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    String issuer;
    int expirationSec;
    int clockSkewSec;
    String secretKey;

    public JwtTokenService(@Value("${jwt.issuer:mitrais}") String issuer,
                           @Value("${jwt.expiration-sec:86400}") int expirationSec,
                           @Value("${jwt.clock-skew-sec:300}") int clockSkewSec,
                           @Value("${jwt.secret:secret}") String secretKey) {
        super();
        this.issuer = requireNonNull(issuer);
        this.expirationSec = requireNonNull(expirationSec);
        this.clockSkewSec = requireNonNull(clockSkewSec);
        this.secretKey = BASE64.encode(requireNonNull(secretKey));
    }

    private String newToken(final Map<String, String> attributes, final int expiresInSec){
        final LocalDateTime now = LocalDateTime.now();
        final Claims claims = Jwts
                .claims()
                .setIssuer(issuer)
                .setIssuedAt(java.sql.Timestamp.valueOf(now));

        if(expiresInSec > 0){
            final LocalDateTime expiresAt = now.plusSeconds(expiresInSec);
            claims.setExpiration(java.sql.Timestamp.valueOf(expiresAt));
        }
        claims.putAll(attributes);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    @Override
    public String permanent(Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    @Override
    public String expiring(Map<String, String> attributes) {
        return newToken(attributes, expirationSec);
    }

    private static Map<String, String> parseClaims(final Supplier<Claims> toClaims){
        try{
            final Claims claims = toClaims.get();
            final Map<String, String> map = new HashMap<>();
            for(final Map.Entry<String, Object> e: claims.entrySet()){
                map.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return Collections.unmodifiableMap(map);
        } catch (IllegalArgumentException | JwtException e){
            return Collections.unmodifiableMap(new HashMap<>());
        }
    }

    @Override
    public Map<String, String> untrusted(String token) {
        final JwtParser jwtParser = Jwts.parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec);

        // see: https://github.com/jwtk/jjwt/issues/135
        final String withoutSignature = StringUtils.substringBeforeLast(token, DOT) + DOT;
        return parseClaims(() -> jwtParser.parseClaimsJwt(withoutSignature).getBody());
    }

    @Override
    public Map<String, String> verify(String token) {
        final JwtParser jwtParser = Jwts.parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey);
        return parseClaims(() -> jwtParser.parseClaimsJws(token).getBody());
    }

    @Override
    public Date now() {
        return java.sql.Timestamp.valueOf(LocalDateTime.now());
    }
}
