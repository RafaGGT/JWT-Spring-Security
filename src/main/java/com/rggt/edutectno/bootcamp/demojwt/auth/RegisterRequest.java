package com.rggt.edutectno.bootcamp.demojwt.auth;

import lombok.*;

/**
 * Clase que representa una solicitud de registro de usuario.
 * Contiene los datos necesarios para crear un nuevo usuario en el sistema.
 *
 * Utiliza anotaciones de Lombok para reducir el código boilerplate, como
 * constructores, getters, setters y el método `toString`.
 */
@Builder
@NoArgsConstructor  // Genera un constructor vacío.
@AllArgsConstructor // Genera un constructor con todos los argumentos.
@Data               // Genera getters, setters, equals, hashCode y toString.
public class RegisterRequest {

    // Atributos que representan los datos necesarios para registrar un usuario.
    String username;   // Nombre de usuario único.
    String password;   // Contraseña del usuario.
    String firstname;  // Primer nombre del usuario.
    String lastname;   // Apellido del usuario.
    String country;    // País de residencia del usuario.
}
