package org.personalfinance;

import java.util.Date;

public class Transaccion implements Comparable<Transaccion> {
	private float cantidadDinero;
	private Date fecha;
	private String nota;
	public boolean isOutcome;
	
	public Transaccion(float cantidadDinero, Date fecha, String nota,
			boolean isOutcome) {
		super();
		this.cantidadDinero = cantidadDinero;
		this.nota = nota;
		this.fecha = fecha;
		this.isOutcome = isOutcome;
	}
	
	public float getCantidadDinero() {
		return cantidadDinero;
	}
	public void setCantidadDinero(float cantidadDinero) {
		this.cantidadDinero = cantidadDinero;
	}
	public String getNota() {
		return nota;
	}
	public void setNota(String nota) {
		this.nota = nota;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public boolean isOutcome() {
		return isOutcome;
	}
	public void setOutcome(boolean isOutcome) {
		this.isOutcome = isOutcome;
	}
	
	@Override
	  public int compareTo(Transaccion t) {
		if (getFecha() == null || t.getFecha() == null)
		      return 0;
		
	    return getFecha().compareTo(t.getFecha());
	  }
	
}
