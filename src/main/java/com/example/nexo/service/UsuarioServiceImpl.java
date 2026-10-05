package com.example.nexo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.nexo.model.Usuario;

/**
 * Guarda los usuarios (cuentas de acceso) en una colección en memoria.
 * No hay base de datos: la lista vive mientras la aplicación está en ejecución.
 * Los datos iniciales son las tres cuentas de la versión 1.
 *
 * La contraseña no se guarda: la versión 1 indica que nunca se muestra ni se
 * almacena en texto plano, y el hash real llegará con la futura implementación.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final List<Usuario> usuarios = new ArrayList<>();

    public UsuarioServiceImpl() {
        // Datos de partida tomados de la versión 1 (cuentas de acceso)
        usuarios.add(new Usuario(1, 1, "atorres", null, "Operador / Vendedor", "Activo"));
        usuarios.add(new Usuario(2, 2, "lrojas", null, "Operador / Vendedor", "Activo"));
        usuarios.add(new Usuario(3, 3, "ediaz", null, "Administrador", "Activo"));
    }

    public List<Usuario> listaUsuarios() {
        return usuarios;
    }

    public void crearUsuario(Usuario usuario) {
        usuario.setId(siguienteId());
        usuario.setPassword(null); // La contraseña no se almacena en memoria
        usuarios.add(usuario);
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

    public void editarUsuario(Usuario usuario) {
        if (usuario.getId() == null) {
            return;
        }
        usuario.setPassword(null); // La contraseña no se almacena en memoria
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(usuario.getId())) {
                usuarios.set(i, usuario);
                return;
            }
        }
        // Id inexistente: no se modifica nada
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
