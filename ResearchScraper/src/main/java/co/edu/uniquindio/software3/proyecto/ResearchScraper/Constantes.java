package co.edu.uniquindio.software3.proyecto.ResearchScraper;

public class Constantes {
	public static final String INSERT_INVES = "insert into investigadores (id, nombre, categoria, formacion) values ";
	public static final String DELETE_INVES = "delete from investigadores where";
	public static final String SELECT_INVES = "select * from investigadores";
	public static final String INSERT_ART = "insert into articulos (titulo, autores, lugar, nombre_revista, anio, investigadores_id) values ";
	public static final String INSERT_EVT = "insert into eventos (nombre, tipo, ambito, fecha, lugar, rol, investigadores_id) values ";
	public static final String INSERT_LIB = "insert into libros (titulo, autores, lugar, anio, editorial, investigadores_id) values ";
	public static final String INSERT_PROY = "insert into proyectos (nombre, tipo, fecha, investigadores_id) values ";
	}
