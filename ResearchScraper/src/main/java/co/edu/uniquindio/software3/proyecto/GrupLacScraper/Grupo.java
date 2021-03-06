package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

import java.util.ArrayList;

import co.edu.uniquindio.software3.proyecto.CvLacScraper.Investigador;

public class Grupo {
	private int id;
	private String nombre;
	private String anioFundacion;
	private String lider;
	private String clasificacion;
	private String areaDeConocimiento;
	private ArrayList<String> integrantes;
	private ArrayList<Articulo> articulos;
	private ArrayList<EventoCientifico> eventos;
	private ArrayList<Libro> libros;
	private ArrayList<Proyecto> proyectos;

	public Grupo(int id, String nombre, String anioFundacion, String lider, String clasificacion, String areaDeConocimiento,
			ArrayList<String> integrantes,ArrayList<Articulo> articulos, ArrayList<EventoCientifico> eventos, ArrayList<Libro> libros,
			ArrayList<Proyecto> proyectos) {
		this.id = id;
		this.nombre = nombre;
		this.anioFundacion = anioFundacion;
		this.lider = lider;
		this.clasificacion = clasificacion;
		this.areaDeConocimiento = areaDeConocimiento;
		this.integrantes= integrantes;
		this.articulos = articulos;
		this.eventos = eventos;
		this.libros = libros;
		this.proyectos = proyectos;
	}

	public Grupo() {
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

	public String getAnioFundacion() {
		return anioFundacion;
	}

	public void setAnioFundacion(String anioFundacion) {
		this.anioFundacion = anioFundacion;
	}

	public String getLider() {
		return lider;
	}

	public void setLider(String lider) {
		this.lider = lider;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public String getAreaDeConocimiento() {
		return areaDeConocimiento;
	}

	public void setAreaDeConocimiento(String areaDeConocimiento) {
		this.areaDeConocimiento = areaDeConocimiento;
	}

	public ArrayList<Articulo> getArticulos() {
		return articulos;
	}

	public void setArticulos(ArrayList<Articulo> articulos) {
		this.articulos = articulos;
	}

	public ArrayList<EventoCientifico> getEventos() {
		return eventos;
	}

	public void setEventos(ArrayList<EventoCientifico> eventos) {
		this.eventos = eventos;
	}

	

	public ArrayList<Libro> getLibros() {
		return libros;
	}

	public void setLibros(ArrayList<Libro> libros) {
		this.libros = libros;
	}

	public ArrayList<Proyecto> getProyectos() {
		return proyectos;
	}

	public void setProyectos(ArrayList<Proyecto> proyectos) {
		this.proyectos = proyectos;
	}

	public ArrayList<String> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(ArrayList<String> integrantes) {
		this.integrantes = integrantes;
	}
	
}
