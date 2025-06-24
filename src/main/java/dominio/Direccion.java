package dominio;

public class Direccion {
	
	private int id;
	private String calle;
	private String numero;
	private String localidad;
	private String provincia;
	
	public Direccion() {
	}
	
	public Direccion(int id, String calle, String numero, String localidad, String provincia) {
		super();
		this.id = id;
		this.calle = calle;
		this.numero = numero;
		this.localidad = localidad;
		this.provincia = provincia;
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
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getDirCompleta()
	{
		return this.calle + ", " +this.numero;
	}
	
}
