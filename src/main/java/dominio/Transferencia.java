package dominio;

import java.time.LocalDate;

public class Transferencia {
    private int id;
    private int idMovimientoSalida;
    private int idMovimientoEntrada;
    private double importe;
    private LocalDate fecha;
    private int nroCuentaOrigen;
    private int nroCuentaDestino;

    public Transferencia() {}

    public Transferencia(int idMovimientoSalida, int idMovimientoEntrada, double importe, LocalDate fecha) {
        this.idMovimientoSalida = idMovimientoSalida;
        this.idMovimientoEntrada = idMovimientoEntrada;
        this.importe = importe;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdMovimientoSalida() { return idMovimientoSalida; }
    public void setIdMovimientoSalida(int idMovimientoSalida) { this.idMovimientoSalida = idMovimientoSalida; }

    public int getIdMovimientoEntrada() { return idMovimientoEntrada; }
    public void setIdMovimientoEntrada(int idMovimientoEntrada) { this.idMovimientoEntrada = idMovimientoEntrada; }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public int getNroCuentaOrigen() { return nroCuentaOrigen; }
    public void setNroCuentaOrigen(int nroCuentaOrigen) { this.nroCuentaOrigen = nroCuentaOrigen; }

    public int getNroCuentaDestino() { return nroCuentaDestino; }
    public void setNroCuentaDestino(int nroCuentaDestino) { this.nroCuentaDestino = nroCuentaDestino; }
}
