package com.example.nexo.usuario.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.empleado.service.EmpleadoService;
import com.example.nexo.usuario.model.Usuario;


/**
 * Guarda los usuarios (cuentas de acceso) en una colección en memoria.
 * No hay base de datos: la lista vive mientras la aplicación está en ejecución.
 * Las tres cuentas iniciales son las de la versión 1; sus contraseñas son
 * hashes BCrypt de una contraseña de demostración pública, nunca datos reales.
 *
 * Reglas del servidor (ETAPA 2):
 * - la contraseña sólo se almacena como hash BCrypt y jamás se muestra;
 * - el nombre de usuario es único ignorando mayúsculas y espacios sobrantes;
 * - el rol y el estado sólo aceptan valores del catálogo;
 * - una cuenta «Inactivo» queda deshabilitada y no puede iniciar sesión;
 * - la cuenta se vincula a un empleado que todavía no tenga cuenta.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    /** Contraseña de demostración de las cuentas semilla (no es un dato personal). */
    public static final String CONTRASENA_DEMO = "NexoDemo2026";

    private static final List<String> ROLES =
            List.of("Administrador", "Operador / Vendedor", "Empleado");

    private final List<Usuario> usuarios = new ArrayList<>();
    private final PasswordEncoder passwordEncoder;
    private final EmpleadoService empleadoService;

    public UsuarioServiceImpl(PasswordEncoder passwordEncoder, EmpleadoService empleadoService) {
        this.passwordEncoder = passwordEncoder;
        this.empleadoService = empleadoService;
        // Datos de partida tomados de la versión 1 (cuentas de acceso)
        String demo = passwordEncoder.encode(CONTRASENA_DEMO);
        usuarios.add(new Usuario(1, 1, "atorres", demo, "Operador / Vendedor", "Activo"));
        usuarios.add(new Usuario(2, 2, "lrojas", demo, "Operador / Vendedor", "Activo"));
        usuarios.add(new Usuario(3, 3, "ediaz", demo, "Administrador", "Activo"));
    }

    public List<Usuario> listaUsuarios() {
        return usuarios;
    }

    public String crearUsuario(Usuario usuario) {
        String error = validar(usuario, null);
        if (error != null) {
            return error;
        }
        usuario.setId(siguienteId());
        usuario.setUsername(normalizar(usuario.getUsername()));
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarios.add(usuario);
        return null;
    }

    public Usuario buscarUsuarioPorId(Integer id) {
        if (id == null) {
            return null;
        }
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        // Id inexistente: se devuelve null y el controller regresa a la lista
        return null;
    }

    public Usuario buscarUsuarioPorUsername(String username) {
        String normalizado = normalizar(username);
        if (normalizado.isEmpty()) {
            return null;
        }
        for (Usuario usuario : usuarios) {
            if (normalizado.equals(normalizar(usuario.getUsername()))) {
                return usuario;
            }
        }
        return null;
    }

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
        usuarios.set(usuarios.indexOf(actual), usuario);
        return null;
    }

    /**
     * Valida el formulario en el servidor. «idExcepcion» es la cuenta que se
     * está editando (null al crear): la propia cuenta no cuenta como duplicado.
     */
    private String validar(Usuario usuario, Integer idExcepcion) {
        String username = normalizar(usuario.getUsername());
        if (!username.matches("[a-z0-9._-]{4,20}")) {
            return "El nombre de usuario debe tener de 4 a 20 caracteres: "
                    + "minúsculas, números, punto, guion o guion bajo.";
        }
        for (Usuario existente : usuarios) {
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
            for (Usuario existente : usuarios) {
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
    private Integer siguienteId() {
        int maximo = 0;
        for (Usuario usuario : usuarios) {
            if (usuario.getId() != null && usuario.getId() > maximo) {
                maximo = usuario.getId();
            }
        }
        return maximo + 1;
    }

}
