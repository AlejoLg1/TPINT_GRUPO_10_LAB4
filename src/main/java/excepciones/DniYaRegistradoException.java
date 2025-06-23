package excepciones;

public class DniYaRegistradoException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public  DniYaRegistradoException(String mensaje){
	   super(mensaje);
	}

	@Override
	public String getMessage() {

		return "El DNI ya se encuntra registrado";
	}
	
	

}
