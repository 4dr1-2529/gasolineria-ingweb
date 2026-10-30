package com.example.nexo.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.repository.UsuarioRepository;

@Service
public class ActorActual {
    private final UsuarioRepository usuarios;
    public ActorActual(UsuarioRepository usuarios) { this.usuarios=usuarios; }
    public Usuario usuario() {
        var auth=SecurityContextHolder.getContext().getAuthentication();
        Usuario u=auth==null?null:usuarios.findByUsername(auth.getName()).orElse(null);
        if(u==null || !"Activo".equals(u.getEstado()) || !"Activo".equals(u.getEmpleado().getEstado())
                || !"Activo".equals(u.getPerfil().getEstado())) throw new AccessDeniedException("La cuenta no está activa.");
        String autoridad=switch(u.getPerfil().getCodigo()) {
            case "ADMINISTRADOR"->"ROLE_ADMIN";case "OPERADOR"->"ROLE_OPERADOR";case "VENDEDOR"->"ROLE_VENDEDOR";default->"";
        };
        if(autoridad.isEmpty() || auth.getAuthorities().stream().noneMatch(a->autoridad.equals(a.getAuthority())))
            throw new AccessDeniedException("El rol de la sesión dejó de estar vigente.");
        return u;
    }
    public boolean estaVigente(){usuario();return true;}
}
