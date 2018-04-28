package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

public class CapituloLibro {
	
	private int id;
	private String tituloCapitulo;
	private String tituloLibro;
	private String lugar;
	private String anio;
	private String editorial;
	private String autores;
	public CapituloLibro(int id, String tituloCapitulo, String tituloLibro, String lugar, String anio, String editorial,
			String autores) {
		super();
		this.id = id;
		this.tituloCapitulo = tituloCapitulo;
		this.tituloLibro = tituloLibro;
		this.lugar = lugar;
		this.anio = anio;
		this.editorial = editorial;
		this.autores = autores;
	}
	public CapituloLibro() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTituloCapitulo() {
		return tituloCapitulo;
	}
	public void setTituloCapitulo(String tituloCapitulo) {
		this.tituloCapitulo = tituloCapitulo;
	}
	public String getTituloLibro() {
		return tituloLibro;
	}
	public void setTituloLibro(String tituloLibro) {
		this.tituloLibro = tituloLibro;
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
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}
	public String getAutores() {
		return autores;
	}
	public void setAutores(String autores) {
		this.autores = autores;
	}
	
	
}
