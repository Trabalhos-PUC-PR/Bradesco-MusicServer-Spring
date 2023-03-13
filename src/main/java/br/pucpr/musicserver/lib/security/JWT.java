package br.pucpr.musicserver.lib.security;

import br.pucpr.musicserver.rest.users.User;
import br.pucpr.musicserver.rest.users.resonses.RoleResponse;
import br.pucpr.musicserver.rest.users.resonses.UserLoginResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JWT {
    private SecuritySettings settings;
    private final String prefix = "Bearer";

    public JWT(SecuritySettings secretSetting) {
        this.settings = secretSetting;
    }

    public Authentication extract(HttpServletRequest req){
        final var header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || !header.startsWith(prefix)) return null;

        final var token = header.replace(prefix, "").trim();
        final var claims = Jwts.parserBuilder()
                .setSigningKey(settings.getSecret().getBytes())
                .deserializeJsonWith(new JacksonDeserializer<>(Map.of("user", User.class)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        if(!settings.getIssuer().equals(claims.getIssuer())) return null;
        final var user = claims.get("user", User.class);
        if(user == null) return null;

        final var authorities = user.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_"+r))
                .toList();
        return UsernamePasswordAuthenticationToken.authenticated(user, user.getId(), authorities);
    }

    public static Date toDate(LocalDate date){
        return Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant());
    }

    public String createToken(String token){
        if(!settings.isTestUserAllowed()) return null;
        if(!Objects.equals(token, settings.getToken())) return null;

        final var now = LocalDate.now();
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(settings.getSecret().getBytes()))
                .serializeToJsonWith(new JacksonSerializer<>())
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(now.plusDays(2)))
                .setIssuer(settings.getIssuer())
                .setSubject(settings.getTestUser().getId().toString())
                .addClaims(Map.of("user", settings.getTestUser()))
                .compact();
    }

    public UserLoginResponse createTestUser(String token){
        if(!settings.isTestUserAllowed()) return null;
        if(!Objects.equals(token, settings.getToken())) return null;

        final var testUser = settings.getTestUser();

        final var now = LocalDate.now();
        final var jwtToken = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(settings.getSecret().getBytes()))
                .serializeToJsonWith(new JacksonSerializer<>())
                .setIssuedAt(toDate(now))
                .setExpiration(toDate(now.plusDays(2)))
                .setIssuer(settings.getIssuer())
                .setSubject(settings.getTestUser().getId().toString())
                .addClaims(Map.of("user", settings.getTestUser()))
                .compact();

        return new UserLoginResponse(
                jwtToken,
                testUser.getId(),
                "Test_User",
                "Test_User",
                testUser.getRoles().stream()
                        .map(RoleResponse::new)
                        .collect(Collectors.toSet())
        );
    }

}
