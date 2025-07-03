package negocioImpl;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;
import negocio.UsuarioNegocio;

import java.util.List;

public class UsuarioNegocioImpl implements UsuarioNegocio {
    UsuarioDao usuarioDao = new UsuarioDaoImpl();

    @Override
    public List<Usuario> listarConFiltros(String nombreUsuario, String rol, String estado) {
        return usuarioDao.listarConFiltros(nombreUsuario, rol, estado);
    }
}
