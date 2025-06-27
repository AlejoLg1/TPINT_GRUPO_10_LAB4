package dominio;
 
import java.math.BigDecimal;
 
public class Reporte {
 
    private String nombreReporte;
    private int total;           // Conteo genérico
    private BigDecimal monto;    // Para sumas como cuotas pagadas
 
    // Constructor vacío
    public Reporte() {}
 
    public Reporte(String nombreReporte, int total, BigDecimal monto) {
        this.nombreReporte = nombreReporte;
        this.total = total;
        this.monto = monto;
    }
 
    // Getters y setters
    public String getNombreReporte() {
        return nombreReporte;
    }
    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public BigDecimal getMonto() {
        return monto;
    }
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}