package negocioImpl;

import dao.UsuarioDao;
import dao.ClienteDao; 
import daoImpl.UsuarioDaoImpl;
import daoImpl.ClienteDaoImpl;
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
    
    public boolean validarRolAdmin(Usuario usuario) {
        return usuario != null && usuario.isAdmin();
    }
    
    public int obtenerIdClientePorUsuario(int idUsuario) {
        ClienteDao clienteDao = new ClienteDaoImpl();
        return clienteDao.obtenerIdClientePorIdUsuario(idUsuario);
    }

}
