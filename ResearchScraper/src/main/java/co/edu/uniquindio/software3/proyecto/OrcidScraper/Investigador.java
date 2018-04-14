package co.edu.uniquindio.software3.proyecto.OrcidScraper;

import java.util.ArrayList;

public class Investigador {

	private String nombre;
	private ArrayList<Produccion> producciones;
	
	
	public Investigador() {
	}


	public Investigador(String nombre, ArrayList<Produccion> producciones) {
		this.nombre = nombre;
		this.producciones = producciones;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public ArrayList<Produccion> getProducciones() {
		return producciones;
	}


	public void setProducciones(ArrayList<Produccion> producciones) {
		this.producciones = producciones;
	}
	
	
	
}
