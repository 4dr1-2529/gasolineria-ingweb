package com.example.nexo.auth.config;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.transaction.annotation.Transactional;
import com.example.nexo.usuario.repository.UsuarioRepository;
import java.time.*;
import com.example.nexo.shared.config.TiempoConfig;
@Component
public class RegistroAcceso {
    private final UsuarioRepository usuarios;private final Clock reloj;
    public RegistroAcceso(UsuarioRepository usuarios,Clock reloj){this.usuarios=usuarios;this.reloj=reloj.withZone(TiempoConfig.LIMA);}
    @EventListener @Transactional
    public void registrar(AuthenticationSuccessEvent evento) {
        usuarios.findByUsername(evento.getAuthentication().getName()).ifPresent(u->u.setUltimoAcceso(LocalDateTime.now(reloj)));
    }
}
