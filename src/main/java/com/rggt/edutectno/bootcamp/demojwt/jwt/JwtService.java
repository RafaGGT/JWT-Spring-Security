package com.rggt.edutectno.bootcamp.demojwt.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Clave secreta utilizada para firmar el token JWT
    private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

    /**
     * Genera un token JWT para el usuario proporcionado.
     * El token incluye el nombre de usuario como sujeto y tiene una validez de 24 horas.
     *
     * @param user El usuario para el cual se generará el token.
     * @return El token JWT generado.
     */
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user); // Llama al método privado con un mapa vacío de reclamos adicionales.
    }

    /**
     * Genera un token JWT para el usuario proporcionado con reclamos adicionales.
     *
     * @param extraClaims Mapa que contiene reclamos adicionales a agregar al token.
     * @param user El usuario para el cual se generará el token.
     * @return El token JWT generado.
     */
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)  // Establece los reclamos adicionales (si los hay).
                .setSubject(user.getUsername())  // Establece el nombre de usuario como sujeto del token.
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Establece la fecha de emisión del token.
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))  // Establece la fecha de expiración a 24 horas desde la emisión.
                .signWith(getKey(), SignatureAlgorithm.HS256)  // Firma el token con la clave secreta utilizando el algoritmo HS256.
                .compact();  // Compone el token final y lo devuelve.
    }

    /**
     * Obtiene la clave secreta usada para firmar el token.
     * La clave secreta es decodificada desde su representación en base64.
     *
     * @return La clave secreta utilizada para la firma del token.
     */
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);  // Decodifica la clave secreta desde base64.
        return Keys.hmacShaKeyFor(keyBytes);  // Devuelve la clave en formato HMAC adecuada para firmar el token.
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     *
     * @param token El token JWT.
     * @return El nombre de usuario extraído del token.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);  // Extrae el nombre de usuario (sujeto) del token JWT.
    }

    /**
     * Valida si el token es válido comparando el nombre de usuario con el del usuario proporcionado
     * y verificando si el token ha expirado.
     *
     * @param token El token JWT.
     * @param userDetails Los detalles del usuario con los cuales se compara el nombre de usuario.
     * @return `true` si el token es válido, `false` de lo contrario.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);  // Extrae el nombre de usuario del token.
        // Valida si el nombre de usuario coincide y si el token no ha expirado.
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Obtiene todos los reclamos del token JWT.
     * Los reclamos contienen la información asociada al token como el nombre de usuario, fecha de expiración, etc.
     *
     * @param token El token JWT.
     * @return Los reclamos extraídos del token.
     */
    private Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())  // Configura la clave secreta para verificar la firma del token.
                .build()
                .parseClaimsJws(token)  // Parsea el token JWT.
                .getBody();  // Obtiene los reclamos (el cuerpo del token).
    }

    /**
     * Extrae un reclamo específico del token JWT utilizando una función que resuelve el reclamo solicitado.
     *
     * @param token El token JWT.
     * @param claimsResolver Función que permite obtener un reclamo específico.
     * @param <T> El tipo de valor que se espera como resultado del reclamo.
     * @return El valor del reclamo extraído.
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);  // Obtiene todos los reclamos del token.
        return claimsResolver.apply(claims);  // Aplica la función para obtener el reclamo específico.
    }

    /**
     * Obtiene la fecha de expiración del token JWT.
     *
     * @param token El token JWT.
     * @return La fecha de expiración del token.
     */
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);  // Extrae la fecha de expiración de los reclamos.
    }

    /**
     * Verifica si el token JWT ha expirado comparando la fecha de expiración con la fecha actual.
     *
     * @param token El token JWT.
     * @return `true` si el token ha expirado, `false` de lo contrario.
     */
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());  // Compara la fecha de expiración con la fecha actual.
    }
}
