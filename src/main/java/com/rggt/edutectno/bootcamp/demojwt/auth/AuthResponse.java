package com.rggt.edutectno.bootcamp.demojwt.auth;

/**
 * Clase que representa la respuesta de autenticación enviada al cliente.
 * Incluye el token JWT necesario para futuras solicitudes autenticadas.
 * Implementa el patrón Builder para una creación controlada y fluida de instancias.
 */
public class AuthResponse {

    /**
     * Token de autenticación (generalmente JWT) generado tras un inicio de sesión exitoso.
     */
    private String token;

    /**
     * Constructor privado para asegurar que las instancias de AuthResponse solo puedan ser
     * creadas a través del patrón Builder.
     *
     * @param builder Objeto de tipo Builder que contiene los datos necesarios para construir la instancia.
     */
    private AuthResponse(Builder builder) {
        this.token = builder.token;
    }

    /**
     * Obtiene el token de autenticación.
     *
     * @return Token JWT como una cadena de texto.
     */
    public String getToken() {
        return token;
    }

    /**
     * Clase interna estática que implementa el patrón Builder para crear instancias de AuthResponse.
     * Facilita una construcción fluida y segura del objeto.
     */
    public static class Builder {

        /** Token que será incluido en la respuesta. */
        private String token;

        /**
         * Método del Builder para establecer el token de autenticación.
         *
         * @param token Token JWT generado tras la autenticación.
         * @return Instancia del Builder con el valor de token establecido.
         */
        public Builder token(String token) {
            this.token = token;
            return this;
        }

        /**
         * Construye una instancia de AuthResponse utilizando los datos proporcionados al Builder.
         *
         * @return Instancia de AuthResponse con el token establecido.
         */
        public AuthResponse build() {
            return new AuthResponse(this);
        }
    }
}

