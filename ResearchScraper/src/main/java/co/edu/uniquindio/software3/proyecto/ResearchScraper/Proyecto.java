package co.edu.uniquindio.software3.proyecto.ResearchScraper;

public class Proyecto {

	private int id;
	private String tipo;
	private String nombre;
	private String fecha;

	public Proyecto(int id, String tipo, String nombre, String fecha) {
		this.id = id;
		this.tipo = tipo;
		this.nombre = nombre;
		this.fecha = fecha;
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

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
