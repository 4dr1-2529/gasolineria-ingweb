package com.example.nexo.auth.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.service.UsuarioService;


/**
 * Conecta los usuarios en memoria con Spring Security (F01).
 * El nombre de usuario se busca normalizado (sin mayúsculas ni espacios
 * sobrantes) y una cuenta con estado «Inactivo» queda deshabilitada:
 * el usuario no puede iniciar sesión aunque recuerde su contraseña.
 */
@Service
public class NexoUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public NexoUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarUsuarioPorUsername(username);
        if (usuario == null) {
            // Spring Security oculta este detalle y responde «credenciales incorrectas»
            throw new UsernameNotFoundException("Usuario inexistente");
        }
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword() == null ? "" : usuario.getPassword())
                .authorities(rolSeguridad(usuario.getRol()))
                .disabled("Inactivo".equals(usuario.getEstado()))
                .build();
    }

    /** Traduce el rol del catálogo al prefijo ROLE_ que usa Spring Security. */
    private String[] rolSeguridad(String rol) {
        if ("Administrador".equals(rol)) {
            return new String[] { "ROLE_ADMIN" };
        }
        if ("Empleado".equals(rol)) {
            return new String[] { "ROLE_EMPLEADO" };
        }
        return new String[] { "ROLE_OPERADOR" }; // «Operador / Vendedor»
    }
}
