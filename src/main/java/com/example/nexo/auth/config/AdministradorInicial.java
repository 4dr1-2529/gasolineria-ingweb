package com.example.nexo.auth.config;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.*;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.empleado.repository.EmpleadoRepository;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.repository.*;
import static com.example.nexo.shared.validation.ReglaNegocioException.exigir;

/** Aprovisionamiento explícito del primer administrador; nunca restablece una cuenta existente. */
@Component @Profile("!demo")
public class AdministradorInicial implements ApplicationRunner {
    private final Environment env;private final UsuarioRepository usuarios;private final EmpleadoRepository empleados;private final RolRepository roles;private final PasswordEncoder encoder;
    public AdministradorInicial(Environment env,UsuarioRepository usuarios,EmpleadoRepository empleados,RolRepository roles,PasswordEncoder encoder){this.env=env;this.usuarios=usuarios;this.empleados=empleados;this.roles=roles;this.encoder=encoder;}
    @Override @Transactional
    public void run(ApplicationArguments argumentos){
        String password=env.getProperty("NEXO_ADMIN_PASSWORD","");if(password.isBlank() || usuarios.count()>0)return;
        String dni=env.getProperty("NEXO_ADMIN_DNI","");String nombres=env.getProperty("NEXO_ADMIN_NOMBRES","");String apellidos=env.getProperty("NEXO_ADMIN_APELLIDOS","");String telefono=env.getProperty("NEXO_ADMIN_TELEFONO","");String username=env.getProperty("NEXO_ADMIN_USERNAME","admin");
        exigir(password.length()>=8 && password.length()<=40,"NEXO_ADMIN_PASSWORD debe tener entre 8 y 40 caracteres.");
        exigir(dni.matches("[0-9]{8}") && telefono.matches("[0-9]{3} [0-9]{3} [0-9]{3}"),"Configura NEXO_ADMIN_DNI (8 dígitos) y NEXO_ADMIN_TELEFONO (000 000 000).");
        exigir(nombres.length()>=3 && nombres.length()<=60 && apellidos.length()>=3 && apellidos.length()<=60,"Configura los nombres y apellidos del administrador (3 a 60 caracteres).");
        exigir(username.matches("[a-z0-9._-]{4,20}"),"NEXO_ADMIN_USERNAME inválido.");
        Empleado e=empleados.findAll().stream().filter(p->dni.equals(p.getDni())).findFirst().orElse(null);
        if(e==null)e=empleados.saveAndFlush(new Empleado(null,dni,nombres,apellidos,"Administrador",telefono,"Activo"));
        exigir("Activo".equals(e.getEstado()),"El empleado del administrador inicial está inactivo.");
        Usuario u=new Usuario();u.setEmpleado(e);u.setPerfil(roles.findByCodigo("ADMINISTRADOR").orElseThrow());u.setUsername(username);u.setPassword(encoder.encode(password));u.setEstado("Activo");usuarios.saveAndFlush(u);
    }
}
