package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import java.util.ArrayList;

public class Investigador {
	
private int id;	
private String nombre;
private String categoria;
private String formacion;
private ArrayList <EventoCientifico> eventos;
private ArrayList <Articulo> articulos;
private ArrayList <InformeInvestigacion> informesInvestigaciones;
private ArrayList <Libro> libros;
private ArrayList <Proyecto> proyectos;


public Investigador(int id, String nombre, String categoria, String formacion, ArrayList<EventoCientifico> eventos,
		ArrayList<Articulo> articulos, ArrayList<InformeInvestigacion> informesInvestigaciones, ArrayList<Libro> libros,
		ArrayList<Proyecto> proyectos) {
	this.id = id;
	this.nombre = nombre;
	this.categoria = categoria;
	this.formacion = formacion;
	this.eventos = eventos;
	this.articulos = articulos;
	this.informesInvestigaciones = informesInvestigaciones;
	this.libros = libros;
	this.proyectos = proyectos;
}


public Investigador() {
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


public String getCategoria() {
	return categoria;
}


public void setCategoria(String categoria) {
	this.categoria = categoria;
}


public String getFormacion() {
	return formacion;
}


public void setFormacion(String formacion) {
	this.formacion = formacion;
}


public ArrayList<EventoCientifico> getEventos() {
	return eventos;
}


public void setEventos(ArrayList<EventoCientifico> eventos) {
	this.eventos = eventos;
}


public ArrayList<Articulo> getArticulos() {
	return articulos;
}


public void setArticulos(ArrayList<Articulo> articulos) {
	this.articulos = articulos;
}


public ArrayList<InformeInvestigacion> getInformesInvestigaciones() {
	return informesInvestigaciones;
}


public void setInformesInvestigaciones(ArrayList<InformeInvestigacion> informesInvestigaciones) {
	this.informesInvestigaciones = informesInvestigaciones;
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


}
