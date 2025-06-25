package negocio;

import dominio.Usuario;
import excepciones.AutenticacionException;

public interface AutenticacionNegocio {
    Usuario autenticar(String nombreUsuario, String clave) throws AutenticacionException;
    boolean validarRolAdmin(Usuario usuario);
}
