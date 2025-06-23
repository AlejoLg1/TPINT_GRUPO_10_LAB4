package excepciones;

public class ContrasenasNoCoincidenException extends Exception {
	private static final long serialVersionUID = 1L;

	public ContrasenasNoCoincidenException(String mensaje) {
        super(mensaje);
    }
}
