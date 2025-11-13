package BlackAdhuleSystem.dev.userAdvicesMariadb.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;

/**
 * ⚙️ Configuration principale de Spring Security :
 * - Définit les routes publiques (login, inscription, activation)
 * - Active l’authentification JWT sans session
 * - Intègre le filtre JwtFilter
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ApplicationSecurityConfig {

    private final JwtFilter jwtFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive CSRF car on utilise des JWT (API stateless)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Routes publiques
                        .requestMatchers(POST, "/inscription", "/activation", "/login").permitAll()
                        // Toute autre route nécessite un token JWT valide
                        .anyRequest().authenticated()
                )
                // Pas de session côté serveur : tout est dans le JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // On ajoute notre filtre JWT avant celui d’authentification
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** ⚙️ Fournit l’AuthenticationManager à Spring Security */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /** ⚙️ Fournit un AuthenticationProvider utilisant UserDetailsService + BCrypt */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }
}
