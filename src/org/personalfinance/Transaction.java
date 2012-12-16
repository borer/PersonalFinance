package org.personalfinance;

import java.util.Date;

public class Transaction implements Comparable<Transaction> {
	
	//Used to identify this object in the database
	private int id;
	
	private int category;
	
	private float cantidadDinero;
	
	private Date fecha;
	
	private String nota;
	
	private int localizacionValida;
	
	private float longitud;
	
	private float latitud;
	
	public boolean isOutcome;
	
	
	//This must exist ,so don't remove it with an excuse to optimize this .
	public Transaction(){
		
	}
	
	public Transaction(float cantidadDinero, Date fecha, String nota,
			boolean isOutcome) {
		super();
		this.cantidadDinero = cantidadDinero;
		this.nota = nota;
		this.fecha = fecha;
		this.isOutcome = isOutcome;
	}
	
	public Transaction(int id, int category, float cantidadDinero, Date fecha,
			String nota, int localizacionValida, float longitud, float latitud,
			boolean isOutcome) {
		super();
		this.id = id;
		this.category = category;
		this.cantidadDinero = cantidadDinero;
		this.fecha = fecha;
		this.nota = nota;
		this.localizacionValida = localizacionValida;
		this.longitud = longitud;
		this.latitud = latitud;
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getLocalizacionValida() {
		return localizacionValida;
	}

	public void setLocalizacionValida(int localizacionValida) {
		this.localizacionValida = localizacionValida;
	}

	public float getLongitud() {
		return longitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

	public float getLatitud() {
		return latitud;
	}

	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}

	@Override
	  public int compareTo(Transaction t) {
		if (getFecha() == null || t.getFecha() == null)
		      return 0;
		
	    return getFecha().compareTo(t.getFecha());
	  }
	
}
