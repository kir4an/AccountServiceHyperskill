package account.Utils;

import account.model.HrRequest;
import account.model.InternRequest;
import account.model.UserRoleType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class JwtUtils {
    @Value("${jwt.secret.access}")
    private String accessSecret;
    @Value("${jwt.secret.refresh}")
    private String refreshSecret;

    @Value("${myapp.jwtLifeTime}")
    private Duration jwtLifeTime;

    public String generateJwtToken(String email){
        List<String> rolesList = new ArrayList<>();
        rolesList.add(UserRoleType.ROLE_INTERN.name());
        Map<String, Object> claims = new HashMap<>();
        //claims.put("name",request.getName());
       // claims.put("lastname",request.getLastname());
        claims.put("email",email);
        //claims.put("Resume",request.getResume());
        claims.put("roles",rolesList);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setSubject(email)
                .setExpiration(Date.from(Instant.now().plus(jwtLifeTime)))
                .signWith(SignatureAlgorithm.HS256, accessSecret)
                .compact();

    }
    public String generateJwtByHR(HrRequest hrManagerRequest){
        List<String> rolesList = new ArrayList<>();
        rolesList.add(UserRoleType.ROLE_HR.name());
        Map<String, Object> claims = new HashMap<>();
        claims.put("name",hrManagerRequest.getName());
        claims.put("lastname",hrManagerRequest.getLastname());
        claims.put("email",hrManagerRequest.getEmail());
        claims.put("secretKey",hrManagerRequest.getSecretKey());
        claims.put("roles",rolesList);


        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setSubject(hrManagerRequest.getEmail())
                .setExpiration(Date.from(Instant.now().plus(jwtLifeTime)))
                .signWith(SignatureAlgorithm.HS256, accessSecret)
                .compact();
    }
    public String generateRefreshToken(String email){
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now.plusDays(30).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS256,refreshSecret)
                .compact();
    }
    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }
    public String getUsernameRefreshToken(String token){
        String email = Jwts.parser()
                .setSigningKey(refreshSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return email;
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessSecret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
