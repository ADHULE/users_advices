package BlackAdhuleSystem.dev.userAdvicesMariadb.security;

import BlackAdhuleSystem.dev.userAdvicesMariadb.services.implementations.UserServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *  Filtre JWT appliqué à chaque requête HTTP :
 * - Extrait le token du header Authorization
 * - Valide le token et charge l’utilisateur
 * - Ajoute l’utilisateur au contexte de sécurité Spring
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceImp userServiceImp;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        // Vérifie la présence d’un token Bearer
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(token);
            } catch (Exception e) {
                // Token invalide ou mal formé
                logger.warn("Échec d'extraction du token : {}");
            }
        }

        //  Si le token est valide et qu’aucune authentification n’est déjà active
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (!jwtService.isTokenExpired(token)) {
                UserDetails userDetails = userServiceImp.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //  Continue la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}
