package co.edu.uniquindio.software3.proyecto.CvLacScraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
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
import co.edu.uniquindio.software3.proyecto.ResearchScraper.BdAccess;
import co.edu.uniquindio.software3.proyecto.ResearchScraper.Constantes;

public class CvLac {

	///////////////////////////////////////////

	// Guardar informacion en uppercase y limpio;

	//////////////////////////////////////

	// Lista en la que se guardan las direcciones url de cada investigador
	public ArrayList<String> urlSet;

	// Lista sincronizada en la que se guardan todos los investidagores junto a
	// su respectiva informacion
	public List<Investigador> investigadores = Collections.synchronizedList(new ArrayList<Investigador>());

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
		System.err.println(urlSet.size());
		for (int i = 0; i < urlSet.size(); i++) {
			Runnable worker = new ArrayThread(urlSet.get(i), i, 0, this, null);
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
	 * Metodo que realiza la extraccion de la estructura de una pagina web,
	 * separa en las diferentas categorias la estructura de la pagina web y
	 * hacer el llamado al metodo que asigna los datos a cada investigador
	 * 
	 * @param url,
	 *            direccion url de un investigador
	 */
	public void extraer(String url) {

		if (getStatusConnectionCode(url) == 200) {
			// Lista en la que se guarda la informacion personal de cada
			// invsestigador
			ArrayList<String> elemInfoPersonal = new ArrayList<>();

			// Lista en la que se guardan la formacion academica de cada
			// invsestigador
			ArrayList<String> elemFormacionAcam = new ArrayList<>();

			// Lista en la que se guardan los eventos en los que ha participado
			// de cada
			// invsestigador
			ArrayList<String> elemEventos = new ArrayList<>();

			// Lista en la que se guardan los articulos que ha escrito de cada
			// invsestigador
			ArrayList<String> elemArticulos = new ArrayList<>();

			// Lista en la que se guardan los libros que ha escrito de cada
			// invsestigador
			ArrayList<String> elemLibros = new ArrayList<>();

			// Lista en la que se guardan los informes de investigación que ha
			// escrito
			// de cada invsestigador
			ArrayList<String> elemInformes = new ArrayList<>();

			// Lista en la que se guardan los proyectos en los que ha
			// participado de
			// cada invsestigador
			ArrayList<String> elemProyectos = new ArrayList<>();

			// Lista en la que se guardan las publicaciones en revistas no
			// especializadas que ha realizado de cada invsestigador
			ArrayList<String> elemPublicacionesN = new ArrayList<>();

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
		if (datosPersonales.size() == 0) {
			investigador.setNombre("PERFIL PRIVADO");
			investigador.setCategoria("N/D");
			investigador.setFormacion("N/D");
			ArrayList<EventoCientifico> eventos1 = new ArrayList<>();
			ArrayList<Articulo> articulos1 = new ArrayList<>();
			ArrayList<Libro> libros1 = new ArrayList<>();
			ArrayList<Proyecto> proyectos1 = new ArrayList<>();

			investigador.setArticulos(articulos1);
			investigador.setEventos(eventos1);
			investigador.setProyectos(proyectos1);
			investigador.setLibros(libros1);
			investigadores.add(investigador);
			System.out.println(investigador.getNombre());
			String aux = Constantes.INSERT_INVES + "('" + investigador.getNombre().toUpperCase() + "', '"
					+ investigador.getCategoria().toUpperCase() + "' , '" + investigador.getFormacion().toUpperCase()
					+ "')";
			BdAccess bdA = new BdAccess();
			bdA.conexion(aux);
		} else {
			try {
				for (int i = 0; i < datosPersonales.size(); i++) {
					if (datosPersonales.get(i).contains("CATEGORÍA")) {
						String registro = datosPersonales.get(i + 1);
						char[] aux = registro.toCharArray();
						String categoria = "";
						for (int j = 0; j < aux.length; j++) {
							if (aux[j] == '(') {
								categoria = registro.substring(0, j - 1);
								investigador.setCategoria(categoria);
								break;
							}
						}
						// investigador.setCategoria(datosPersonales.get(i +
						// 1));
					}
					if (datosPersonales.get(i).equalsIgnoreCase("NOMBRE")) {
						investigador.setNombre(datosPersonales.get(i + 1));
					}
				}

				if (investigador.getCategoria() == null) {
					investigador.setCategoria("N/D");
				}
			} catch (NullPointerException e) {

			}

			try {
				extraerFormacionAcademica(formacion, investigador);
				if (investigador.getFormacion() == null) {
					investigador.setFormacion("N/D");
				}
			} catch (Exception e) {

			}

			try {
				extraerEventos(eventos, investigador);
			} catch (Exception e) {

			}
			try {
				extraerArticulos(articulos, investigador);
			} catch (Exception e) {
			}
			try {
				extraerLibros(libros, investigador);
			} catch (Exception e) {

			}
			try {
				extraerProyectos(proyectos, investigador);
			} catch (Exception e) {

			}

			try {
				investigadores.add(investigador);
				System.out.println(investigador.getNombre());
				String aux = Constantes.INSERT_INVES + "('" + investigador.getNombre().toUpperCase() + "', '"
						+ investigador.getCategoria().toUpperCase() + "' , '"
						+ investigador.getFormacion().toUpperCase() + "')";
				BdAccess bdA = new BdAccess();
				bdA.conexion(aux);

			} catch (Exception e) {
				System.err.println("Sin articulos");
			}
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
			if (elementos.get(i).equalsIgnoreCase("Formación Académica")) {
				investigador.setFormacion(elementos.get(i + 1));
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
		ArrayList<EventoCientifico> eventoAux = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			EventoCientifico eventos = new EventoCientifico();
			if (elementos.get(i).contains("NOMBRE DEL EVENTO:")) {
				nombre = elementos.get(i + 1);
			}
			if (elementos.get(i).contains("TIPO DE EVENTO:")) {
				tipo = elementos.get(i + 1);
			}
			if (elementos.get(i).contains("ÁMBITO:")) {
				ambito = elementos.get(i + 1);
			}
			if (elementos.get(i).contains("REALIZADO EL:")) {
				String cadena = elementos.get(i);
				fecha = cadena.substring(13, 17);
				if (fecha.contains(",")) {
					fecha = "NO REGISTRA";
				}

				char[] auxiliar = cadena.toCharArray();
				int posI = 0;
				int posF = 0;
				for (int j = 0; j < auxiliar.length; j++) {
					if (auxiliar[j] == 'E' && auxiliar[j + 1] == 'N' && auxiliar[j + 2] == ' ') {
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
			if (aux2.equalsIgnoreCase(aux) && elementos.get(i + 1).contains("ROL EN EL EVENTO:")) {
				rol = elementos.get(i + 2);
				eventos.setNombre(nombre);
				eventos.setTipo(tipo);
				eventos.setAmbito(ambito);
				eventos.setFecha(fecha);
				eventos.setLugar(lugar);
				eventos.setRol(rol);
				eventoAux.add(eventos);
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
		ArrayList<Articulo> articuloAux = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			Articulo articulo = new Articulo();
			if (elementos.get(i).contains("PUBLICADO EN REVISTA ESPECIALIZADA")) {
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
						} else if (auxiliar[j] == ':' && j+1<auxiliar.length) {
							posI = j + 1;
							lugar = cadena.substring(posI + 1);
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
			if (elementos.get(i).contains("ED:")) {
				nomRevista += " Editorial: " + elementos.get(i + 1);
			}
			
		
			if (elementos.get(i).contains("FASC.")) {
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
				System.out.println(titulo);
				articuloAux.add(articulo);
				esEspecializada = false;
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
		ArrayList<Libro> libroAux = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			Libro libro = new Libro();
			aux2 = StringUtils.stripAccents(elementos.get(i));
			if (aux2.toUpperCase().contains(aux.toUpperCase())) {
				String cadena = elementos.get(i);
				char[] auxiliar = cadena.toCharArray();
				for (int j = 0; j < auxiliar.length; j++) {
					int posI = 0;
					int posF = 0;

					if (auxiliar[j] != ' ') {
						if (auxiliar[j] == '"') {
							posI = j + 1;
							for (int k = posI + 1; k < auxiliar.length; k++) {
								if (auxiliar[k] == '"') {
									posF = k;
									titulo = cadena.substring(posI, posF);
									j = k + 1;
									break;
								}
							}
						} else if (auxiliar[j] == 'E' && auxiliar[j + 1] == 'N' && auxiliar[j + 2] == ':') {
							posI = j + 4;
							for (int l = posI; l < auxiliar.length; l++) {
								if (auxiliar[l] == '1' || auxiliar[l] == '2') {
									posF = l - 1;
									lugar = cadena.substring(posI, posF);
									j = l - 1;
									break;
								}

							}

						} else if ((auxiliar[j] == '1' || auxiliar[j] == '2')) {

							posI = j;

							for (int m = posI; m < auxiliar.length; m++) {
								if (auxiliar[m] == 'E' && auxiliar[m + 1] == 'D' && auxiliar[m + 2] == ':') {
									posF = m - 3;
									anio = cadena.substring(posI, posF);
									j = m - 1;
									break;
								}
							}

						} else if (auxiliar[j] == 'E' && auxiliar[j + 1] == 'D' && auxiliar[j + 2] == ':') {
							posI = j + 3;
							posF = cadena.length();
							editorial = cadena.substring(posI, posF);
							j = posF;
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
			if (elementos.get(i).contains("TIPO DE PROYECTO:")) {
				tipo = elementos.get(i + 1);
				nombre = elementos.get(i + 2);
			}
			if (elementos.get(i).contains("INICIO:")) {
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
		String temporal = "";
		ArrayList<String> elementosLimpio = new ArrayList<>();
		ArrayList<String> aux = new ArrayList<>();
		ArrayList<String> aux2 = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			temporal = elementos.get(i).replaceAll("\n", "");
			temporal = temporal.replaceAll("&nbsp;", " ");
			temporal = temporal.replaceAll("  ", " ");
			temporal = temporal.replaceAll("&AMP;", "&");
		}
		char[] auxiliar = temporal.toCharArray();
		int posI = 0;
		int posF = 0;
		for (int j = 0; j < auxiliar.length; j++) {
			if (auxiliar[j] == '>') {
				posI = j;
				for (int i = j; i < auxiliar.length; i++) {
					if (auxiliar[i] == '<') {
						posF = i;
						elementosLimpio.add(temporal.substring(posI, posF));
						j = i;
						break;
					}
				}
			}
		}
		for (int i = 0; i < elementosLimpio.size(); i++) {
			temporal = "";
			temporal = elementosLimpio.get(i).replaceAll(">", " ");
			aux.add(temporal);

		}
		for (int i = 0; i < aux.size(); i++) {
			if (!aux.get(i).equalsIgnoreCase(" ")) {
				temporal = "";
				temporal = aux.get(i).substring(1);
				aux2.add(temporal.trim());
			}

		}
		aux.clear();

		for (int i = 0; i < aux2.size(); i++) {
			temporal = aux2.get(i);
			if (!temporal.equalsIgnoreCase("")) {
				aux.add(temporal.trim().toUpperCase());
			}

		}

		elementosLimpio = aux;
		// for (int i = 0; i < elementosLimpio.size(); i++) {
		// System.out.println(elementosLimpio.get(i));
		// }
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
			FileReader f = new FileReader("src//main//java//datasets//DatasetCvLac.txt");
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

	/**
	 * 
	 */
	public void llenarBD() {

		BdAccess bdA = new BdAccess();
		String aux;
		System.out.println(investigadores.size());
		for (int i = 0; i < investigadores.size(); i++) {
			aux = Constantes.INSERT_INVES + "('" + investigadores.get(i).getNombre().toUpperCase() + "')";
			// System.out.println(i+1+
			// investigadores.get(i).getNombre().toUpperCase());
			bdA.conexion(aux);

		}
	}

	// public ArrayList<String> getElemInfoPersonal() {
	// return elemInfoPersonal;
	// }
	//
	// public void setElemInfoPersonal(ArrayList<String> elemInfoPersonal) {
	// this.elemInfoPersonal = elemInfoPersonal;
	// }
	//
	// public ArrayList<String> getElemFormacionAcam() {
	// return elemFormacionAcam;
	// }
	//
	// public void setElemFormacionAcam(ArrayList<String> elemFormacionAcam) {
	// this.elemFormacionAcam = elemFormacionAcam;
	// }
	//
	// public ArrayList<String> getElemEventos() {
	// return elemEventos;
	// }
	//
	// public void setElemEventos(ArrayList<String> elemEventos) {
	// this.elemEventos = elemEventos;
	// }
	//
	// public ArrayList<String> getElemArticulos() {
	// return elemArticulos;
	// }
	//
	// public void setElemArticulos(ArrayList<String> elemArticulos) {
	// this.elemArticulos = elemArticulos;
	// }
	//
	// public ArrayList<String> getElemLibros() {
	// return elemLibros;
	// }
	//
	// public void setElemLibros(ArrayList<String> elemLibros) {
	// this.elemLibros = elemLibros;
	// }
	//
	// public ArrayList<String> getElemInformes() {
	// return elemInformes;
	// }
	//
	// public void setElemInformes(ArrayList<String> elemInformes) {
	// this.elemInformes = elemInformes;
	// }
	//
	// public ArrayList<String> getElemProyectos() {
	// return elemProyectos;
	// }
	//
	// public void setElemProyectos(ArrayList<String> elemProyectos) {
	// this.elemProyectos = elemProyectos;
	// }
	//
	// public ArrayList<String> getElemPublicacionesN() {
	// return elemPublicacionesN;
	// }
	//
	// public void setElemPublicacionesN(ArrayList<String> elemPublicacionesN) {
	// this.elemPublicacionesN = elemPublicacionesN;
	// }
	//
	// public List<Investigador> getInvestigadores() {
	// return investigadores;
	// }
	//
	// public void setInvestigadores(List<Investigador> investigadores) {
	// this.investigadores = investigadores;
	// }
	//
	// public ArrayList<String> getUrlSet() {
	// return urlSet;
	// }
	//
	// public void setUrlSet(ArrayList<String> urlSet) {
	// this.urlSet = urlSet;
	// }

}