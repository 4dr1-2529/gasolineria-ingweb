package com.example.nexo.auth.config;

import java.io.IOException;
import java.util.Set;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.nexo.empleado.service.EmpleadoService;
import com.example.nexo.usuario.service.UsuarioService;

/** Revalida cuentas desactivadas y cambios de rol en sesiones existentes. */
public class CuentaVigenteFilter extends OncePerRequestFilter {
    private final UsuarioService usuarios;
    private final EmpleadoService empleados;

    public CuentaVigenteFilter(UsuarioService usuarios, EmpleadoService empleados) {
        this.usuarios = usuarios;
        this.empleados = empleados;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            var usuario = usuarios.buscarUsuarioPorUsername(auth.getName());
            var empleado = usuario == null ? null : empleados.buscarEmpleadoPorId(usuario.getIdEmpleado());
            String autoridad = usuario == null ? "" : switch (String.valueOf(usuario.getRol())) {
                case "Administrador" -> "ROLE_ADMIN";
                case "Operador" -> "ROLE_OPERADOR";
                case "Vendedor" -> "ROLE_VENDEDOR";
                default -> "";
            };
            boolean rolVigente = !autoridad.isEmpty() && auth.getAuthorities().stream()
                    .map(a -> a.getAuthority()).collect(java.util.stream.Collectors.toSet())
                    .equals(Set.of(autoridad));
            if (usuario == null || !"Activo".equals(usuario.getEstado()) || empleado == null
                    || !"Activo".equals(empleado.getEstado()) || !rolVigente || !"Activo".equals(usuario.getPerfil().getEstado())) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
                response.sendRedirect(request.getContextPath() + "/login?error=inactivo");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
