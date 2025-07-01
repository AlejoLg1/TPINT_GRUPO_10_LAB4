package dominio;

public class Direccion {
	
	private int id;
	private String calle;
	private String numero;
	
	public Direccion() {
	}
	
	public Direccion(int id, String calle, String numero) {
		super();
		this.id = id;
		this.calle = calle;
		this.numero = numero;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCalle() {
		return calle;
	}
	public void setCalle(String calle) {
		this.calle = calle;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getDirCompleta()
	{
		return this.calle + ", " +this.numero;
	}
	
}
