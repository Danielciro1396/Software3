package co.edu.uniquindio.software3.proyecto.ResearchScraper;

public class Proyecto {

	private int id;
	private String tipo;
	private String nombre;
	private String inicio;
	
	public Proyecto(int id, String tipo, String nombre, String inicio) {
		this.id = id;
		this.tipo = tipo;
		this.nombre = nombre;
		this.inicio = inicio;
	}

	public Proyecto() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}
	
	
	
}
