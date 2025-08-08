package com.fproject.FProject.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author javier
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("JWT");

        if (token != null && !token.isBlank()) {
            try {

                if (!jwtProvider.validateToken(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    filterChain.doFilter(request, response);
                    return;
                }

                Claims claims = jwtProvider.getClaimsToken(token).getBody();

                // Extraer la información del usuario desde el token (por ejemplo, nombre, roles, etc.)
                String name = claims.getSubject();  // Método que extrae el nombre de usuario del token
                List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + (String) claims.get("role", String.class)));  // Método que extrae los roles

                // Crear un objeto de autenticación con el usuario y sus roles
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                        name, null, authorities);

                // Establecer el contexto de seguridad en Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;

            } catch (ExpiredJwtException | IllegalArgumentException e) {
                filterChain.doFilter(request, response);
                return;

            }

        }
        filterChain.doFilter(request, response);
    }
}