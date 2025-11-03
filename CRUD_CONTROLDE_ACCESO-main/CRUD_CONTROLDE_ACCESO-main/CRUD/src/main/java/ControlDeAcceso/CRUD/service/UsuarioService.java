package ControlDeAcceso.CRUD.service;

import ControlDeAcceso.CRUD.model.Usuario;
import ControlDeAcceso.CRUD.repository.UsuarioRepository;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Usuario autenticarUsuario(String dni, String password) {
        return usuarioRepository.buscarPorDniYPassword(dni, password);
    }
}
