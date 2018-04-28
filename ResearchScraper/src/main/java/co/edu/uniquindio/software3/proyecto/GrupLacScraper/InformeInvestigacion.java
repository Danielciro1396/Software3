package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

public class InformeInvestigacion {
	private int id;
	private String autores;
	private String titulo;
	private String anio;
	private String proyecto;
	public InformeInvestigacion(int id, String autores, String titulo, String anio, String proyecto) {
		super();
		this.id = id;
		this.autores = autores;
		this.titulo = titulo;
		this.anio = anio;
		this.proyecto = proyecto;
	}
	public InformeInvestigacion() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAutores() {
		return autores;
	}
	public void setAutores(String autores) {
		this.autores = autores;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getProyecto() {
		return proyecto;
	}
	public void setProyecto(String proyecto) {
		this.proyecto = proyecto;
	}
	
	
}
