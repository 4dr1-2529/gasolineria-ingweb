package com.example.nexo.usuario.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.empleado.service.EmpleadoService;
import com.example.nexo.usuario.model.Usuario;


/** Servicio persistente: conserva validación, baja lógica e historial. */
@Service
@org.springframework.transaction.annotation.Transactional
public class UsuarioServiceImpl implements UsuarioService {

    /** Contraseña de demostración de las cuentas semilla (no es un dato personal). */
    public static final String CONTRASENA_DEMO = System.getenv("NEXO_DEMO_PASSWORD");

    private static final List<String> ROLES =
            List.of("Administrador", "Operador", "Vendedor");

    private final com.example.nexo.usuario.repository.UsuarioRepository repo;
    private final com.example.nexo.usuario.repository.RolRepository roles;
    private final PasswordEncoder passwordEncoder;
    private final EmpleadoService empleadoService;



    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, EmpleadoService empleadoService, com.example.nexo.usuario.repository.UsuarioRepository repo, com.example.nexo.usuario.repository.RolRepository roles) {
        this.repo=repo;
        this.passwordEncoder = passwordEncoder;
        this.empleadoService = empleadoService;
        this.roles=roles;
    }

    public List<Usuario> listaUsuarios() {
        return repo.findAll();
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String crearUsuario(Usuario usuario) {
        String error = validar(usuario, null);
        if (error != null) {
            return error;
        }
        usuario.setId(null);
        usuario.setUsername(normalizar(usuario.getUsername()));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEmpleado(empleadoService.buscarEmpleadoPorId(usuario.getIdEmpleado()));
        usuario.setPerfil(roles.findByNombre(usuario.getRol()).orElseThrow());
        repo.save(usuario);
        return null;
    }

    public Usuario buscarUsuarioPorId(Long id) {return id==null?null:repo.findById(id).orElse(null);}

    public Usuario buscarUsuarioPorUsername(String username) {String nombre=normalizar(username);return nombre.isBlank()?null:repo.findByUsername(nombre).orElse(null);}

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN') && @actorActual.estaVigente()")
    public String editarUsuario(Usuario usuario) {
        Usuario actual = buscarUsuarioPorId(usuario.getId());
        if (actual == null) {
            return "La cuenta solicitada no existe.";
        }
        String error = validar(usuario, actual.getId());
        if (error != null) {
            return error;
        }
        usuario.setUsername(normalizar(usuario.getUsername()));
        usuario.setRol(usuario.getRol().trim());
        usuario.setEstado(usuario.getEstado().trim());
        // Si el formulario no envía contraseña, se conserva el hash actual
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            usuario.setPassword(actual.getPassword());
        } else {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        // El formulario de edición no cambia el empleado de la cuenta
        usuario.setIdEmpleado(actual.getIdEmpleado());
        usuario.setEmpleado(actual.getEmpleado());
        usuario.setPerfil(roles.findByNombre(usuario.getRol()).orElseThrow());
        usuario.setFechaCreacion(actual.getFechaCreacion());
        usuario.setUltimoAcceso(actual.getUltimoAcceso());
        repo.save(usuario);
        return null;
    }

    /**
     * Valida el formulario en el servidor. «idExcepcion» es la cuenta que se
     * está editando (null al crear): la propia cuenta no cuenta como duplicado.
     */
    private String validar(Usuario usuario, Long idExcepcion) {
        String username = normalizar(usuario.getUsername());
        if (!username.matches("[a-z0-9._-]{4,20}")) {
            return "El nombre de usuario debe tener de 4 a 20 caracteres: "
                    + "minúsculas, números, punto, guion o guion bajo.";
        }
        for (Usuario existente : repo.findAll()) {
            boolean mismaCuenta = existente.getId().equals(idExcepcion);
            if (!mismaCuenta && username.equals(normalizar(existente.getUsername()))) {
                return "El nombre de usuario «" + username + "» ya está registrado.";
            }
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            if (idExcepcion == null) {
                return "La contraseña es obligatoria.";
            }
            // Al editar puede conservarse la contraseña actual
        } else if (usuario.getPassword().length() < 8 || usuario.getPassword().length() > 40) {
            return "La contraseña debe tener entre 8 y 40 caracteres.";
        }
        if (usuario.getRol() == null || !ROLES.contains(usuario.getRol().trim())) {
            return "El rol seleccionado no es válido.";
        }
        var perfil=roles.findByNombre(usuario.getRol().trim()).orElse(null);
        if(perfil==null || !"Activo".equals(perfil.getEstado())) return "El rol no está disponible.";
        if (usuario.getEstado() == null
                || (!"Activo".equals(usuario.getEstado().trim())
                        && !"Inactivo".equals(usuario.getEstado().trim()))) {
            return "El estado debe ser Activo o Inactivo.";
        }
        if (idExcepcion == null) {
            // La cuenta nueva pertenece a un empleado que todavía no tenga cuenta
            if (usuario.getIdEmpleado() == null) {
                return "Debe seleccionar el empleado dueño de la cuenta.";
            }
            if (empleadoService.buscarEmpleadoPorId(usuario.getIdEmpleado()) == null) {
                return "El empleado seleccionado no existe.";
            }
            if (!"Activo".equals(empleadoService.buscarEmpleadoPorId(usuario.getIdEmpleado()).getEstado())) {
                return "La cuenta nueva debe pertenecer a un empleado activo.";
            }
            for (Usuario existente : repo.findAll()) {
                if (usuario.getIdEmpleado().equals(existente.getIdEmpleado())) {
                    return "Ese empleado ya tiene una cuenta de usuario.";
                }
            }
        }
        return null;
    }

    /** Normaliza para comparar y almacenar: sin espacios sobrantes y en minúsculas. */
    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * El id_usuario es entero en el modelo de la versión 1,
     * así que se conserva la numeración correlativa 1, 2, 3 → 4.
     */


}
