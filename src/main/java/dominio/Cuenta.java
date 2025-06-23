package dominio;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cuenta {

    private int nroCuenta;
    private String cbu;
    private String tipoCuenta; 
    private LocalDateTime fechaCreacion;
    private BigDecimal saldo;
    private boolean estado;

   
    public Cuenta() {
    }

   
    public Cuenta(int nroCuenta, String cbu, String tipoCuenta, LocalDateTime fechaCreacion, BigDecimal saldo) {
        this.nroCuenta = nroCuenta;
        this.cbu = cbu;
        this.tipoCuenta = tipoCuenta;
        this.fechaCreacion = fechaCreacion;
        this.saldo = saldo;
    }

    // Getters y Setters

    public int getNroCuenta() {
        return nroCuenta;
    }

    public void setNroCuenta(int nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
    public boolean isEstado() {
		return estado;
	}


	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
    @Override
    public String toString() {
        return "Cuenta [nroCuenta=" + nroCuenta + ", cbu=" + cbu + ", tipoCuenta=" + tipoCuenta
                + ", fechaCreacion=" + fechaCreacion + ", saldo=" + saldo + "]";
    }


	
}