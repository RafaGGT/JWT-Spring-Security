package com.rggt.edutectno.bootcamp.demojwt.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * Controlador de demostración de la API.
 * Esta clase expone un endpoint seguro que solo puede ser accedido por usuarios autenticados.
 */
@RestController // Indica que esta clase es un controlador REST que maneja solicitudes HTTP.
@RequestMapping("/api/v1") // Define la ruta base para todas las solicitudes de este controlador.
@RequiredArgsConstructor // Genera un constructor con todos los campos finales no inicializados automáticamente (como dependencias de otros componentes).
public class DemoController {

    /**
     * Endpoint seguro que responde con un mensaje de bienvenida.
     * Este endpoint solo estará disponible para usuarios autenticados debido a la configuración de seguridad en la aplicación.
     *
     * @return Mensaje de bienvenida.
     */
    @PostMapping(value = "demo") // Mapea las solicitudes HTTP POST en la ruta "/api/v1/demo".
    public String welcome() {
        return "Welcome from secure endpoint"; // Respuesta simple que se devuelve cuando se accede a este endpoint.
    }
}
