package negocio;

import dominio.Usuario;
import java.util.List;

public interface UsuarioNegocio {
    List<Usuario> listarConFiltros(String nombreUsuario, String rol, String estado);
}
