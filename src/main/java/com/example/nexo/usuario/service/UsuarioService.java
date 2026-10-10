package com.example.nexo.usuario.service;

import java.util.List;
import com.example.nexo.usuario.model.Usuario;


public interface UsuarioService {

    public List<Usuario> listaUsuarios();

    /** Crea la cuenta: devuelve null si fue válida o el motivo del rechazo. */
    public String crearUsuario(Usuario usuario);

    public Usuario buscarUsuarioPorId(Integer id);

    /** Busca por nombre de usuario ignorando mayúsculas y espacios sobrantes. */
    public Usuario buscarUsuarioPorUsername(String username);

    /** Guarda los cambios: devuelve null si fue válido o el motivo del rechazo. */
    public String editarUsuario(Usuario usuario);

}
