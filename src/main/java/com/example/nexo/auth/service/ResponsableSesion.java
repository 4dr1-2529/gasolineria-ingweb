package com.example.nexo.auth.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.service.UsuarioService;

public final class ResponsableSesion {
    private ResponsableSesion() { }

    public static Long id(Authentication autenticacion, UsuarioService usuarios) {
        Usuario usuario = autenticacion == null ? null
                : usuarios.buscarUsuarioPorUsername(autenticacion.getName());
        if (usuario == null || !"Activo".equals(usuario.getEstado())) {
            throw new AccessDeniedException("La cuenta de la sesión no está disponible.");
        }
        return usuario.getId();
    }
}
