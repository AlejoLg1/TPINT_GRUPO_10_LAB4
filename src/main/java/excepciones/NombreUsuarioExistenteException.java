package excepciones;

public class NombreUsuarioExistenteException extends Exception {
	private static final long serialVersionUID = 1L;

	public NombreUsuarioExistenteException(String mensaje) {
        super(mensaje);
    }

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return "El nombre de usuario ya existe";
	}
	
	
}
