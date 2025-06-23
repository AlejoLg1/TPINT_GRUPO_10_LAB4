package excepciones;

public class UsuarioNoEncontradoException extends AutenticacionException {
	private static final long serialVersionUID = 1L;

	public UsuarioNoEncontradoException() {
        super("Usuario o contraseña incorrectos.");
    }
}
