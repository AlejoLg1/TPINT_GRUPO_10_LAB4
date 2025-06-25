package dominio;

import java.sql.Timestamp;

public class Prestamo {
    private int id_prestamo;
    private int id_cliente;
    private int nro_cuenta;
    private Timestamp fecha;
    private double importe_solicitado;
    private int cantidad_cuotas;
    private double monto_cuota;
    private boolean autorizacion;

    // Getters y setters
    public int getId_prestamo() { return id_prestamo; }
    public void setId_prestamo(int id_prestamo) { this.id_prestamo = id_prestamo; }

    public int getId_cliente() { return id_cliente; }
    public void setId_cliente(int id_cliente) { this.id_cliente = id_cliente; }

    public int getNro_cuenta() { return nro_cuenta; }
    public void setNro_cuenta(int nro_cuenta) { this.nro_cuenta = nro_cuenta; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }

    public double getImporte_solicitado() { return importe_solicitado; }
    public void setImporte_solicitado(double importe_solicitado) { this.importe_solicitado = importe_solicitado; }

    public int getCantidad_cuotas() { return cantidad_cuotas; }
    public void setCantidad_cuotas(int cantidad_cuotas) { this.cantidad_cuotas = cantidad_cuotas; }

    public double getMonto_cuota() { return monto_cuota; }
    public void setMonto_cuota(double monto_cuota) { this.monto_cuota = monto_cuota; }

    public boolean isAutorizacion() { return autorizacion; }
    public void setAutorizacion(boolean autorizacion) { this.autorizacion = autorizacion; }
}