package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

public class Software {
	private String nombre;
	private String tipo;
	private String lugar;
	private String anio;
	private String financiadora;
	private String autores;
	
	
	public Software(String nombre, String tipo, String lugar, String anio, String financiadora, String autores) {
		super();
		this.nombre = nombre;
		this.tipo = tipo;
		this.lugar = lugar;
		this.anio = anio;
		this.financiadora = financiadora;
		this.autores = autores;
	}


	public Software() {
		super();
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getLugar() {
		return lugar;
	}


	public void setLugar(String lugar) {
		this.lugar = lugar;
	}


	public String getAnio() {
		return anio;
	}


	public void setAnio(String anio) {
		this.anio = anio;
	}


	public String getFinanciadora() {
		return financiadora;
	}


	public void setFinanciadora(String financiadora) {
		this.financiadora = financiadora;
	}


	public String getAutores() {
		return autores;
	}


	public void setAutores(String autores) {
		this.autores = autores;
	}
	
	
	
}
