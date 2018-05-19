package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

public class Libro {

	private int id;
	private String autores;
	private String titulo;
	private String lugar;
	private String anio;
	private String editorial;
	private String repetido;

	public Libro(int id, String autores, String titulo, String lugar, String anio, String editorial, String repetido) {
		super();
		this.id = id;
		this.autores = autores;
		this.titulo = titulo;
		this.lugar = lugar;
		this.anio = anio;
		this.editorial = editorial;
		this.repetido = repetido;
	}

	public Libro() {
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
	public String getRepetido() {
		return repetido;
	}

	public void setRepetido(String repetido) {
		this.repetido = repetido;
	}
}
