package dominio;

public class TipoCuenta {

    private int idTipoCuenta;
    private String descripcion;

    public TipoCuenta() {}

    public TipoCuenta(int idTipoCuenta, String descripcion) {
        this.idTipoCuenta = idTipoCuenta;
        this.descripcion = descripcion;
    }

    public int getIdTipoCuenta() {
        return idTipoCuenta;
    }

    public void setIdTipoCuenta(int idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoCuenta [idTipoCuenta=" + idTipoCuenta + ", descripcion=" + descripcion + "]";
    }
}
