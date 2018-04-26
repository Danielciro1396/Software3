package co.edu.uniquindio.software3.proyecto.OrcidScraper;

public class Produccion {

	String nombre;
	String revista;
	String anio;
	String tipo;

	public Produccion() {
	}

	public Produccion(String nombre, String revista, String anio, String tipo) {

		this.nombre = nombre;
		this.revista = revista;
		this.anio = anio;
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRevista() {
		return revista;
	}

	public void setRevista(String revista) {
		this.revista = revista;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
