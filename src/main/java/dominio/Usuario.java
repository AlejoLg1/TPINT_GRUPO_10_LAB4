package dominio;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String clave;
    private String tipo; // "BANCARIO" o "CLIENTE"
    private boolean isAdmin;
    private boolean estado;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombreUsuario, String clave, String tipo, boolean isAdmin, boolean estado) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.tipo = tipo;
        this.isAdmin = isAdmin;
        this.estado = estado;
    }

    // Setters
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // Getters
    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return "Usuario [idUsuario=" + idUsuario + ", nombreUsuario=" + nombreUsuario + ", clave=" + clave
                + ", tipo=" + tipo + ", isAdmin=" + isAdmin + ", estado=" + estado + "]";
    }
}
