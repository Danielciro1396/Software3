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
	// public ArrayList<String> elementos = new ArrayList<String>();
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
					// nombre += elem.toString() + "\n";
					elemInfoPersonal.add(elem.toString());
				}
				if (elem.text().contains("Formación Académica")) {
					// nombre += elem.toString() + "\n";
					elemFormacionAcam.add(elem.toString());
				}
				if (elem.text().contains("Eventos científicos")) {
					// nombre += elem.toString() + "\n";
					elemEventos.add(elem.toString());
				}
				if (elem.text().contains("Artículos")) {
					// nombre += elem.toString() + "\n";
					elemArticulos.add(elem.toString());
				}
				if (elem.text().contains("Libros")) {
					// nombre += elem.toString() + "\n";
					elemLibros.add(elem.toString());
				}
				if (elem.text().contains("Informes de investigación")) {
					// nombre += elem.toString() + "\n";
					elemInformes.add(elem.toString());
				}
				if (elem.text().contains("Proyectos")) {
					// nombre += elem.toString() + "\n";
					elemProyectos.add(elem.toString());
				}
				if (elem.text().contains("Textos en publicaciones no científicas")) {
					// nombre += elem.toString() + "\n";
					elemPublicacionesN.add(elem.toString());
				}

			}

			BufferedWriter writer = null;
			try {
				// writer = new BufferedWriter(new FileWriter("d://test.txt"));
				writer = new BufferedWriter(new FileWriter("C://Users//dan97//Desktop/test.txt"));
				writer.write(nombre);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			extraerDatos(limpiar(elemInfoPersonal), null, limpiar(elemEventos), null, null, null, null, null);
			// extraerDatos(limpiar(elemInfoPersonal),limpiar(elemFormacionAcam),
			// limpiar(elemEventos), limpiar(elemArticulos),
			// limpiar(elemLibros),limpiar(elemInformes),limpiar(elemProyectos),limpiar(elemPublicacionesN));

		} else {
			System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(url));
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);

	}

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

	public void obtenerDatos(ArrayList<String> elementos) {
		// try{
		// Investigador i = new Investigador();
		// String [] datos=extraerDatosPersonales(elementos);
		// String formacion=extraerFormacionAcademica(elementos);
		// i.setNombre(datos[0]);
		// i.setCategoria(datos[1]);
		// i.setFormacion(formacion);
		// System.out.println(i.getNombre());
		// System.out.println(i.getCategoria());
		// System.out.println(i.getFormacion());
		// }catch(NullPointerException ex){
		//
		// }
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
			extraerFormacionAcademica(formacion, investigador);
			extraerEventos(eventos, investigador);
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
		String aux= "";
		aux= StringUtils.stripAccents(investigador.getNombre());
		aux=aux.substring(1);
		ArrayList <EventoCientifico> eventoAux = new ArrayList<>();
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
			if (elementos.get(i).equalsIgnoreCase(aux)
					&& elementos.get(i + 1).contains("Rol en el evento:")) {
				rol = elementos.get(i + 2);
				eventos.setNombre(nombre);
				eventos.setTipo(tipo);
				eventos.setAmbito(ambito);
				eventos.setFecha(fecha);
				eventos.setLugar(lugar);
				eventos.setRol(rol);
				
				if (investigador.getEventos()!=null) {
					eventoAux=investigador.getEventos();
					eventoAux.add(eventos);
				}else {
					eventoAux.add(eventos);
				}
				
				investigador.setEventos(eventoAux);
			}
		}
	}

	public void obtenerArticulos() {
		// TODO
	}
	public int obtenerPosicionArray(ArrayList<String> elementos, String cadena) {
		int pos = -1;
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).contains(cadena)) {
				pos = i;
			}
		}
		return pos;
	}

	public String obtenerSubcadena(String datos, String claveInicio, String claveFin) {
		int inicio = datos.indexOf(claveInicio);
		int fin = datos.indexOf(claveFin);
		String subcadena = datos.substring(inicio + (claveInicio.length() + 1), fin);
		String nuevaCadena = subcadena.replaceAll("&nbsp;", " ");
		return nuevaCadena;
	}

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
		// for (int i = 0; i < elementosLimpio.size(); i++) {
		// System.out.println(elementosLimpio.get(i));
		// }

		return elementosLimpio;
	}

}
