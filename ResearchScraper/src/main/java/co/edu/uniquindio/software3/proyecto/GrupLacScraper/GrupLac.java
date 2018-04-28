package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GrupLac {

	public ArrayList<String> urlSet;

	public ArrayList<String> elemInformacion = new ArrayList<>();

	public ArrayList<String> elemEventos = new ArrayList<>();

	public ArrayList<String> elemArticulos = new ArrayList<>();

	public ArrayList<String> elemLibros = new ArrayList<>();

	public ArrayList<String> elemCapLibros = new ArrayList<>();

	public ArrayList<String> elemInformes = new ArrayList<>();

	public ArrayList<String> softwares = new ArrayList<>();

	public ArrayList<String> innovaciones = new ArrayList<>();

	public ArrayList<String> proyectos = new ArrayList<>();

	public List<Grupo> grupos = Collections.synchronizedList(new ArrayList<Grupo>());

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
	 * hacer el llamado al metodo que asigna los datos a cada grupo
	 * 
	 * @param url,
	 *            direccion url de un investigador
	 */
	public void extraer(String url) {

		if (getStatusConnectionCode(url) == 200) {
			// Obtengo el HTML de la web en un objeto Document
			Document document = getHtmlDocument(url);
			// Busca todas las coincidencias que estan dentro de
			Elements entradas = document.select("body");

			String nombre = "";

			for (Element elem : entradas) {

				nombre += elem.toString() + "\n";
				BufferedWriter writer = null;
				try {
					writer = new BufferedWriter(new FileWriter("d://test.txt"));
					writer.write(nombre);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// if (elem.text().contains("Nombre en citaciones")) {
				// elemInformacion.add(elem.toString());
				// }
				// if (elem.text().contains("Formación Académica")) {
				// elemCapLibros.add(elem.toString());
				// }
				// if (elem.text().contains("Eventos científicos")) {
				// elemEventos.add(elem.toString());
				// }
				// if (elem.text().contains("Artículos")) {
				// elemArticulos.add(elem.toString());
				// }
				// if (elem.text().contains("Libros")) {
				// elemLibros.add(elem.toString());
				// }
				// if (elem.text().contains("Informes de investigación")) {
				// elemInformes.add(elem.toString());
				// }
				// if (elem.text().contains("Proyectos")) {
				// softwares.add(elem.toString());
				// }
				// if (elem.text().contains("Textos en publicaciones no
				// científicas")) {
				// innovaciones.add(elem.toString());
				// }
				// if (elem.text().contains("Textos en publicaciones no
				// científicas")) {
				// proyectos.add(elem.toString());
				// }
			}

		} else {
			System.out.println("El Status Code no es OK es: " + getStatusConnectionCode(url));
		}

	}
}
