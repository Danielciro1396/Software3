package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Orcid {

	public final String url = "https://orcid.org/0000-0002-6111-3055";
	public ArrayList<String> elementos = new ArrayList<String>();

	public void extraer() {
		long startTime = System.currentTimeMillis();
		if (getStatusConnectionCode(url) == 200) {
			// Obtengo el HTML de la web en un objeto Document
			Document document = getHtmlDocument(url);
			// Busco todas las entradas que estan dentro de:
			Elements entradas = document.select("script");
			System.out.println("a encontrados: " + entradas.size() + "\n");
			String nombre = "";
			for (Element elem : entradas) {
				for (DataNode node : elem.dataNodes()) {
					System.out.println(node.getWholeData());
				}
				System.err.println("-----------------------------------------");
			}

		}
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
}
