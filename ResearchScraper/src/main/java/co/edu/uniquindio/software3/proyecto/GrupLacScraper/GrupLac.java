package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Connection.Response;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import co.edu.uniquindio.software3.proyecto.CvLacScraper.CvLac;
import co.edu.uniquindio.software3.proyecto.CvLacScraper.Investigador;
import co.edu.uniquindio.software3.proyecto.ResearchScraper.ArrayThread;
import co.edu.uniquindio.software3.proyecto.ResearchScraper.Constantes;
import co.edu.uniquindio.software3.proyecto.ResearchScraper.DataSource;

public class GrupLac {

	ArrayList<String> urlSet;

	List<Grupo> grupos = Collections.synchronizedList(new ArrayList<Grupo>());

	ArrayList<PublicacionBibliografica> publicaciones = new ArrayList<>();

	/**
	 * Método provisto por JSoup para comprobar el Status code de la respuesta que
	 * recibo al hacer la petición Codigos: 200 OK 300 Multiple Choices 301 Moved
	 * Permanently 305 Use Proxy 400 Bad Request 403 Forbidden 404 Not Found 500
	 * Internal Server Error 502 Bad Gateway 503 Service Unavailable
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
	 * Método que retorna un objeto de la clase Document con el contenido del HTML
	 * de la web para poder ser parseado posteriormente con JSoup
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
	 * Metodo que realiza la extraccion de la estructura de una pagina web, separa
	 * en las diferentas categorias la estructura de la pagina web y hacer el
	 * llamado al metodo que asigna los datos a cada grupo
	 * 
	 * @param url,
	 *            direccion url de un investigador
	 */
	public void extraer(String url) {

		if (getStatusConnectionCode(url) == 200) {
			String titulo = "";
			ArrayList<String> elemInformacion = new ArrayList<>();

			ArrayList<String> elemEventos = new ArrayList<>();

			ArrayList<String> elemArticulos = new ArrayList<>();

			ArrayList<String> elemLibros = new ArrayList<>();

			ArrayList<String> elemCapLibros = new ArrayList<>();

			ArrayList<String> elemInformes = new ArrayList<>();

			ArrayList<String> elemSoftwares = new ArrayList<>();

			ArrayList<String> elemInnovaciones = new ArrayList<>();

			ArrayList<String> elemProyectos = new ArrayList<>();

			ArrayList<String> elemIntegrantes = new ArrayList<>();
			// Obtengo el HTML de la web en un objeto Document
			Document document = getHtmlDocument(url);
			// Busca todas las coincidencias que estan dentro de
			Elements entradas = document.select("tbody");

			Elements entradas2 = document.select("span.celdaEncabezado");

			for (Element elem : entradas2) {
				if (elem.toString().contains("span class")) {
					titulo = elem.text();

				}
			}

			for (Element elem : entradas) {

				if (elem.text().contains("Datos básicos")) {
					elemInformacion.add(elem.toString());
				}

				if (elem.text().contains("Integrantes del grupo")) {
					elemIntegrantes.add(elem.toString());
				}
				if (elem.text().startsWith("Libros publicados")) {
					elemLibros.add(elem.toString());
				}

				if (elem.text().contains("Artículos publicados")) {
					elemArticulos.add(elem.toString());
				}
				if (elem.text().contains("Eventos Científicos")) {
					elemEventos.add(elem.toString());
				}
				if (elem.text().contains("Capítulos de libro publicados")) {
					elemCapLibros.add(elem.toString());
				}
				if (elem.text().contains("Informes de investigación")) {
					elemInformes.add(elem.toString());
				}
				if (elem.text().contains("Softwares")) {
					elemSoftwares.add(elem.toString());
				}
				if (elem.text().contains("Innovaciones en Procesos y Procedimientos")) {
					elemInnovaciones.add(elem.toString());
				}
				if (elem.toString().contains("<td class=\"celdaEncabezado\" colspan=\"2\">Proyectos</td>")) {
					elemProyectos.add(elem.toString());
				}
			}

			// Obtenemos el id del grupo a partir de la URL
			String id = url.substring(81);

			extraerDatos(limpiar(elemInformacion), limpiar(elemArticulos), limpiar(elemEventos), limpiar(elemInformes),
					limpiar(elemInnovaciones), limpiar(elemLibros), limpiar(elemSoftwares), limpiar(elemProyectos),
					limpiar(elemIntegrantes), titulo, id);

		} else {
			System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(url));
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
			temporal = temporal.replaceAll("&AMP", "&");
			temporal = temporal.replaceAll("&AMP;", "&");
			temporal = temporal.replaceAll("'", "");
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
			if (!aux.get(i).equals(" ")) {
				temporal = "";
				temporal = aux.get(i).substring(1);
				aux2.add(temporal.trim());
			}

		}
		aux.clear();

