package com.fproject.FProject.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author javier
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configura los filtros de seguridad HTTP. - Permite acceso sin
     * autenticación a ciertos recursos como CSS, JS y la página de portfolio. -
     * Requiere autenticación para cualquier otra solicitud. - Configura una
     * página de login personalizada y permite el logout sin autenticación.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //Desactivamos la verificacion de sesion con las coockies
                .cors()
                .and()
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                // Permite el acceso público a los recursos estáticos y a las páginas publicas
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/auth/**", "/").permitAll()
                // Requiere autenticación para cualquier otra petición
                .anyRequest().authenticated())
                .logout((logout) -> logout.permitAll()) // Permite el logout sin autenticación
                        // Añadir el filtro de autenticación JWT y lo posicionamos antes de UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Permite el logout sin autenticación

        return http.build(); // Devuelve la configuración de seguridad construida

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Configuramos el algoritmo de encriptacion por la implementacion de BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
