package negocio;

import dominio.Usuario;
import java.util.List;

public interface UsuarioNegocio {
    List<Usuario> listarConFiltros(String nombreUsuario, String rol, String estado);
    
    void bajaUsuario(int idUsuario, int idUsuarioLogueado) throws Exception;
    
    boolean modificarUsuario(int idUsuario, String tipoUser, String pass, String passRepetida) throws Exception;

    Usuario obtenerPorId(int idUsuario);

    boolean altaUsuario(String tipoUser, String username, String pass, String passRepetida, Usuario usuarioLogueado) throws Exception;

}
