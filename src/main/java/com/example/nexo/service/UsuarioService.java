package com.example.nexo.service;

import java.util.List;

import com.example.nexo.model.Usuario;

public interface UsuarioService {

    public List<Usuario> listaUsuarios();

    public void crearUsuario(Usuario usuario);

    public Usuario buscarUsuarioPorId(Integer id);

    public void editarUsuario(Usuario usuario);

}
