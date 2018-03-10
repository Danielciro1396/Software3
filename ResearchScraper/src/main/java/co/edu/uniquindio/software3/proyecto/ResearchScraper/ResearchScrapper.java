package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import java.io.IOException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Documentar esta mierda papu
 *
 */
public class ResearchScrapper {
	
	public static final String url ="http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0001383939";
	
	public static void main(String[] args) {
		if(getStatusConnectionCode(url)==200){
			// Obtengo el HTML de la web en un objeto Document
            Document document = getHtmlDocument(url);
			
            // Busco todas las entradas que estan dentro de: 
            Elements entradas = document.select("table");
            System.out.println("TR encontrados: "+entradas.size()+"\n");
			
            // Paseo cada una de las entradas
            for (Element elem : entradas) {
                String nombre = elem.getElementsByClass("Nombre").text();
               
				
                System.out.println(nombre);
				
                // Con el método "text()" obtengo el contenido que hay dentro de las etiquetas HTML
                // Con el método "toString()" obtengo todo el HTML con etiquetas incluidas
            }
				
        }else{
            System.out.println("El Status Code no es OK es: "+getStatusConnectionCode(url));
        }
    }

			
		
	
	
	
	/**
	 * Método provisto por JSoup para comprobar el Status code de la respuesta que recibo al hacer la petición
	 * Codigos:
	 * 		200 OK			
	 * 		300 Multiple Choices
	 * 		301 Moved Permanently	
	 * 		305 Use Proxy
	 * 		400 Bad Request		
	 * 		403 Forbidden
	 * 		404 Not Found		
	 * 		500 Internal Server Error
	 * 		502 Bad Gateway		
	 * 		503 Service Unavailable
	 * @param url, el enlace de la página web a analizar.
	 * @return Status Code, el código que identifica el estado de la página.
	 */
	public static int getStatusConnectionCode(String url) {
			
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
	 * @param url, el enlace de la página web a analizar.
	 * @return Documento con el HTML de la página en cuestión.
	 */
	public static Document getHtmlDocument(String url) {

	    Document doc = null;
		try {
		    doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		    } catch (IOException ex) {
			System.out.println("Excepción al obtener el HTML de la página" + ex.getMessage());
		    }
	    return doc;
	}
	
	
}
