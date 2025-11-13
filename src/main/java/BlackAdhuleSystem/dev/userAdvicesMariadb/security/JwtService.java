package BlackAdhuleSystem.dev.userAdvicesMariadb.security;

import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.User;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.implementations.UserServiceImp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 *  Service de gestion des tokens JWT :
 * - Génère un token signé avec une clé secrète HMAC
 * - Extrait les informations (email, expiration)
 * - Vérifie la validité du token
 * - Compatible avec JJWT 0.13.0
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final UserServiceImp userServiceImp;

    @Value("${jwt.secret:213d43ec60b50d2f096b02842e15f8340a5152153a19370c7dd16e15f00c9634}")
    private String encryptionKey;

    private SecretKey secretKey;

    /**
     * Initialise la clé secrète une fois au démarrage
     */
    @PostConstruct
    public void initKey() {
        byte[] keyBytes = Base64.getDecoder().decode(encryptionKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     *  Génère un JWT à partir du nom d’utilisateur
     */
    public Map<String, String> jwtGenerate(String username) {
        User user =(User) userServiceImp.findEntityByEmail(username);
        return generateJwt(user);
    }

    /**
     *  Construit et signe le JWT avec les claims utilisateur
     */
    private Map<String, String> generateJwt(User user) {
        long now = System.currentTimeMillis();
        long expiration = now + 30 * 60 * 1000; // 30 minutes

        Map<String, Object> claims = Map.of(
                "name", user.getName(),
                "email", user.getEmail()
        );

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return Map.of(
                "Bearer", token,
                "expiresAt", new Date(expiration).toString()
        );
    }

    /**
     * Extrait l'email (subject) depuis le token
     */
    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Vérifie si le token est expiré
     */
    public boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     *  Extrait un claim spécifique via une fonction
     */
    private <T> T getClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(getAllClaims(token));
    }

    /**
     *  Extrait tous les claims du token signé
     */
    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
