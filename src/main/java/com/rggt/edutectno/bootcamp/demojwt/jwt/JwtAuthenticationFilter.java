package com.rggt.edutectno.bootcamp.demojwt.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Filtro de autenticación JWT.
 * Este filtro intercepta todas las solicitudes HTTP para verificar si contienen un token JWT válido.
 * Si el token es válido, establece la autenticación del usuario en el contexto de seguridad de Spring.
 */
@Component // Indica que esta clase es un componente de Spring y será gestionada por el contenedor de Spring.
public class JwtAuthenticationFilter extends OncePerRequestFilter { // El filtro se ejecuta una sola vez por solicitud

    private final JwtService jwtService; // Servicio para manejar la creación, validación y extracción de información del token JWT
    private final UserDetailsService userDetailsService; // Servicio para cargar los detalles del usuario desde la base de datos

    /**
     * Constructor que inyecta las dependencias necesarias.
     *
     * @param jwtService Servicio para trabajar con tokens JWT.
     * @param userDetailsService Servicio para cargar detalles del usuario.
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método principal que filtra las solicitudes entrantes.
     * Extrae el token JWT de la solicitud, lo valida y establece la autenticación en el contexto de seguridad de Spring si es válido.
     *
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @param filterChain La cadena de filtros.
     * @throws ServletException Si ocurre un error durante la ejecución del filtro.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Si la solicitud es para los endpoints de autenticación (por ejemplo, /auth/login o /auth/register), no se filtra.
        if (request.getRequestURI().startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el token JWT de la cabecera de la solicitud
        final String token = getTokenFromRequest(request);
        final String username;

        // Si no hay token, se permite que la solicitud continúe
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el nombre de usuario del token
        username = jwtService.getUsernameFromToken(token);

        // Si el token es válido y el usuario no está autenticado en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carga los detalles del usuario desde la base de datos
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Si el token es válido, se crea un objeto de autenticación y se establece en el contexto de seguridad
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // Establece los detalles de la autenticación (como la IP o el agente de usuario)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establece el token de autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del encabezado Authorization de la solicitud HTTP.
     * El token debe comenzar con "Bearer ".
     *
     * @param request La solicitud HTTP.
     * @return El token JWT si existe, de lo contrario, null.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Si el encabezado de autorización está presente y comienza con "Bearer ", extrae el token
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Elimina el prefijo "Bearer "
        }
        return null; // No se encontró el token
    }
}
