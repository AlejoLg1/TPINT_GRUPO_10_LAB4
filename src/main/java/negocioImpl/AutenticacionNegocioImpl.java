package negocioImpl;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;
import excepciones.AutenticacionException;
import excepciones.UsuarioNoEncontradoException;
import negocio.AutenticacionNegocio;

public class AutenticacionNegocioImpl implements AutenticacionNegocio {
    
    private UsuarioDao usuarioDao = new UsuarioDaoImpl();

    @Override
    public Usuario autenticar(String nombreUsuario, String clave) throws AutenticacionException {
        Usuario usuario = usuarioDao.obtenerPorCredenciales(nombreUsuario, clave);

        if (usuario == null) {
            throw new UsuarioNoEncontradoException();
        }

        if (!usuario.isEstado()) {
            throw new AutenticacionException("El usuario no está habilitado para ingresar.");
        }

        return usuario;
    }
}
