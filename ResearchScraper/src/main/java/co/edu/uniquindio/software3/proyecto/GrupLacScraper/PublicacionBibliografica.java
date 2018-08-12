package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

public class PublicacionBibliografica {
	
	private String identificador;
	private String autores;
	private String anio;
	private String referencia;
	private String tipo;
	
	public PublicacionBibliografica(String identificador, String autores, String anio, String referencia, String tipo) {
		this.identificador = identificador;
		this.autores = autores;
		this.anio = anio;
		this.referencia = referencia;
		this.tipo=tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public PublicacionBibliografica() {
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getAutores() {
		return autores;
	}

	public void setAutores(String autores) {
		this.autores = autores;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	
}
