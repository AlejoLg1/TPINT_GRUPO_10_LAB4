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
    
    @Override
    public boolean modificarUsuario(int idUsuario, String tipoUser, String pass, String passRepetida) throws Exception {
        UsuarioDao dao = new UsuarioDaoImpl();

        if (idUsuario <= 0) throw new IllegalArgumentException("ID de usuario inválido.");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setTipo("BANCARIO");

        String modo = "";

        if (tipoUser != null) {
            usuario.setIsAdmin(tipoUser.equals("Admin"));
            modo = "tipo";
        }

        if (pass != null && !pass.isEmpty()) {
            if (!pass.equals(passRepetida)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden.");
            }
            usuario.setClave(pass);
            modo = modo.equals("tipo") ? "ambos" : "clave";
        }

        if (modo.isEmpty()) throw new IllegalArgumentException("No se enviaron cambios válidos.");

        return dao.Modificar(usuario, modo);
    }

    @Override
    public Usuario obtenerPorId(int idUsuario) {
        UsuarioDao dao = new UsuarioDaoImpl();
        return dao.obtenerPorId(idUsuario);
    }

}
