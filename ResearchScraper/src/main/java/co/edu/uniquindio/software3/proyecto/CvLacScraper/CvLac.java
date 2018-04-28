package co.edu.uniquindio.software3.proyecto.CvLacScraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import co.edu.uniquindio.software3.proyecto.ResearchScraper.ArrayThread;

public class CvLac {

	// Lista en la que se guardan las direcciones url de cada investigador
	public ArrayList<String> urlSet;

	// Lista en la que se guarda la informacion personal de cada invsestigador
	public ArrayList<String> elemInfoPersonal = new ArrayList<>();

	// Lista en la que se guardan la formacion academica de cada invsestigador
	public ArrayList<String> elemFormacionAcam = new ArrayList<>();

	// Lista en la que se guardan los eventos en los que ha participado de cada
	// invsestigador
	public ArrayList<String> elemEventos = new ArrayList<>();

	// Lista en la que se guardan los articulos que ha escrito de cada
	// invsestigador
	public ArrayList<String> elemArticulos = new ArrayList<>();

	// Lista en la que se guardan los libros que ha escrito de cada
	// invsestigador
	public ArrayList<String> elemLibros = new ArrayList<>();

	// Lista en la que se guardan los informes de investigación que ha escrito
	// de cada invsestigador
	public ArrayList<String> elemInformes = new ArrayList<>();

	// Lista en la que se guardan los proyectos en los que ha participado de
	// cada invsestigador
	public ArrayList<String> elemProyectos = new ArrayList<>();

	// Lista en la que se guardan las publicaciones en revistas no
	// especializadas que ha realizado de cada invsestigador
	public ArrayList<String> elemPublicacionesN = new ArrayList<>();

	// Lista sincronizada en la que se guardan todos los investidagores junto a
	// su respectiva informacion
	public List<Investigador> investigadores = Collections.synchronizedList(new ArrayList<Investigador>());

