package com.rggt.edutectno.bootcamp.demojwt.auth;

import com.rggt.edutectno.bootcamp.demojwt.jwt.JwtService;
import com.rggt.edutectno.bootcamp.demojwt.userdto.Role;
import com.rggt.edutectno.bootcamp.demojwt.userdto.User;
import com.rggt.edutectno.bootcamp.demojwt.userdto.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio que gestiona las operaciones de autenticación y registro de usuarios.
 * Incluye la lógica para autenticar credenciales, registrar nuevos usuarios
 * y generar tokens JWT.
 */
@Service
public class AuthService {

    // Repositorio para gestionar usuarios en la base de datos.
    private final UserRepository userRepository;

    // Servicio para la generación y validación de tokens JWT.
    private final JwtService jwtService;

    // Codificador de contraseñas para almacenar contraseñas de forma segura.
    private final PasswordEncoder passwordEncoder;

    // Administrador de autenticaciones para validar credenciales.
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor del servicio que inicializa las dependencias necesarias.
     *
     * @param userRepository      Repositorio de usuarios.
     * @param jwtService          Servicio para la generación de tokens JWT.
     * @param passwordEncoder     Codificador de contraseñas.
     * @param authenticationManager Administrador de autenticación.
     */
    public AuthService(UserRepository userRepository, JwtService jwtService,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Autentica a un usuario y genera un token JWT si las credenciales son válidas.
     *
     * @param request Objeto que contiene el nombre de usuario y contraseña proporcionados por el cliente.
     * @return Respuesta de autenticación con el token generado.
     */
    public AuthResponse login(LoginRequest request) {
        // Autentica las credenciales usando AuthenticationManager.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Recupera los detalles del usuario desde la base de datos.
        UserDetails user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Genera un token JWT para el usuario autenticado.
        String token = jwtService.getToken(user);

        // Devuelve la respuesta de autenticación con el token generado.
        return new AuthResponse.Builder()
                .token(token)
                .build();
    }

    /**
     * Registra un nuevo usuario, codifica su contraseña y genera un token JWT.
     *
     * @param request Objeto que contiene los datos del nuevo usuario.
     * @return Respuesta de autenticación con el token generado.
     */
    public AuthResponse register(RegisterRequest request) {
        // Crea una nueva instancia de User utilizando el patrón Builder.
        User user = new User.Builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // Codifica la contraseña.
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(Role.USER) // Asigna el rol de usuario predeterminado.
                .build();

        // Guarda el usuario en el repositorio.
        userRepository.save(user);

        // Genera un token JWT para el nuevo usuario registrado.
        return new AuthResponse.Builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
