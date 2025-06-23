package excepciones;

public class TodosLosCamposObligatorios extends Exception {
	private static final long serialVersionUID = 1L;

	public TodosLosCamposObligatorios(String mensaje) {
        super(mensaje);
    }
}
