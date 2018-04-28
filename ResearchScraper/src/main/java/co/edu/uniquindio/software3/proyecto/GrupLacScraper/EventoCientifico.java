package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

public class EventoCientifico {
	private int id;
	private String nombre;
	private String tipo;
	private String ambito;
	private String fecha;
	private String lugar;
	private String tipoParticipacion;
	
	public EventoCientifico(int id, String nombre, String tipo, String ambito, String fecha, String lugar,
			String tipoParticipacion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.ambito = ambito;
		this.fecha = fecha;
		this.lugar = lugar;
		this.tipoParticipacion = tipoParticipacion;
	}

	public EventoCientifico() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public String getTipoParticipacion() {
		return tipoParticipacion;
	}

	public void setTipoParticipacion(String tipoParticipacion) {
		this.tipoParticipacion = tipoParticipacion;
	}
	
	

}
