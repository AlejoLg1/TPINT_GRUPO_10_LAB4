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
    
    @Override
    public void bajaUsuario(int idUsuario, int idUsuarioLogueado) throws Exception {
        UsuarioDao dao = new UsuarioDaoImpl();

        Usuario usuarioADesactivar = dao.obtenerPorId(idUsuario);

        if (usuarioADesactivar.isAdmin() && dao.contarAdminsActivos() == 1) {
            throw new Exception("No se puede desactivar al último administrador activo.");
        }

        if (idUsuario == idUsuarioLogueado) {
            throw new Exception("No podés desactivarte a vos mismo. Contactá a otro administrador para realizar la desactivación.");
        }

        usuarioADesactivar.setEstado(false);
        dao.Eliminar(usuarioADesactivar);
    }

}
