package excepciones;

public class MontoInsuficienteException extends Exception {

	private static final long serialVersionUID = 1L;

	public MontoInsuficienteException(String mensaje) {
		super(mensaje);
	}
	
}
