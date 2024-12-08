package com.rggt.edutectno.bootcamp.demojwt.auth;

import lombok.*;

/**
 * Clase que representa la solicitud de inicio de sesión.
 * Contiene los datos necesarios para autenticar a un usuario:
 * nombre de usuario (username) y contraseña (password).
 *
 * Utiliza anotaciones de Lombok para reducir el código boilerplate,
 * como constructores, métodos `getter`, y el patrón Builder.
 */
@Builder
@AllArgsConstructor // Genera un constructor con todos los argumentos.
@NoArgsConstructor  // Genera un constructor vacío.
public class LoginRequest {

    // Atributos que representan las credenciales de inicio de sesión.
    String username; // Nombre de usuario.
    String password; // Contraseña.

    /**
     * Obtiene el nombre de usuario proporcionado en la solicitud.
     *
     * @return Nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtiene la contraseña proporcionada en la solicitud.
     *
     * @return Contraseña.
     */
    public String getPassword() {
        return password;
    }
}
