package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

public class Innovacion {
	private String nombre;
	private String lugar;
	private String anio;
	private String financiadora;
	private String autores;
	public Innovacion(String nombre, String lugar, String anio, String financiadora, String autores) {
		super();
		this.nombre = nombre;
		this.lugar = lugar;
		this.anio = anio;
		this.financiadora = financiadora;
		this.autores = autores;
	}
	public Innovacion() {
		super();
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
