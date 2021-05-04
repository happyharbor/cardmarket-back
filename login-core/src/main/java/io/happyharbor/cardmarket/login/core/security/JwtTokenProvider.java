package io.happyharbor.cardmarket.login.core.security;

import io.happyharbor.cardmarket.login.api.enums.Role;
import io.happyharbor.cardmarket.login.api.exception.InvalidOrExpiredJwtTokenException;
import io.happyharbor.cardmarket.login.core.property.JwtTokenProperties;
import io.happyharbor.cardmarket.login.core.service.ApplicationUserDetails;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

  /**
   * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
   * microservices environment, this key would be kept on a config-server.
   */
  private final JwtTokenProperties jwtTokenProperties;

  private final String secretKey;

  private final ApplicationUserDetails applicationUserDetails;

  public JwtTokenProvider(final JwtTokenProperties jwtTokenProperties, final ApplicationUserDetails applicationUserDetails) {
    this.jwtTokenProperties = jwtTokenProperties;
    this.applicationUserDetails = applicationUserDetails;
    this.secretKey = jwtTokenProperties.getSecretKey();
  }

  public String createToken(String username, Set<Role> roles) {

    val claims = Jwts.claims().setSubject(username);
    claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).collect(Collectors.toList()));

    val now = LocalDateTime.now();
    val validity = now.plusSeconds(jwtTokenProperties.getExpireLength());

    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    Key key = Keys.hmacShaKeyFor(keyBytes);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(java.sql.Timestamp.valueOf(now))
        .setExpiration(Timestamp.valueOf(validity))
        .signWith(key, SignatureAlgorithm.HS256)//
        .compact();
  }

  public Authentication getAuthentication(String token) {
    val userDetails = applicationUserDetails.loadUserByUsername(getUsername(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsername(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      throw new InvalidOrExpiredJwtTokenException();
    }
  }
}
