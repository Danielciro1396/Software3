package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

public class Integrante {

	private String nombre;
	private String horas;
	private String vinculacion;
	
	
	public Integrante() {
		super();
	}


	public Integrante(String nombre, String horas, String vinculacion) {
		super();
		this.nombre = nombre;
		this.horas = horas;
		this.vinculacion = vinculacion;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getHoras() {
		return horas;
	}


	public void setHoras(String horas) {
		this.horas = horas;
	}


	public String getVinculacion() {
		return vinculacion;
	}


	public void setVinculacion(String vinculacion) {
		this.vinculacion = vinculacion;
	}
	
	
	
}