		for (int i = 0; i < aux2.size(); i++) {
			temporal = aux2.get(i);
			if (!temporal.equals("")) {
				aux.add(temporal.trim().toUpperCase());
			}

		}

		elementosLimpio = aux;
		return elementosLimpio;
	}

	public void extraerDatos(ArrayList<String> datosBasicos, ArrayList<String> articulos, ArrayList<String> eventos,
			ArrayList<String> informes, ArrayList<String> innovaciones, ArrayList<String> libros,
			ArrayList<String> softwares, ArrayList<String> proyectos, ArrayList<String> integrantes, String titulo,
			String id) {
		Grupo grupo = new Grupo();
		try {
			grupo.setId(Integer.parseInt(id));
			grupo.setNombre(titulo);
			extraerIntegrantes(integrantes, grupo);

			extraerInformacionBasica(datosBasicos, grupo);
			extraerArticulos(articulos, grupo);
			extraerEventos(eventos, grupo);
			extraerLibros(libros, grupo);
			extraerProyectos(proyectos, grupo);
			grupos.add(grupo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void extraerIntegrantes(ArrayList<String> elementos, Grupo grupo) {
		String nombre = "";
		ArrayList<String> auxIntegrantes = new ArrayList<>();
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).contains(".-") && elementos.get(i + 3).contains("ACTUAL")) {

				nombre = elementos.get(i + 1);
				auxIntegrantes.add(nombre);
			} else if (elementos.get(i).contains(".-") && elementos.get(i + 4).contains("ACTUAL")) {
				nombre = elementos.get(i + 1);
				auxIntegrantes.add(nombre);
			}
		}
		grupo.setIntegrantes(auxIntegrantes);
	}

	/**
	 * Metodo para la extraccion de la informacion basica del grupo
	 * 
	 * @param arreglo
	 *            lista que contiene los datos a ser extraidos
	 * @param grupo
	 *            atributo de tipo Grupo para almacenar los datos extraidos
	 */
	public void extraerInformacionBasica(ArrayList<String> elementos, Grupo grupo) {
		String anio;
		String lider;
		String clasificacion;
		String area = "";
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).equals("AÑO Y MES DE FORMACIÓN")) {
				anio = elementos.get(i + 1);
				grupo.setAnioFundacion(anio);
			} else if (elementos.get(i).equals("LÍDER")) {
				lider = elementos.get(i + 1);

				grupo.setLider(lider);
			} else if (elementos.get(i).equals("CLASIFICACIÓN")) {
				if (elementos.get(i + 1).equals("ÁREA DE CONOCIMIENTO")) {
					clasificacion = "N/D";
				} else {
					clasificacion = elementos.get(i + 1);
				}
				grupo.setClasificacion(clasificacion);
			} else if (elementos.get(i).equals("ÁREA DE CONOCIMIENTO")) {
				area = elementos.get(i + 1);
				grupo.setAreaDeConocimiento(area);
			}
		}

	}

	/**
	 * Metodo para la extraccion de los eventos pertenecientes a un grupo
	 * 
	 * @param elementos
	 *            arreglo que contiene los eventos
	 */
	public void extraerEventos(ArrayList<String> elementos, Grupo grupo) {
		String nombre = "";
		String tipo = "";
		String ambito = "";
		String fecha = "";
		String lugar = "";
		String tipoParticipacion = "";
		String repetido = "NO";
		ArrayList<EventoCientifico> auxEvento = new ArrayList<>();

		for (int i = 0; i < elementos.size(); i++) {
			EventoCientifico evento = new EventoCientifico();
			if (elementos.get(i).contains(".-")) {
				tipo = elementos.get(i + 1);
			}
			if (elementos.get(i).startsWith(":")) {
				nombre = elementos.get(i).replaceAll("&AMP;", "&");
				nombre = nombre.substring(2);
			}
			if (elementos.get(i).contains("DESDE") && elementos.get(i).contains("HASTA")) {
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				int posF = 0;
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						posF = j;
						lugar = cadena.substring(0, posF);
						if (lugar == null) {
							lugar = "N/D";
						}
						j = posF;
						break;
					}
				}
			}
			if (elementos.get(i).contains("DESDE") && elementos.get(i).contains("HASTA")) {
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				int posI = 0;
				int posF = 0;
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == 'S' && aux[j + 1] == 'D' && aux[j + 2] == 'E' && aux[j + 8] == '-') {
						posI = j + 4;
						posF = j + 8;
						fecha = cadena.substring(posI, posF);
						break;
					}

				}
			}
			if (elementos.get(i).contains("ÁMBITO:")) {
				String cadena = elementos.get(i);
				int posI = 0;
				int posF = 0;
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ':') {
						posI = j + 2;
					}
					if (aux[j] == ',') {
						posF = j;
						if (cadena.substring(posI, posF).equalsIgnoreCase("NULL")) {
							ambito = "N/D";
						} else {
							ambito = cadena.substring(posI, posF);
						}
						break;
					}
				}
			}
			if (elementos.get(i).contains("TIPOS DE PARTICIPACIÓN:")) {
				String cadena = elementos.get(i);
				int posI = 0;
				char aux[] = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == 'N' && aux[j + 1] == ':') {
						try {
							posI = j + 3;
							tipoParticipacion = cadena.substring(posI);
							break;
						} catch (Exception e) {
							tipoParticipacion = "N/D";
							break;
						}

					}
				}
				evento.setNombre(nombre);
				evento.setTipo(tipo);
				evento.setLugar(lugar);
				evento.setFecha(fecha);
				evento.setAmbito(ambito);
				evento.setTipoParticipacion(tipoParticipacion);
				String auxiliar = limpiarCadena(nombre);
				boolean estaRepetido = false;
				for (int j = 0; j < auxEvento.size(); j++) {
					String auxiliar2 = limpiarCadena(auxEvento.get(j).getNombre());
					if (auxiliar.startsWith(auxiliar2) || auxiliar2.startsWith(auxiliar)) {
						auxEvento.get(j).setRepetido("SI");
						estaRepetido = true;
						break;
					}
				}
				if (estaRepetido) {
					repetido = "SI";
				} else {
					repetido = "NO";
				}
				evento.setRepetido(repetido);
				auxEvento.add(evento);
				grupo.setEventos(auxEvento);

			}

		}
	}

	/**
	 * Metodo para la extraccion de los articulos de un grupo
	 * 
	 * @param elementos
	 *            arreglo que contiene lo articuos
	 */
	public void extraerArticulos(ArrayList<String> elementos, Grupo grupo) {
		String autores = "";
		String titulo = "";
		String lugar = "";
		String nomRevista = "";
		String anio = "";
		String tipo = "";
		String repetido = "NO";
		ArrayList<Articulo> auxArticulos = new ArrayList<>();

		for (int i = 0; i < elementos.size(); i++) {
			Articulo articulo = new Articulo();
			if (elementos.get(i).contains(".-")) {
				tipo = elementos.get(i + 1).replaceAll(":", "");
				titulo = elementos.get(i + 2);
				titulo = titulo.replaceAll("&AMP;", "&");
			}
			if (elementos.get(i).contains("ISSN:")) {
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {

						if (j == 0) {
							lugar = "N/D";
						} else {
							lugar = cadena.substring(0, j);
						}
						break;
					}
				}

			}
			if (elementos.get(i).contains("ISSN:")) {
				String cadena = elementos.get(i);
				int posI = 0;
				int posF = 0;
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						posI = j + 2;
					}
					if (aux[j] == 'I' && aux[j + 1] == 'S' && aux[j + 2] == 'S') {
						try {
							posF = j - 1;
							nomRevista = cadena.substring(posI, posF);
							break;
						} catch (Exception e) {
							nomRevista = "N/D";
							break;
						}

					}
				}

			}
			if (elementos.get(i).contains("VOL:")) {
				String cadena = elementos.get(i);
				int posI = 0;
				int posF = 0;
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						posI = j + 2;
					}
					if (aux[j] == 'V' && aux[j + 1] == 'O' && aux[j + 2] == 'L' && aux[j + 3] == ':') {
						posF = j - 1;
						anio = cadena.substring(posI, posF);
						break;
					}
				}

			}
			if (elementos.get(i).contains("AUTORES:")) {
				String cadena = elementos.get(i);
				int posI = 0;
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ':') {
						posI = j + 2;
						autores = cadena.substring(posI, aux.length - 1);
						break;

					}
				}
				articulo.setAnio(anio);
				articulo.setAutores(autores);
				articulo.setLugar(lugar);
				articulo.setNomRevista(nomRevista);
				articulo.setTipo(tipo);
				articulo.setTitulo(titulo);
				String auxiliar = limpiarCadena(titulo);
				boolean estaRepetido = false;
				for (int j = 0; j < auxArticulos.size(); j++) {
					String auxiliar2 = limpiarCadena(auxArticulos.get(j).getTitulo());
					if (auxiliar.startsWith(auxiliar2) || auxiliar2.startsWith(auxiliar)) {
						auxArticulos.get(j).setRepetido("SI");
						estaRepetido = true;
						break;
					}
				}
				if (estaRepetido) {
					repetido = "SI";
				} else {
					repetido = "NO";
				}
				articulo.setRepetido(repetido);
				auxArticulos.add(articulo);
				grupo.setArticulos(auxArticulos);
			}
		}

	}

	/**
	 * 
	 * @param elementos
	 */
	// public void extraerInnovaciones(ArrayList<String> elementos) {
	// String nombre;
	// String lugar;
	// String anio;
	// String financiadora;
	// String autores;
	// ArrayList<String> auxInno = new ArrayList<>();
	// for (int i = 0; i < elementos.size(); i++) {
	// if(elementos.get(i).contains(".-")) {
	// nombre=elementos.get(i+2).substring(2);
	// auxInno.add(nombre);
	// }
	// if(elementos.get(i).contains("DISPONIBILIDAD:")) {
	// int posF=0;
	// String cadena=elementos.get(i);
	// char [] aux = cadena.toCharArray();
	// for (int j = 0; j < aux.length; j++) {
	// if(aux[j]==',') {
	// posF=j;
	// lugar=cadena.substring(0, posF);
	// auxInno.add(lugar);
	// break;
	// }
	// }
	// }
	// if(elementos.get(i).contains("DISPONIBILIDAD:")) {
	// int posI=0;
	// int posF=0;
	// String cadena = elementos.get(i);
	// char [] aux = cadena.toCharArray();
	// for (int j = 0; j < aux.length; j++) {
	// if(aux[j]==',') {
	// posI=j+2;
	// break;
	// }
	//
	// }
	// for (int j2 = posI; j2 < aux.length; j2++) {
	// if(aux[j2]==',') {
	// posF=j2;
	// anio=cadena.substring(posI,posF);
	// auxInno.add(anio);
	// break;
	// }
	// }
	//
	// }
	// if(elementos.get(i).contains("INSTITUCIÓN FINANCIADORA:")) {
	// int posI=0;
	// String cadena=elementos.get(i);
	// char [] aux = cadena.toCharArray();
	// for (int j = 0; j < aux.length; j++) {
	// if(aux[j]=='R'&&aux[j+1]=='A'&&aux[j+2]==':') {
	// posI=j+4;
	// financiadora=cadena.substring(posI);
	// auxInno.add(financiadora);
	// break;
	// }
	// }
	// }
	// if(elementos.get(i).contains("AUTORES:")) {
	// int posI=0;
	// String cadena = elementos.get(i);
	// char [] aux = cadena.toCharArray();
	// for (int j = 0; j < aux.length; j++) {
	// if(aux[j]=='E'&&aux[j+1]=='S'&&aux[j+2]==':') {
	// posI=j+4;
	// autores=cadena.substring(posI);
	// auxInno.add(autores);
	// break;
	// }
	// }
	// }
	// }
	// for (int j = 0; j < auxInno.size(); j++) {
	// System.out.println(auxInno.get(j));
	// }
	//
	// }

	public void extraerLibros(ArrayList<String> elementos, Grupo grupo) {
		String autores = "";
		String titulo = "";
		String lugar = "";
		String anio = "";
		String editorial = "";
		String repetido = "NO";
		ArrayList<Libro> auxLib = new ArrayList<>();

		for (int i = 0; i < elementos.size(); i++) {
			Libro libro = new Libro();
			if (elementos.get(i).contains(".-")) {
				titulo = elementos.get(i + 2).substring(2).replaceAll("&AMP;", "&");
			}
			if (elementos.get(i).contains("ISBN")) {
				int posF = 0;
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						posF = j;
						lugar = cadena.substring(0, posF);
						break;
					}
				}
			}
			if (elementos.get(i).contains("ISBN")) {
				int posI = 0;
				int posF = 0;
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						posI = j + 1;
						break;
					}

				}
				for (int j2 = posI; j2 < aux.length; j2++) {
					if (aux[j2] == ',') {
						posF = j2;
						anio = cadena.substring(posI, posF);
						break;
					}
				}
			}
			if (elementos.get(i).contains("ED.")) {
				int posI = 0;
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				try {
					for (int j = 0; j < aux.length; j++) {
						if (aux[j] == 'E' && aux[j + 1] == 'D' && aux[j + 2] == '.') {
							posI = j + 4;
							editorial = cadena.substring(posI);
							break;
						}
					}

				} catch (Exception e) {
					editorial = "N/D";
				}

			}
			if (elementos.get(i).contains("AUTORES:")) {
				int posI = 0;
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == 'E' && aux[j + 1] == 'S' && aux[j + 2] == ':') {
						posI = j + 4;
						autores = cadena.substring(posI, aux.length - 1);
						libro.setAnio(anio);
						libro.setAutores(autores);
						libro.setEditorial(editorial);
						libro.setLugar(lugar);
						libro.setTitulo(titulo);
						String auxiliar = limpiarCadena(titulo);
						boolean estaRepetido = false;
						for (int k = 0; k < auxLib.size(); k++) {
							String auxiliar2 = limpiarCadena(auxLib.get(k).getTitulo());
							if (auxiliar.startsWith(auxiliar2) || auxiliar2.startsWith(auxiliar)) {
								auxLib.get(k).setRepetido("SI");
								estaRepetido = true;
								break;
							}
						}
						if (estaRepetido) {
							repetido = "SI";
						} else {
							repetido = "NO";
						}
						libro.setRepetido(repetido);
						auxLib.add(libro);
						grupo.setLibros(auxLib);
						break;
					}
				}
			}
		}

	}

	public void extraerProyectos(ArrayList<String> elementos, Grupo grupo) {
		String tipo = "";
		String nombre = "";
		String fecha = "";
		String repetido = "NO";
		ArrayList<Proyecto> auxPro = new ArrayList<>();

		for (int i = 0; i < elementos.size(); i++) {
			Proyecto proyecto = new Proyecto();
			if (elementos.get(i).contains(".-")) {
				tipo = elementos.get(i + 1);
			}
			if (elementos.get(i).contains(".-")) {
				nombre = elementos.get(i + 2).substring(2).replaceAll("&AMP;", "&");
			}
			if (elementos.get(i).contains(".-")) {
				if (elementos.get(i + 3).contains(".-")) {
					fecha = "N/D";
				} else {
					String aux = elementos.get(i + 3);
					fecha = aux.substring(0, 4);
				}
				proyecto.setFecha(fecha);
				proyecto.setNombre(nombre);
				proyecto.setTipo(tipo);
				String auxiliar = limpiarCadena(nombre);
				boolean estaRepetido = false;
				for (int j = 0; j < auxPro.size(); j++) {
					String auxiliar2 = limpiarCadena(auxPro.get(j).getNombre());
					if (auxiliar.startsWith(auxiliar2) || auxiliar2.startsWith(auxiliar)) {
						auxPro.get(j).setRepetido("SI");
						estaRepetido = true;
						break;
					}
				}
				if (estaRepetido) {
					repetido = "SI";
				} else {
					repetido = "NO";
				}
				proyecto.setRepetido(repetido);
				auxPro.add(proyecto);
				grupo.setProyectos(auxPro);
			}

		}

	}

	public void leerDataSet() {
		try {
			urlSet = new ArrayList<String>();
			String cadena;
			FileReader f = new FileReader("src//main//java//datasets//DatasetGrupLac.txt");
			BufferedReader b = new BufferedReader(f);
			while ((cadena = b.readLine()) != null) {
				String url = "http://scienti.colciencias.gov.co:8085/gruplac/jsp/visualiza/visualizagr.jsp?nro="
						+ cadena;
				urlSet.add(url);
			}
			b.close();

		} catch (IOException ex) {
			ex.printStackTrace();

		}
	}

	/**
	 * Este metodo se encarga de hacer el llamado al metodo que lee un archivo plano
	 * y carga el dataSet de url's, ademas, crea y lanza un pool de hilos para
	 * mejorar el tiempo de ejecucion del programa
	 */
	public void scrapData() {

		long startTime = System.currentTimeMillis();
		long stopTime = 0;
		long elapsedTime = 0;
		leerDataSet();
		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 0; i < urlSet.size(); i++) {
			Runnable worker = new ArrayThread(urlSet.get(i), i, 1, null, this);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
			stopTime = System.currentTimeMillis();
			elapsedTime = stopTime - startTime;

		}

		try {
			Connection connection = DataSource.getInstance().getConnection();

			Statement statement = connection.createStatement();
			CvLac cvLac = new CvLac();
			cvLac.scrapData();
			for (int i = 0; i < grupos.size(); i++) {
				Grupo grupo = grupos.get(i);
				String query = Constantes.INSERT_GRUP + "(" + grupo.getId() + ", '" + grupo.getNombre().toUpperCase()
						+ "', '" + grupo.getAnioFundacion().toUpperCase() + "' , '"
						+ grupo.getClasificacion().toUpperCase() + "' , '" + grupo.getLider().toUpperCase() + "' , '"
						+ grupo.getAreaDeConocimiento().toUpperCase() + "')";
				statement.executeQuery(query);

				if (grupo.getIntegrantes() != null) {
					ArrayList<String> listaIntegrantes = grupo.getIntegrantes();
					List<Investigador> listaInvestigadores = cvLac.getInvestigadores();
					for (int j = 0; j < listaIntegrantes.size(); j++) {
						for (int k = 0; k < listaInvestigadores.size(); k++) {
							String auxIntegrante = StringUtils.stripAccents(listaIntegrantes.get(j));
							String auxInvestigador = StringUtils.stripAccents(listaInvestigadores.get(k).getNombre());
							if (auxIntegrante.equalsIgnoreCase(auxInvestigador)) {
								String queryIntermedia = "insert into grup_inves (grupos_id, investigadores_id) values ("
										+ grupo.getId() + "," + listaInvestigadores.get(k).getId() + ")";
								statement.executeQuery(queryIntermedia);
							} else if (auxInvestigador.contains(auxIntegrante)) {
								String queryIntermedia = "insert into grup_inves (grupos_id, investigadores_id) values ("
										+ grupo.getId() + "," + listaInvestigadores.get(k).getId() + ")";
								statement.executeQuery(queryIntermedia);
							}
						}

					}
				}

				if (grupo.getArticulos() != null) {
					ArrayList<Articulo> listaArticulos = grupo.getArticulos();

					for (int j = 0; j < listaArticulos.size(); j++) {
						Articulo a = listaArticulos.get(j);
						String queryArticulos = Constantes.INSERT_GRUP_ART + "('" + a.getAutores() + "','"
								+ a.getTitulo() + "','" + a.getNomRevista() + "','" + a.getLugar() + "','" + a.getAnio()
								+ "','" + a.getTipo() + "'," + grupo.getId() + ",'" + a.getRepetido() + "')";
						statement.executeQuery(queryArticulos);
					}
				}

				if (grupo.getEventos() != null) {
					ArrayList<EventoCientifico> listaEventos = grupo.getEventos();

					for (int j = 0; j < listaEventos.size(); j++) {
						EventoCientifico e = listaEventos.get(j);
						String queryEventos = Constantes.INSERT_GRUP_EVT + "('" + e.getNombre() + "','" + e.getTipo()
								+ "','" + e.getAmbito() + "','" + e.getLugar() + "','" + e.getFecha() + "','"
								+ e.getTipoParticipacion() + "'," + grupo.getId() + ",'" + e.getRepetido() + "')";
						statement.executeQuery(queryEventos);
					}
				}

				if (grupo.getLibros() != null) {
					ArrayList<Libro> listaLibros = grupo.getLibros();

					for (int j = 0; j < listaLibros.size(); j++) {
						Libro l = listaLibros.get(j);
						String queryLibros = Constantes.INSERT_GRUP_LIB + "('" + l.getTitulo() + "','" + l.getAutores()
								+ "','" + l.getLugar() + "','" + l.getAnio() + "','" + l.getEditorial() + "',"
								+ grupo.getId() + ",'" + l.getRepetido() + "')";
						statement.executeQuery(queryLibros);
					}

				}

				if (grupo.getProyectos() != null) {
					ArrayList<Proyecto> listaProyecto = grupo.getProyectos();

					for (int j = 0; j < listaProyecto.size(); j++) {
						Proyecto p = listaProyecto.get(j);
						String queryProyectos = Constantes.INSERT_GRUP_PROY + "('" + p.getNombre() + "','" + p.getTipo()
								+ "','" + p.getFecha() + "'," + grupo.getId() + ",'" + p.getRepetido() + "')";
						statement.executeQuery(queryProyectos);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.err.println(elapsedTime);

	}

	public String limpiarCadena(String cadena) {
		String aux = cadena;
		aux = aux.replaceAll(" ", "");
		aux = aux.replaceAll("&", "Y");
		aux = aux.replaceAll(":", "");
		aux = aux.replaceAll(",", "");
		aux = aux.replaceAll("-", "");
		aux = aux.replaceAll(";", "");
		aux = aux.replaceAll("¿", "");
		aux = aux.replaceAll("¡", "");
		aux = aux.replaceAll("!", "");
		String auxiliar = StringUtils.stripAccents(aux);

		return auxiliar;
	}

	/**
	 * Método que extrae la referencia biliográfica completa.
	 */
	public void extraerReferencia(String url) {
		if (getStatusConnectionCode(url) == 200) {

			ArrayList<String> elemArticulos = new ArrayList<>();

			ArrayList<String> elemLibros = new ArrayList<>();

			ArrayList<String> elemCapLibros = new ArrayList<>();

			ArrayList<String> elemOtrosRes = new ArrayList<>();

			ArrayList<String> elemOtrosArticulos = new ArrayList<>();

			ArrayList<String> elemOtrosLibros = new ArrayList<>();

			ArrayList<String> elemTrabajos = new ArrayList<>();

			Document document = getHtmlDocument(url);
			// Busca todas las coincidencias que estan dentro de
			Elements entradas = document.select("tbody");

			for (Element elem : entradas) {

				if (elem.text().contains("Artículos publicados")) {
					elemArticulos.add(elem.toString());
					elemArticulos = limpiar(elemArticulos);
				}

				if (elem.text().startsWith("Libros publicados")) {
					elemLibros.add(elem.toString());
					elemLibros = limpiar(elemLibros);
				}

				if (elem.text().contains("Capítulos de libro publicados")) {
					elemCapLibros.add(elem.toString());
					elemCapLibros = limpiar(elemCapLibros);
				}

				if (elem.text().contains("Documentos de trabajo")) {
					ArrayList<String> aux = new ArrayList<>();
					aux.add(elem.toString());
					aux = limpiar(aux);
					elemOtrosRes.addAll(aux);
				}
				if (elem.text().contains("Otra publicación divulgativa")) {
					ArrayList<String> aux = new ArrayList<>();
					aux.add(elem.toString());
					aux = limpiar(aux);
					elemOtrosRes.addAll(aux);
				}

				if (elem.text().startsWith("Otros artículos publicados")) {
					elemOtrosArticulos.add(elem.toString());
					elemOtrosArticulos = limpiar(elemOtrosArticulos);
				}
				if (elem.text().contains("Otros Libros publicados")) {
					elemOtrosLibros.add(elem.toString());
					elemOtrosLibros = limpiar(elemOtrosLibros);
				}
				if (elem.text().contains("Trabajos dirigidos/turorías")) {
					elemTrabajos.add(elem.toString());
					elemTrabajos = limpiar(elemTrabajos);
				}
			}
			 referenciaArticulos(elemArticulos);
			 referenciaArticulos(elemOtrosArticulos);
			referenciaLibros(elemLibros);
			referenciaLibros(elemOtrosLibros);
			 referenciaCapitulosLibros(elemCapLibros);
			 referenciaTrabajosGrado(elemTrabajos);

			 try {
			 Connection connection = DataSource.getInstance().getConnection();
			 Statement statement = connection.createStatement();
			 for (int i = 0; i < publicaciones.size(); i++) {
			 PublicacionBibliografica p = publicaciones.get(i);
			 String query = Constantes.INSERT_REPORTE + "('" + p.getTipo().toUpperCase() +
			 "', '"
			 + p.getIdentificador().toUpperCase() + "' , '" + p.getAutores().toUpperCase()
			 + "' , '"
			 + p.getAnio().toUpperCase() + "' , '" + p.getReferencia().toUpperCase() +
			 "')";
			 statement.executeQuery(query);
			 }
			
			 } catch (Exception e) {
			 e.printStackTrace();
			 }
		} else {
			System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(url));
		}

	}

	public void referenciaCapitulosLibros(ArrayList<String> elementos) {

		String autores = "";
		String cadena = "";
		String anio = "";

		for (int i = 0; i < elementos.size(); i++) {
			PublicacionBibliografica publicacionBibliografica = new PublicacionBibliografica();
			if (elementos.get(i).contains("CAPÍTULO DE LIBRO")) {
				cadena = "";
				anio = elementos.get(i + 2);
				char[] aux = anio.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						anio = anio.substring(j + 2, j + 6);
						break;
					}
				}

				i++;
				while (!elementos.get(i).contains("AUTORES:")) {
					cadena += elementos.get(i);
					cadena += ", ";
					i++;

				}
				cadena = cadena.substring(2, cadena.length() - 2);
			}

			if (elementos.get(i).contains("AUTORES:")) {
				autores = elementos.get(i);
				autores = autores.substring(9, autores.length() - 1);
				publicacionBibliografica.setIdentificador("C. Lb.");
				publicacionBibliografica.setReferencia(cadena);
				publicacionBibliografica.setAnio(anio);
				publicacionBibliografica.setAutores(autores);
				publicacionBibliografica.setTipo("CAPITULO DE LIBRO");
				publicaciones.add(publicacionBibliografica);
			}
		}

	}

	public void referenciaTrabajosGrado(ArrayList<String> elementos) {

		String autores = "";
		String cadena = "";
		String anio = "";
		String identificador = "";

		for (int i = 0; i < elementos.size(); i++) {
			PublicacionBibliografica publicacionBibliografica = new PublicacionBibliografica();
			if (elementos.get(i).contains("TRABAJOS DE GRADO DE PREGRADO")
					|| elementos.get(i).contains("TRABAJO DE GRADO DE MAESTRÍA")
					|| elementos.get(i).contains("TESIS DE DOCTORADO")) {
				if (elementos.get(i).contains("TRABAJOS DE GRADO DE PREGRADO")) {
					identificador = "T.Grado-P";
				}
				if (elementos.get(i).contains("TRABAJO DE GRADO DE MAESTRÍA")) {
					identificador = "T.Grado-M";
				}
				if (elementos.get(i).contains("TESIS DE DOCTORADO")) {
					identificador = "T.Grado-D";
				}

				cadena = "";
				anio = elementos.get(i + 2);
				char[] aux = anio.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == 'H' && aux[j + 1] == 'A' && aux[j + 2] == 'S') {
						anio = anio.substring(j - 5, j - 1);
						break;
					}
				}

				i++;
				while (!elementos.get(i).contains("AUTORES:")) {
					cadena += elementos.get(i);
					cadena += ", ";
					i++;

				}
				cadena = cadena.substring(2, cadena.length() - 2);
				if (elementos.get(i).contains("AUTORES:")) {
					autores = elementos.get(i);
					autores = autores.substring(9, autores.length() - 1);

					publicacionBibliografica.setIdentificador(identificador);
					publicacionBibliografica.setReferencia(cadena);
					publicacionBibliografica.setAnio(anio);
					publicacionBibliografica.setAutores(autores);
					publicacionBibliografica.setTipo("TRABAJO GRADO");
					publicaciones.add(publicacionBibliografica);
				}
			}

			
		}

	}

	public void referenciaLibros(ArrayList<String> elementos) {
		String identificador = "";
		String autores = "";
		String anio = "";
		String referencia = "";
		String tipo = "";
		for (int i = 0; i < elementos.size(); i++) {
			PublicacionBibliografica publicacionBibliografica = new PublicacionBibliografica();
			if (elementos.get(i).contains(".-")) {
				i++;
				if (elementos.get(i).contains("LIBRO RESULTADO DE INVESTIGACIÓN")) {

					identificador = "Lb.";
					tipo = "LIBRO";
					anio = elementos.get(i + 2);
					char[] aux = anio.toCharArray();
					for (int j = 0; j < aux.length; j++) {
						if (aux[j] == ',') {
							anio = anio.substring(j + 1, j + 5);
							break;
						}
					}
					referencia = "";
					i++;
					while (!elementos.get(i).contains("AUTORES:")) {
						referencia += elementos.get(i);
						referencia += ", ";
						i++;

					}
					referencia = referencia.substring(2, referencia.length() - 2);
				}
				if (elementos.get(i).contains("OTRO LIBRO PUBLICADO")) {

					identificador = "Lb.";
					tipo = "OTROLIBRO";
					anio = elementos.get(i + 2);
					char[] aux = anio.toCharArray();
					for (int j = 0; j < aux.length; j++) {
						if (aux[j] == ',') {
							anio = anio.substring(j + 1, j + 5);
							break;
						}
					}
					referencia = "";
					i++;
					while (!elementos.get(i).contains("AUTORES:")) {
						referencia += elementos.get(i);
						referencia += ", ";
						i++;

					}
					referencia = referencia.substring(2, referencia.length() - 2);
				}
			}

			if (elementos.get(i).contains("AUTORES:")) {
				autores = elementos.get(i);
				autores = autores.substring(9, autores.length() - 1);


				 publicacionBibliografica.setIdentificador(identificador);
				 publicacionBibliografica.setReferencia(referencia);
				 publicacionBibliografica.setAnio(anio);
				 publicacionBibliografica.setAutores(autores);
				 publicacionBibliografica.setTipo(tipo);
				 publicaciones.add(publicacionBibliografica);
			}
		}
	}

	public void referenciaArticulos(ArrayList<String> elementos) {
		String autores = "";
		String referencia = "";
		String anio = "";
		String issn = "";
		String tipo = "";
		for (int i = 0; i < elementos.size(); i++) {
			PublicacionBibliografica publicacionBibliografica = new PublicacionBibliografica();

			if (elementos.get(i).contains(".-")) {
				if (elementos.get(i + 1).contains("PUBLICADO EN REVISTA ESPECIALIZADA:")) {
					tipo = "ARTICULO";
				} else {
					tipo = "OTROART";
				}

				referencia = "";
				for (int j = i + 2; j < elementos.size(); j++) {
					if (!elementos.get(j).contains("AUTORES:")) {
						referencia += " " + elementos.get(j);
					} else {
						break;
					}
				}

				if (referencia.endsWith("DOI:")) {
					referencia = referencia.substring(1, referencia.length() - 5);
				}

				if (referencia.startsWith(" : ")) {
					referencia = referencia.substring(3);
				}

			}

			if (elementos.get(i).contains("ISSN")) {
				char[] aux = elementos.get(i).toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == 'I' && aux[j + 1] == 'S' && aux[j + 2] == 'S') {
						int posI = j + 6;
						int posF = posI;
						for (int k = posI; k < aux.length; k++) {
							if (aux[k] == ',') {
								posF = k;
								break;
							}
						}
						issn = elementos.get(i).substring(posI, posF);
						anio = elementos.get(i).substring(posF + 2, posF + 6);
						break;
					}
				}

			}

			if (elementos.get(i).contains("AUTORES:")) {
				autores = elementos.get(i).substring(9, elementos.get(i).length() - 1);
				publicacionBibliografica.setAnio(anio);
				publicacionBibliografica.setAutores(autores);
				publicacionBibliografica.setIdentificador(issn);
				publicacionBibliografica.setReferencia(referencia);
				publicacionBibliografica.setTipo(tipo);
				publicaciones.add(publicacionBibliografica);

			}

		}
	}
}
