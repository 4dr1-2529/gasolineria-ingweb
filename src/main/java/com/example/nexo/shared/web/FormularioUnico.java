package com.example.nexo.shared.web;

import jakarta.servlet.http.HttpSession;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

/** Evita repetir un formulario operativo dentro de la misma sesión. No sustituye las claves únicas SQL. */
public final class FormularioUnico {
    private static final String CLAVE = FormularioUnico.class.getName();
    private FormularioUnico() { }

    @SuppressWarnings("unchecked")
    private static Set<String> tokens(HttpSession sesion) {
        Object actuales = sesion.getAttribute(CLAVE);
        if (actuales == null) {
            actuales = new LinkedHashSet<String>();
            sesion.setAttribute(CLAVE, actuales);
        }
        return (Set<String>) actuales;
    }

    public static String emitir(HttpSession sesion, String operacion) {
        synchronized (sesion) {
            Set<String> tokens = tokens(sesion);
            if (tokens.size() >= 32) tokens.remove(tokens.iterator().next());
            String token = operacion + ":" + UUID.randomUUID();
            tokens.add(token);
            return token;
        }
    }

    public static String ejecutar(HttpSession sesion, String operacion, String token,
            Supplier<String> accion) {
        synchronized (sesion) {
            if (token == null || !token.startsWith(operacion + ":") || !tokens(sesion).contains(token)) {
                return "El formulario ya fue procesado o caducó. Abre un formulario nuevo.";
            }
            String error = accion.get();
            if (error == null) tokens(sesion).remove(token);
            return error;
        }
    }
}
