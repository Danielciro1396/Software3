package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

public class Articulo {
	
	private int id;
	private String autores;
	private String titulo;
	private String lugar;
	private String nomRevista;
	private String anio;
	
	public Articulo(int id, String autores, String titulo, String lugar, String nomRevista, String anio) {
		super();
		this.id = id;
		this.autores = autores;
		this.titulo = titulo;
		this.lugar = lugar;
		this.nomRevista = nomRevista;
		this.anio = anio;
	}

	public Articulo() {
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

	public String getNomRevista() {
		return nomRevista;
	}

	public void setNomRevista(String nomRevista) {
		this.nomRevista = nomRevista;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	
}
