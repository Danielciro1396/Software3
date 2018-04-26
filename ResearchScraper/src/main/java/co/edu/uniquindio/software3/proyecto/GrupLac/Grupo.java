package co.edu.uniquindio.software3.proyecto.GrupLac;

import java.util.ArrayList;

public class Grupo {
	private String nombre;
	private String añoFundación;
	private String lider;
	private String clasificacion;
	private String AreaDeConocimiento;
	private String instituciones;
	private ArrayList<String> lineasDeInvestigacion;
	private ArrayList<Integrante> integrantes;
	private ArrayList<Articulo> articulos;
	private ArrayList<EventoCientifico> eventos;
	private ArrayList<InformeInvestigacion> informesInvestigacion;
	private ArrayList<Innovacion> innovaciones;
	private ArrayList<Libro> libros;
	private ArrayList<Software> softwares;
	
	public Grupo(String nombre, String añoFundación, String lider, String clasificacion, String areaDeConocimiento,
			String instituciones, ArrayList<String> lineasDeInvestigacion, ArrayList<Integrante> integrantes,
			ArrayList<Articulo> articulos, ArrayList<EventoCientifico> eventos,
			ArrayList<InformeInvestigacion> informesInvestigacion, ArrayList<Innovacion> innovaciones,
			ArrayList<Libro> libros, ArrayList<Software> softwares) {
		super();
		this.nombre = nombre;
		this.añoFundación = añoFundación;
		this.lider = lider;
		this.clasificacion = clasificacion;
		AreaDeConocimiento = areaDeConocimiento;
		this.instituciones = instituciones;
		this.lineasDeInvestigacion = lineasDeInvestigacion;
		this.integrantes = integrantes;
		this.articulos = articulos;
		this.eventos = eventos;
		this.informesInvestigacion = informesInvestigacion;
		this.innovaciones = innovaciones;
		this.libros = libros;
		this.softwares = softwares;
	}

	public Grupo() {
		super();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAñoFundación() {
		return añoFundación;
	}

	public void setAñoFundación(String añoFundación) {
		this.añoFundación = añoFundación;
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
		return AreaDeConocimiento;
	}

	public void setAreaDeConocimiento(String areaDeConocimiento) {
		AreaDeConocimiento = areaDeConocimiento;
	}

	public String getInstituciones() {
		return instituciones;
	}

	public void setInstituciones(String instituciones) {
		this.instituciones = instituciones;
	}

	public ArrayList<String> getLineasDeInvestigacion() {
		return lineasDeInvestigacion;
	}

	public void setLineasDeInvestigacion(ArrayList<String> lineasDeInvestigacion) {
		this.lineasDeInvestigacion = lineasDeInvestigacion;
	}

	public ArrayList<Integrante> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(ArrayList<Integrante> integrantes) {
		this.integrantes = integrantes;
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

	public ArrayList<InformeInvestigacion> getInformesInvestigacion() {
		return informesInvestigacion;
	}

	public void setInformesInvestigacion(ArrayList<InformeInvestigacion> informesInvestigacion) {
		this.informesInvestigacion = informesInvestigacion;
	}

	public ArrayList<Innovacion> getInnovaciones() {
		return innovaciones;
	}

	public void setInnovaciones(ArrayList<Innovacion> innovaciones) {
		this.innovaciones = innovaciones;
	}

	public ArrayList<Libro> getLibros() {
		return libros;
	}

	public void setLibros(ArrayList<Libro> libros) {
		this.libros = libros;
	}

	public ArrayList<Software> getSoftwares() {
		return softwares;
	}

	public void setSoftwares(ArrayList<Software> softwares) {
		this.softwares = softwares;
	}
	
	
	
}
