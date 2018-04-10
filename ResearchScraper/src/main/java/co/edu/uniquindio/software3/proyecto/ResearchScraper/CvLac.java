package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CvLac {

	public final String url = "http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0000438383";
	public ArrayList<String> elemInfoPersonal = new ArrayList<>();
	public ArrayList<String> elemFormacionAcam = new ArrayList<>();
	public ArrayList<String> elemEventos = new ArrayList<>();
	public ArrayList<String> elemArticulos = new ArrayList<>();
	public ArrayList<String> elemLibros = new ArrayList<>();
	public ArrayList<String> elemInformes = new ArrayList<>();
	public ArrayList<String> elemProyectos = new ArrayList<>();
	public ArrayList<String> elemPublicacionesN = new ArrayList<>();

	public void extraer() {
		long startTime = System.currentTimeMillis();
		if (getStatusConnectionCode(url) == 200) {
			// Obtengo el HTML de la web en un objeto Document
			Document document = getHtmlDocument(url);
			// Busco todas las entradas que estan dentro de:
			Elements entradas = document.select("tbody>tr>td>table>tbody");
			System.out.println("a encontrados: " + entradas.size() + "\n");
			String nombre = "";
			// Paseo cada una de las entradas
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

			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter("d://test.txt"));
				// writer = new BufferedWriter(new
				// FileWriter("C://Users//dan97//Desktop/test.txt"));
				writer.write(nombre);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// extraerDatos(limpiar(elemInfoPersonal), null, null,
			// limpiar(elemArticulos), null, null, null, null);
			// extraerDatos(limpiar(elemInfoPersonal),limpiar(elemFormacionAcam),
			// limpiar(elemEventos), limpiar(elemArticulos),
			// limpiar(elemLibros),limpiar(elemInformes),limpiar(elemProyectos),limpiar(elemPublicacionesN));

			limpiar(elemInformes);

		} else {
			System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(url));
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);

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
	}

	public void extraerFormacionAcademica(ArrayList<String> elementos, Investigador investigador) {
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).equals("Formación Académica")) {
				investigador.setFormacion(elementos.get(i + 6));
			}
		}
	}

	public void extraerEventos(ArrayList<String> elementos, Investigador investigador) {
		String nombre = "";
		String tipo = "";
		String ambito = "";
		String fecha = "";
		String lugar = "";
		String rol = "";
		String aux = "";
		aux = StringUtils.stripAccents(investigador.getNombre());
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
			if (elementos.get(i).equalsIgnoreCase(aux) && elementos.get(i + 1).contains("Rol en el evento:")) {
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
		// for (int j = 0; j < investigador.getEventos().size(); j++) {
		// System.out.println((j+1)+":
		// "+investigador.getEventos().get(j).getNombre());
		// System.out.println(investigador.getEventos().get(j).getFecha());
		// }
	}

	public void extraerArticulos(ArrayList<String> elementos, Investigador investigador) {
		boolean esEspecializada = false;
		String autores = "";
		String titulo = "";
		String lugar = "";
		String nomRevista = "";
		String anio = "";
		String aux = "";
		aux = StringUtils.stripAccents(investigador.getNombre());
		aux = aux.substring(1, aux.length() - 1);
		ArrayList<Articulo> articuloAux = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			Articulo articulo = new Articulo();
			if (elementos.get(i).contains("Publicado en revista especializada")) {
				esEspecializada = true;
			}
			if (elementos.get(i).contains(aux.toUpperCase())) {
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
		// for (int j = 0; j < investigador.getArticulos().size(); j++) {
		// System.out.println((j + 1) + ": " +
		// investigador.getArticulos().get(j).getTitulo());
		// System.out.println(investigador.getArticulos().get(j).getAnio());
		// }
	}

	public void extraerLibros(ArrayList<String> elementos, Investigador investigador) {
		String autores = "";
		String titulo = "";
		String lugar = "";
		String anio = "";
		String editorial = "";
		String aux = "";
		aux = StringUtils.stripAccents(investigador.getNombre());
		aux = aux.substring(1, aux.length() - 1);
		ArrayList<Libro> libroAux = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			Libro libro = new Libro();
			if (elementos.get(i).contains(aux.toUpperCase())) {
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
	 * 
	 * @param elementos
	 * @param investigador
	 */
//	public void extraerInforme(ArrayList<String> elementos, Investigador investigador) {
//		String autores = "";
//		String titulo = "";
//		String lugar = "";
//		String anio = "";
//		String aux = "";
//		aux = StringUtils.stripAccents(investigador.getNombre());
//		aux = aux.substring(1, aux.length() - 1);
//		ArrayList<InformeInvestigacion> informeAux = new ArrayList<>();
//		for (int i = 0; i < elementos.size(); i++) {
//			InformeInvestigacion informe = new InformeInvestigacion();
//
//			if (elementos.get(i).contains(aux.toUpperCase())) {
//				String cadena = elementos.get(i);
//				char[] auxiliar = cadena.toCharArray();
//				int posI = 0;
//				int posF = 0;
//				for (int j = 0; j < auxiliar.length; j++) {
//					if ((esMayuscula(auxiliar[j]) && esMinuscula(auxiliar[j + 1]))
//							|| (esMayuscula(auxiliar[j]) && esMinuscula(auxiliar[j + 3]))) {
//						if (auxiliar[j] == '"') {
//							posI = j + 1;
//							for (int k = posI + 1; k < auxiliar.length; k++) {
//								if (auxiliar[k] == '"') {
//									posF = k;
//									titulo = cadena.substring(posI, posF);
//									j = k;
//									break;
//								}
//							}
//						} else if (auxiliar[j] == ':') {
//							posI = j + 1;
//							lugar = cadena.substring(posI);
//						} else {
//							posI = j;
//							for (int k = posI; k < auxiliar.length; k++) {
//								if (auxiliar[k] == '"') {
//									posF = k - 2;
//									autores = cadena.substring(posI, posF);
//									j = k - 1;
//									break;
//								}
//							}
//						}
//					}
//
//				}
//				nomRevista = elementos.get(i + 1);
//			}
//			if (elementos.get(i).contains("ed:")) {
//				nomRevista += " Editorial: " + elementos.get(i + 1);
//			}
//			if (elementos.get(i).contains("fasc.")) {
//				String cadena = elementos.get(i + 1);
//				char[] auxiliar = cadena.toCharArray();
//				int posI = 0;
//				int posF = 0;
//				for (int j = 0; j < auxiliar.length; j++) {
//					if (auxiliar[j] == ',') {
//						posI = j + 1;
//						for (int k = posI; k < auxiliar.length; k++) {
//							if (auxiliar[k] == ',') {
//								posF = k;
//								anio = cadena.substring(posI, posF);
//								j = k;
//								break;
//							}
//						}
//					}
//				}
//				articulo.setEsEspecializada(esEspecializada);
//				articulo.setTitulo(titulo);
//				articulo.setAnio(anio);
//				articulo.setAutores(autores);
//				articulo.setLugar(lugar);
//				articulo.setNomRevista(nomRevista);
//				articuloAux.add(articulo);
//				investigador.setArticulos(articuloAux);
//			}
//
//		}
//		// for (int j = 0; j < investigador.getArticulos().size(); j++) {
//		// System.out.println((j + 1) + ": " +
//		// investigador.getArticulos().get(j).getTitulo());
//		// System.out.println(investigador.getArticulos().get(j).getAnio());
//		// }
//	}

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
		for (int i = 0; i < elementosLimpio.size(); i++) {
			System.out.println(elementosLimpio.get(i));
		}
		return elementosLimpio;
	}

	public boolean esMayuscula(char a) {
		if (a > 40 && a < 91) {
			return true;
		} else {
			return false;
		}
	}

	public boolean esMinuscula(char a) {
		if (a > 60 && a < 123) {
			return true;
		} else {
			return false;
		}
	}

}
