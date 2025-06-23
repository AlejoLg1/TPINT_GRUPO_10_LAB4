package dominio;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Movimiento {
    private int idMovimiento;
    private int nroCuenta;
    private int idTipoMovimiento;
    private LocalDateTime fecha;
    private String detalle;
    private BigDecimal importe;

    // getters y setters

    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }

    public int getNroCuenta() { return nroCuenta; }
    public void setNroCuenta(int nroCuenta) { this.nroCuenta = nroCuenta; }

    public int getIdTipoMovimiento() { return idTipoMovimiento; }
    public void setIdTipoMovimiento(int idTipoMovimiento) { this.idTipoMovimiento = idTipoMovimiento; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
}