	/**
	 * Metodo que realiza la extraccion de la estructura de una pagina web,
	 * separa en las diferentas categorias la estructura de la pagina web y
	 * hacer el llamado al metodo que asigna los datos a cada investigador
	 * 
	 * @param url,
	 *            direccion url de un investigador
	 */
	public void extraer(String url) {

		if (getStatusConnectionCode(url) == 200) {
			// Obtengo el HTML de la web en un objeto Document
			Document document = getHtmlDocument(url);
			// Busca todas las coincidencias que estan dentro de
			Elements entradas = document.select("tbody>tr>td>table>tbody");

			for (Element elem : entradas) {

				if (elem.text().contains("Nombre en citaciones")) {
					elemInfoPersonal.add(elem.toString());
				}
				if (elem.text().contains("Formación Académica")) {
					elemFormacionAcam.add(elem.toString());
				}
				if (elem.text().contains("Eventos científicos")) {
					elemEventos.add(elem.toString());
				}
				if (elem.text().contains("Artículos")) {
					elemArticulos.add(elem.toString());
				}
				if (elem.text().contains("Libros")) {
					elemLibros.add(elem.toString());
				}
				if (elem.text().contains("Informes de investigación")) {
					elemInformes.add(elem.toString());
				}
				if (elem.text().contains("Proyectos")) {
					elemProyectos.add(elem.toString());
				}
				if (elem.text().contains("Textos en publicaciones no científicas")) {
					elemPublicacionesN.add(elem.toString());
				}

			}

			extraerDatos(limpiar(elemInfoPersonal), limpiar(elemFormacionAcam), limpiar(elemEventos),
					limpiar(elemArticulos), limpiar(elemLibros), limpiar(elemInformes), limpiar(elemProyectos),
					limpiar(elemPublicacionesN));

		} else {
			System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(url));
		}

	}

	/**
	 * Este metodo se encarga de hacer el llamado al metodo que lee un archivo
	 * plano y carga el dataSet de url's, ademas, crea y lanza un pool de hilos
	 * para mejorar el tiempo de ejecucion del programa
	 */
	public void scrapData() {
		long startTime = System.currentTimeMillis();
		long stopTime = 0;
		long elapsedTime = 0;
		leerDataSet();
		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 0; i < urlSet.size(); i++) {
			Runnable worker = new ArrayThread(urlSet.get(i), i, this,0, null, null);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;

		}
		System.err.println(elapsedTime);

	}

	/**
	 * Método provisto por JSoup para comprobar el Status code de la respuesta
	 * que recibo al hacer la petición Codigos: 200 OK 300 Multiple Choices 301
	 * Moved Permanently 305 Use Proxy 400 Bad Request 403 Forbidden 404 Not
	 * Found 500 Internal Server Error 502 Bad Gateway 503 Service Unavailable
	 * 
	 * @param url,
	 *            el enlace de la página web a analizar.
	 * @return Status Code, el código que identifica el estado de la página.
	 */
	public int getStatusConnectionCode(String url) {

		Response response = null;

		try {
			response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el Status Code: " + ex.getMessage());
		}
		return response.statusCode();
	}

	/**
	 * Método que retorna un objeto de la clase Document con el contenido del
	 * HTML de la web para poder ser parseado posteriormente con JSoup
	 * 
	 * @param url,
	 *            el enlace de la página web a analizar.
	 * @return Documento con el HTML de la página en cuestión.
	 */
	public Document getHtmlDocument(String url) {

		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		}
		return doc;
	}

	/**
	 * Este metodo asigna cada uno de los elementos encontrados al investigador
	 * respectivo y lo añade a la lista de investigadores
	 * 
	 * @param datosPersonales,
	 *            lista con los datos personales de cada investigador
	 * @param formacion,
	 *            lista con la formacion academica de cada investigador
	 * @param eventos,
	 *            lista con los eventos en los que participo cada investigador
	 * @param articulos,
	 *            lista con los articulos escritos por cada investigador
	 * @param libros,
	 *            lista con los libros escritos por cada investigador
	 * @param informes,
	 *            lista con los informes de investigacion realizados por cada
	 *            investigador
	 * @param proyectos,
	 *            lista con los proyectos realizados por cada investigador
	 * @param publicacionesN,
	 *            lista con las publicaciones en revistas no especializadas
	 *            realizadas por cada investigador
	 */
	public void extraerDatos(ArrayList<String> datosPersonales, ArrayList<String> formacion, ArrayList<String> eventos,
			ArrayList<String> articulos, ArrayList<String> libros, ArrayList<String> informes,
			ArrayList<String> proyectos, ArrayList<String> publicacionesN) {
		Investigador investigador = new Investigador();
		try {
			for (int i = 0; i < datosPersonales.size(); i++) {
				if (datosPersonales.get(i).contains("Categoría")) {
					investigador.setCategoria(datosPersonales.get(i + 2));
				}
				if (datosPersonales.get(i).equals("Nombre")) {
					investigador.setNombre(datosPersonales.get(i + 2));
				}
			}
		} catch (NullPointerException e) {

		}

		try {
			extraerFormacionAcademica(formacion, investigador);
		} catch (NullPointerException e) {

		}

		try {
			extraerEventos(eventos, investigador);
		} catch (NullPointerException e) {

		}
		try {
			extraerArticulos(articulos, investigador);
		} catch (NullPointerException e) {

		}
		try {
			extraerLibros(libros, investigador);
		} catch (NullPointerException e) {

		}
		try {
			extraerProyectos(proyectos, investigador);
		} catch (NullPointerException e) {

		}

		try {
			boolean estaRepetido = false;
			for (int i = 0; i < investigadores.size(); i++) {
				if (investigadores.get(i).getNombre().equals(investigador.getNombre())) {
					estaRepetido = true;
				}
			}
			if (estaRepetido) {
				Investigador investigador2 = new Investigador();

				ArrayList<EventoCientifico> eventos1 = new ArrayList<>();
				ArrayList<Articulo> articulos1 = new ArrayList<>();
				ArrayList<Libro> libros1 = new ArrayList<>();
				ArrayList<Proyecto> proyectos1 = new ArrayList<>();

				investigador2.setArticulos(articulos1);
				investigador2.setEventos(eventos1);
				investigador2.setProyectos(proyectos1);
				investigador2.setLibros(libros1);
				investigador2.setNombre("Perfil Privado");
				investigador2.setCategoria("No disponible");
				investigador2.setFormacion("No disponible");
				investigadores.add(investigador2);
			} else {
				investigadores.add(investigador);
			}

		} catch (Exception e) {

		}

	}

	/**
	 * Metodo que extrae y asigna la formacion academica de cada investigador
	 * 
	 * @param elementos,
	 *            Lista donde esta toda la informacion de la formacion academica
	 *            del investigador
	 * @param investigador,
	 *            investigador al que se le a asignar la informacion
	 */
	public void extraerFormacionAcademica(ArrayList<String> elementos, Investigador investigador) {
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).equals("Formación Académica")) {
				investigador.setFormacion(elementos.get(i + 6));
			}
		}
	}

	/**
	 * Este metodo filtra la informacion y la segmenta en su respectiva
	 * categoria para poder agregarla ordenadamente a las listas de cada uno de
	 * los investigadores
	 * 
	 * @param elementos,
	 *            Lista donde estan todos los eventos en los que ha participado
	 *            el investigador
	 * @param investigador,
	 *            investigador al que se le a asignar la informacion
	 */
	public void extraerEventos(ArrayList<String> elementos, Investigador investigador) {
		String nombre = "";
		String tipo = "";
		String ambito = "";
		String fecha = "";
		String lugar = "";
		String rol = "";
		String aux = "";
		String aux2 = "";
		aux = StringUtils.stripAccents(investigador.getNombre());
		aux = aux.replace("  ", " ");
		aux = aux.substring(1);
		ArrayList<EventoCientifico> eventoAux = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			EventoCientifico eventos = new EventoCientifico();
			if (elementos.get(i).contains("Nombre del evento:")) {
				nombre = elementos.get(i + 1);
			}
			if (elementos.get(i).contains("Tipo de evento:")) {
				tipo = elementos.get(i + 1);
			}
			if (elementos.get(i).contains("Ámbito:")) {
				ambito = elementos.get(i + 1);
			}
			if (elementos.get(i).contains("Realizado el:")) {
				String cadena = elementos.get(i);
				fecha = cadena.substring(13, 17);
				if (fecha.contains(",")) {
					fecha = "No registra";
				}

				char[] auxiliar = cadena.toCharArray();
				int posI = 0;
				int posF = 0;
				for (int j = 0; j < auxiliar.length; j++) {
					if (auxiliar[j] == 'e' && auxiliar[j + 1] == 'n' && auxiliar[j + 2] == ' ') {
						posI = j + 3;
						for (int k = posI; k < auxiliar.length; k++) {
							if (auxiliar[k] == '-') {
								posF = k;
								lugar = cadena.substring(posI, posF);
								j = k;
								break;
							}
						}
					}
				}
			}
			aux2 = StringUtils.stripAccents(elementos.get(i));
			if (aux2.equalsIgnoreCase(aux) && elementos.get(i + 1).contains("Rol en el evento:")) {
				rol = elementos.get(i + 2);
				eventos.setNombre(nombre);
				eventos.setTipo(tipo);
				eventos.setAmbito(ambito);
				eventos.setFecha(fecha);
				eventos.setLugar(lugar);
				eventos.setRol(rol);

				if (investigador.getEventos() != null) {
					eventoAux = investigador.getEventos();
					eventoAux.add(eventos);
				} else {
					eventoAux.add(eventos);
				}

				investigador.setEventos(eventoAux);
			}
		}

	}

	/**
	 * Este metodo filtra la informacion y la segmenta en su respectiva
	 * categoria para poder agregarla ordenadamente a las listas de cada uno de
	 * los investigadores
	 * 
	 * @param elementos,
	 *            Lista donde estan todos los articulos en los que ha
	 *            participado el investigador
	 * @param investigador,
	 *            investigador al que se le a asignar la informacion
	 */
	public void extraerArticulos(ArrayList<String> elementos, Investigador investigador) {
		boolean esEspecializada = false;
		String autores = "";
		String titulo = "";
		String lugar = "";
		String nomRevista = "";
		String anio = "";
		String aux = "";
		String aux2 = "";
		aux = StringUtils.stripAccents(investigador.getNombre());
		aux = aux.replace("  ", " ");
		aux = aux.substring(1, aux.length() - 1);
		ArrayList<Articulo> articuloAux = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			Articulo articulo = new Articulo();
			if (elementos.get(i).contains("Publicado en revista especializada")) {
				esEspecializada = true;
			}
			aux2 = StringUtils.stripAccents(elementos.get(i));
			if (aux2.toUpperCase().contains(aux.toUpperCase())) {
				String cadena = elementos.get(i);
				char[] auxiliar = cadena.toCharArray();
				int posI = 0;
				int posF = 0;
				for (int j = 0; j < auxiliar.length; j++) {
					if (auxiliar[j] != ' ') {
						if (auxiliar[j] == '"') {
							posI = j + 1;
							for (int k = posI + 1; k < auxiliar.length; k++) {
								if (auxiliar[k] == '"') {
									posF = k;
									titulo = cadena.substring(posI, posF);
									j = k;
									break;
								}
							}
						} else if (auxiliar[j] == ':') {
							posI = j + 1;
							lugar = cadena.substring(posI);
						} else {
							posI = j;
							for (int k = posI; k < auxiliar.length; k++) {
								if (auxiliar[k] == '"') {
									posF = k - 2;
									autores = cadena.substring(posI, posF);
									j = k - 1;
									break;
								}
							}
						}
					}

				}
				nomRevista = elementos.get(i + 1);
			}
			if (elementos.get(i).contains("ed:")) {
				nomRevista += " Editorial: " + elementos.get(i + 1);
			}
			if (elementos.get(i).contains("fasc.")) {
				String cadena = elementos.get(i + 1);
				char[] auxiliar = cadena.toCharArray();
				int posI = 0;
				int posF = 0;
				for (int j = 0; j < auxiliar.length; j++) {
					if (auxiliar[j] == ',') {
						posI = j + 1;
						for (int k = posI; k < auxiliar.length; k++) {
							if (auxiliar[k] == ',') {
								posF = k;
								anio = cadena.substring(posI, posF);
								j = k;
								break;
							}
						}
					}
				}
				articulo.setEsEspecializada(esEspecializada);
				articulo.setTitulo(titulo);
				articulo.setAnio(anio);
				articulo.setAutores(autores);
				articulo.setLugar(lugar);
				articulo.setNomRevista(nomRevista);
				articuloAux.add(articulo);
				investigador.setArticulos(articuloAux);
			}

		}
	}

	/**
	 * Este metodo filtra la informacion y la segmenta en su respectiva
	 * categoria para poder agregarla ordenadamente a las listas de cada uno de
	 * los investigadores
	 * 
	 * @param elementos,
	 *            Lista donde estan todos los libros en los que ha participado
	 *            el investigador
	 * @param investigador,
	 *            investigador al que se le a asignar la informacion
	 */
	public void extraerLibros(ArrayList<String> elementos, Investigador investigador) {
		String autores = "";
		String titulo = "";
		String lugar = "";
		String anio = "";
		String editorial = "";
		String aux = "";
		String aux2 = "";
		aux = StringUtils.stripAccents(investigador.getNombre());
		aux = aux.replace("  ", " ");
		aux = aux.substring(1, aux.length() - 1);
		ArrayList<Libro> libroAux = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			Libro libro = new Libro();
			aux2 = StringUtils.stripAccents(elementos.get(i));
			if (aux2.toUpperCase().contains(aux.toUpperCase())) {
				String cadena = elementos.get(i);
				char[] auxiliar = cadena.toCharArray();
				int posI = 0;
				int posF = 0;
				for (int j = 0; j < auxiliar.length; j++) {
					if (auxiliar[j] != ' ') {
						if (auxiliar[j] == '"') {
							posI = j + 1;
							for (int k = posI + 1; k < auxiliar.length; k++) {
								if (auxiliar[k] == '"') {
									posF = k;
									titulo = cadena.substring(posI, posF);
									j = k;
									break;
								}
							}
						} else if (auxiliar[j] == 'e' && auxiliar[j + 1] == 'n' && auxiliar[j + 2] == ':') {
							posI = j + 3;
							for (int l = posI; l < auxiliar.length; l++) {
								if (auxiliar[l] == '1' || auxiliar[l] == '2') {
									posF = l - 1;
									lugar = cadena.substring(posI, posF);
									j = l;
								}

							}

						} else if (auxiliar[j] == '1' || auxiliar[j] == '2') {
							posI = j;
							posF = j + 4;
							anio = cadena.substring(posI, posF);
						} else if (auxiliar[j] == 'e' && auxiliar[j + 1] == 'd' && auxiliar[j + 2] == ':') {
							posI = j + 3;
							posF = cadena.length();
							editorial = cadena.substring(posI, posF);
						}

						else {
							posI = j;
							for (int k = posI; k < auxiliar.length; k++) {
								if (auxiliar[k] == '"') {
									posF = k - 2;
									autores = cadena.substring(posI, posF);
									j = k - 1;
									break;
								}

							}
						}

					}

				}
				libro.setAnio(anio);
				libro.setAutores(autores);
				libro.setEditorial(editorial);
				libro.setLugar(lugar);
				libro.setTitulo(titulo);
				libroAux.add(libro);
				investigador.setLibros(libroAux);

			}
		}

	}

	/**
	 * Este metodo filtra la informacion y la segmenta en su respectiva
	 * categoria para poder agregarla ordenadamente a las listas de cada uno de
	 * los investigadores
	 * 
	 * @param elementos,
	 *            Lista donde estan todos los proyectos en los que ha
	 *            participado el investigador
	 * @param investigador,
	 *            investigador al que se le a asignar la informacion
	 */
	public void extraerProyectos(ArrayList<String> elementos, Investigador investigador) {
		int posI;
		int posF;
		String tipo = "";
		String nombre = "";
		String fecha = "";
		ArrayList<Proyecto> proyectoAux = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			Proyecto proyecto = new Proyecto();
			if (elementos.get(i).contains("Tipo de proyecto:")) {
				tipo = elementos.get(i + 1);
				nombre = elementos.get(i + 2);
			}
			if (elementos.get(i).contains("Inicio:")) {
				String cadena = elementos.get(i + 1);
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == '2') {
						posI = j;
						posF = j + 4;
						fecha = cadena.substring(posI, posF);
						j = posF;
					}
				}
				proyecto.setTipo(tipo);
				proyecto.setNombre(nombre);
				proyecto.setFecha(fecha);
				proyectoAux.add(proyecto);
				investigador.setProyectos(proyectoAux);
			}
		}
	}

	/**
	 * Metodo que elimina las etiquetas y caracteres especiales en la lista que
	 * tiene la estructura de la pagina web del cvlac de cada investigador
	 * 
	 * @param elementos,
	 *            lista que contiene la estructura textual de la pagina web
	 * @return Lista con la estructura de la pagina web sin las etiquetas y los
	 *         caracteres especiales
	 */
	public ArrayList<String> limpiar(ArrayList<String> elementos) {
		String etiquetas = "";
		ArrayList<String> elementosLimpio = new ArrayList<>();
		ArrayList<String> aux = new ArrayList<>();
		ArrayList<String> aux2 = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			etiquetas = elementos.get(i).replaceAll("\n", "");
			etiquetas = etiquetas.replaceAll("&nbsp;", " ");
		}
		char[] auxiliar = etiquetas.toCharArray();
		int posI = 0;
		int posF = 0;
		for (int j = 0; j < auxiliar.length; j++) {
			if (auxiliar[j] == '>') {
				posI = j;
				for (int i = j; i < auxiliar.length; i++) {
					if (auxiliar[i] == '<') {
						posF = i;
						elementosLimpio.add(etiquetas.substring(posI, posF));
						j = i;
						break;
					}
				}
			}
		}
		for (int i = 0; i < elementosLimpio.size(); i++) {
			String temporal = "";
			temporal = elementosLimpio.get(i).replaceAll(">", " ");
			aux.add(temporal);

		}
		for (int i = 0; i < aux.size(); i++) {
			if (!aux.get(i).equals(" ")) {
				String temporal = "";
				temporal = aux.get(i).substring(1);
				aux2.add(temporal);
			}

		}
		elementosLimpio = aux2;
		return elementosLimpio;
	}

	/**
	 * Metodo que lee un archivo de texto y carga la lista con las url's de los
	 * investigadores
	 */
	public void leerDataSet() {
		try {
			urlSet = new ArrayList<String>();
			String cadena;
			FileReader f = new FileReader("d://Dataset.txt");
			BufferedReader b = new BufferedReader(f);
			while ((cadena = b.readLine()) != null) {
				String url = "http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh="
						+ cadena;
				urlSet.add(url);
			}
			b.close();

		} catch (IOException ex) {
			ex.printStackTrace();

		}
	}

	public ArrayList<String> getElemInfoPersonal() {
		return elemInfoPersonal;
	}

	public void setElemInfoPersonal(ArrayList<String> elemInfoPersonal) {
		this.elemInfoPersonal = elemInfoPersonal;
	}

	public ArrayList<String> getElemFormacionAcam() {
		return elemFormacionAcam;
	}

	public void setElemFormacionAcam(ArrayList<String> elemFormacionAcam) {
		this.elemFormacionAcam = elemFormacionAcam;
	}

	public ArrayList<String> getElemEventos() {
		return elemEventos;
	}

	public void setElemEventos(ArrayList<String> elemEventos) {
		this.elemEventos = elemEventos;
	}

	public ArrayList<String> getElemArticulos() {
		return elemArticulos;
	}

	public void setElemArticulos(ArrayList<String> elemArticulos) {
		this.elemArticulos = elemArticulos;
	}

	public ArrayList<String> getElemLibros() {
		return elemLibros;
	}

	public void setElemLibros(ArrayList<String> elemLibros) {
		this.elemLibros = elemLibros;
	}

	public ArrayList<String> getElemInformes() {
		return elemInformes;
	}

	public void setElemInformes(ArrayList<String> elemInformes) {
		this.elemInformes = elemInformes;
	}

	public ArrayList<String> getElemProyectos() {
		return elemProyectos;
	}

	public void setElemProyectos(ArrayList<String> elemProyectos) {
		this.elemProyectos = elemProyectos;
	}

	public ArrayList<String> getElemPublicacionesN() {
		return elemPublicacionesN;
	}

	public void setElemPublicacionesN(ArrayList<String> elemPublicacionesN) {
		this.elemPublicacionesN = elemPublicacionesN;
	}

	public List<Investigador> getInvestigadores() {
		return investigadores;
	}

	public void setInvestigadores(List<Investigador> investigadores) {
		this.investigadores = investigadores;
	}

	public ArrayList<String> getUrlSet() {
		return urlSet;
	}

	public void setUrlSet(ArrayList<String> urlSet) {
		this.urlSet = urlSet;
	}

}