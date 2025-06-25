package dominio;

public class Cliente {

	    private int idCliente;
	    private Usuario usuario;
	    private int idUsuario;
	    private Direccion Direccion;
	    private String dni;
	    private String cuil;
	    private String nombre;
	    private String apellido;
	    private String sexo;
	    private String nacionalidad;
	    private java.time.LocalDate fechaNacimiento;
	    private String correo;
	    private String telefono;
	    private boolean estado;

	    // Constructores
	    public Cliente() {
	    	this.Direccion = new Direccion();
	    }

	    public Cliente(int idCliente, Usuario usuario, int idUsuario, Direccion direccion, String dni, String cuil,
	                   String nombre, String apellido, String sexo, String nacionalidad,
	                   java.time.LocalDate fechaNacimiento, String correo, String telefono, boolean estado) {
	        this.idCliente = idCliente;
	        this.usuario = usuario;
	        this.idUsuario = idUsuario;
	        this.Direccion = direccion;
	        this.dni = dni;
	        this.cuil = cuil;
	        this.nombre = nombre;
	        this.apellido = apellido;
	        this.sexo = sexo;
	        this.nacionalidad = nacionalidad;
	        this.fechaNacimiento = fechaNacimiento;
	        this.correo = correo;
	        this.telefono = telefono;
	        this.estado = estado;
	    }

	    // Getters y Setters

	    public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}

		public int getIdCliente() {
	        return idCliente;
	    }

	    public void setIdCliente(int idCliente) {
	        this.idCliente = idCliente;
	    }

	    public int getIdUsuario() {
	        return idUsuario;
	    }

	    public void setIdUsuario(int idUsuario) {
	        this.idUsuario = idUsuario;
	    }

	    public Direccion getDireccion() {
	        return this.Direccion;
	    }

	    public void setDireccion(Direccion direccion) {
	        this.Direccion = direccion;
	    }

	    public String getDni() {
	        return dni;
	    }

	    public void setDni(String dni) {
	        this.dni = dni;
	    }

	    public String getCuil() {
	        return cuil;
	    }

	    public void setCuil(String cuil) {
	        this.cuil = cuil;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public String getApellido() {
	        return apellido;
	    }

	    public void setApellido(String apellido) {
	        this.apellido = apellido;
	    }

	    public String getSexo() {
	        if(this.sexo.equalsIgnoreCase("M")) return "M";
	        else if(this.sexo.equalsIgnoreCase("F")) return "F";
	        else return "X";
	    }

	    public void setSexo(String sexo) {
	        this.sexo = sexo;
	    }

	    public String getNacionalidad() {
	        return nacionalidad;
	    }

	    public void setNacionalidad(String nacionalidad) {
	        this.nacionalidad = nacionalidad;
	    }

	    public java.time.LocalDate getFechaNacimiento() {
	        return fechaNacimiento;
	    }

	    public void setFechaNacimiento(java.time.LocalDate fechaNacimiento) {
	        this.fechaNacimiento = fechaNacimiento;
	    }

	    public String getCorreo() {
	        return correo;
	    }

	    public void setCorreo(String correo) {
	        this.correo = correo;
	    }

	    public String getTelefono() {
	        return telefono;
	    }

	    public void setTelefono(String telefono) {
	        this.telefono = telefono;
	    }

	    public boolean isEstado() {
	        return estado;
	    }

	    public void setEstado(boolean estado) {
	        this.estado = estado;
	    }
	
}
