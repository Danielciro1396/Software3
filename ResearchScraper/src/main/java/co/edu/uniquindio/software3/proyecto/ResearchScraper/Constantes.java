package co.edu.uniquindio.software3.proyecto.ResearchScraper;

public class Constantes {
	public static final String INSERT_INVES = "insert into investigadores (id, nombre, categoria, formacion) values ";
	public static final String DELETE_INVES = "delete from investigadores where";
	public static final String SELECT_INVES = "select * from investigadores";
	public static final String INSERT_ART = "insert into articulos (autores, titulo, revista, lugar, anio, esEspecializada, investigadores_id) values ";
	public static final String INSERT_EVT = "insert into eventos (nombre, tipo, ambito, lugar, fecha, rol, investigadores_id) values ";
	public static final String INSERT_LIB = "insert into libros (titulo, autores, lugar, anio, editorial, investigadores_id) values ";
	public static final String INSERT_PROY = "insert into proyectos (nombre, tipo, fecha, investigadores_id) values ";
	
	public static final String INSERT_GRUP = "insert into investigadores (id, nombre, anio_fundacion, clasificacion, lider, areas_conocimiento) values ";
	public static final String INSERT_GRUP_ART = "insert into articulos (autores, titulo, revista, lugar, anio, tipo, grupos_id) values ";
	public static final String INSERT_GRUP_EVT = "insert into eventos (nombre, tipo, ambito, lugar, fecha, tipo_participacion, grupos_id) values ";
	public static final String INSERT_GRUP_LIB = "insert into libros (titulo, autores, lugar, anio, editorial, grupos_id) values ";
	public static final String INSERT_GRUP_PROY = "insert into proyectos (nombre, tipo, fecha, grupos_id) values ";
	
	}
