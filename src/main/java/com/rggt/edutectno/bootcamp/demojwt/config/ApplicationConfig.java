package com.rggt.edutectno.bootcamp.demojwt.config;

import com.rggt.edutectno.bootcamp.demojwt.userdto.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Clase de configuración para la seguridad de la aplicación.
 * Define beans necesarios para la autenticación y encriptación de contraseñas.
 */
@Configuration // Marca esta clase como una fuente de beans para el contexto de Spring.
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Constructor que inyecta el repositorio de usuarios.
     *
     * @param userRepository Repositorio que interactúa con los datos de los usuarios.
     */
    @Autowired
    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Define un bean para gestionar la autenticación en la aplicación.
     *
     * @param config Configuración de autenticación de Spring Security.
     * @return Un objeto `AuthenticationManager` configurado.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Define un proveedor de autenticación que utiliza un `UserDetailsService` y un codificador de contraseñas.
     *
     * @return Un objeto `AuthenticationProvider` configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService()); // Servicio para cargar detalles del usuario.
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Codificador de contraseñas.
        return authenticationProvider;
    }

    /**
     * Define un bean para encriptar contraseñas usando BCrypt.
     *
     * @return Un objeto `PasswordEncoder` configurado para usar BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define un servicio para cargar detalles de usuarios basándose en el nombre de usuario.
     *
     * @return Un objeto `UserDetailsService` que interactúa con el repositorio de usuarios.
     */
    @Bean
    public UserDetailsService userDetailService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
