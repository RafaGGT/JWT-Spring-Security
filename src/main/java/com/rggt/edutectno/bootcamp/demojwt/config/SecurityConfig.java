package com.rggt.edutectno.bootcamp.demojwt.config;

import com.rggt.edutectno.bootcamp.demojwt.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Clase de configuración de seguridad de la aplicación.
 * Configura las políticas de autenticación, autorización y seguridad HTTP en la aplicación.
 */
@Configuration // Indica que esta clase contiene configuración para Spring.
@EnableWebSecurity // Habilita la configuración de seguridad en la aplicación.
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    /**
     * Constructor para inyectar el filtro de autenticación JWT y el proveedor de autenticación.
     *
     * @param jwtAuthenticationFilter Filtro personalizado para la autenticación con JWT.
     * @param authProvider Proveedor de autenticación que maneja el proceso de autenticación.
     */
    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authProvider = authProvider;
    }

    /**
     * Configura los filtros y la seguridad HTTP de la aplicación.
     *
     * @param http Configuración HTTP para la seguridad de la aplicación.
     * @return La cadena de filtros de seguridad configurados.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desactiva la protección CSRF, no necesaria en este caso porque usamos JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Configura la autorización de las solicitudes HTTP
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/auth/**").permitAll() // Permite acceso sin autenticación a las rutas que comienzan con /auth/
                                .anyRequest().authenticated() // Requiere autenticación para cualquier otra ruta
                )

                // Configura la política de gestión de sesiones a Stateless
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No se guardan sesiones del lado del servidor
                )

                // Establece el proveedor de autenticación configurado
                .authenticationProvider(authProvider)

                // Añade el filtro de autenticación JWT antes del filtro de autenticación por nombre de usuario y contraseña
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}
