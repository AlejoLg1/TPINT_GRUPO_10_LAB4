package excepciones;

public class ClienteNoVinculadoException extends Exception{
	private static final long serialVersionUID = 1L;

	public ClienteNoVinculadoException(String mensaje) {
        super(mensaje);
    }
}
