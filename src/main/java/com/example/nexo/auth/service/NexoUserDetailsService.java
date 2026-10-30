package com.example.nexo.auth.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.service.UsuarioService;
import com.example.nexo.empleado.service.EmpleadoService;


/**
 * Conecta los usuarios persistidos con Spring Security (F01).
 * El nombre de usuario se busca normalizado (sin mayúsculas ni espacios
 * sobrantes) y una cuenta con estado «Inactivo» queda deshabilitada:
 * el usuario no puede iniciar sesión aunque recuerde su contraseña.
 */
@Service
public class NexoUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;
    private final EmpleadoService empleadoService;

    public NexoUserDetailsService(UsuarioService usuarioService, EmpleadoService empleadoService) {
        this.usuarioService = usuarioService;
        this.empleadoService = empleadoService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarUsuarioPorUsername(username);
        if (usuario == null) {
            // Spring Security oculta este detalle y responde «credenciales incorrectas»
            throw new UsernameNotFoundException("Usuario inexistente");
        }
        Empleado empleado = empleadoService.buscarEmpleadoPorId(usuario.getIdEmpleado());
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword() == null ? "" : usuario.getPassword())
                .authorities(rolSeguridad(usuario.getRol()))
                .disabled(!"Activo".equals(usuario.getEstado()) || empleado == null
                        || !"Activo".equals(empleado.getEstado()) || !"Activo".equals(usuario.getPerfil().getEstado())
                        || !java.util.Set.of("Administrador", "Operador", "Vendedor")
                                .contains(String.valueOf(usuario.getRol())))
                .build();
    }

    /** Traduce el rol del catálogo al prefijo ROLE_ que usa Spring Security. */
    private String[] rolSeguridad(String rol) {
        if ("Administrador".equals(rol)) {
            return new String[] { "ROLE_ADMIN" };
        }
        if ("Operador".equals(rol)) {
            return new String[] { "ROLE_OPERADOR" };
        }
        if ("Vendedor".equals(rol)) {
            return new String[] { "ROLE_VENDEDOR" };
        }
        return new String[0];
    }
}
