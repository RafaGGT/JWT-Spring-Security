package com.rggt.edutectno.bootcamp.demojwt.auth;

// Importaciones necesarias para manejar dependencias, solicitudes HTTP y controladores REST.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para manejar operaciones de autenticación y registro.
 * Expone los endpoints relacionados con la autenticación de usuarios.
 */
@RestController
@RequestMapping("/auth") // Define la ruta base para los endpoints de este controlador.
public class AuthController {

    /**
     * Inyección del servicio de autenticación que contiene la lógica para el login y registro.
     */
    @Autowired
    private AuthService authService;

    /**
     * Endpoint para iniciar sesión en la aplicación.
     *
     * @param request Objeto de tipo LoginRequest que contiene las credenciales del usuario.
     * @return ResponseEntity<AuthResponse> con la respuesta del proceso de login,
     * incluyendo el token JWT si las credenciales son válidas.
     */
    @PostMapping(value = "/login") // Define la ruta y el método HTTP para este endpoint.
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // Delegación de la lógica al servicio de autenticación y retorno de la respuesta.
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Endpoint para registrar un nuevo usuario en la aplicación.
     *
     * @param request Objeto de tipo RegisterRequest que contiene los datos necesarios para registrar al usuario.
     * @return ResponseEntity<AuthResponse> con la respuesta del proceso de registro,
     * incluyendo el token JWT si el registro fue exitoso.
     */
    @PostMapping(value = "/register") // Define la ruta y el método HTTP para este endpoint.
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Delegación de la lógica al servicio de autenticación y retorno de la respuesta.
        return ResponseEntity.ok(authService.register(request));
    }
}
