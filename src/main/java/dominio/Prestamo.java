package dominio;

import java.sql.Timestamp;

public class Prestamo {
    private int id_prestamo;
    private Cliente cliente;
    private Cuenta cuenta;
    private Timestamp fecha;
    private double importe_solicitado;
    private int cantidad_cuotas;
    private double monto_cuota;
    private boolean autorizacion;
    private String estado;
    private int cuotas_pagadas;

    // Getters y setters
    public int getId_prestamo() { return id_prestamo; }
    public void setId_prestamo(int id_prestamo) { this.id_prestamo = id_prestamo; }

    public Cliente get_cliente() { return cliente; }
    public void set_cliente(Cliente cliente) { this.cliente = cliente; }

    public Cuenta get_cuenta() { return cuenta; }
    public void set_cuenta(Cuenta cuenta) { this.cuenta = cuenta; }

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
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getCuotas_pagadas() { return cuotas_pagadas; }
    public void setCuotas_pagadas(int cuotas_pagadas) { this.cuotas_pagadas = cuotas_pagadas; } 
}
