package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 *
 */
public class ResearchScrapper {

	public static final String url = "http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0000438383";
	public static ArrayList<String> elementos = new ArrayList<String>();

	public static void main(String[] args) {
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

				if (elem.text().contains("Nombre en citaciones") || elem.text().contains("Formación Académica")
						|| elem.text().contains("Eventos científicos") || elem.text().contains("Artículos")
						|| elem.text().contains("Libros") || elem.text().contains("Informes de investigación")
						|| elem.text().contains("Proyectos")||elem.text().contains("Textos en publicaciones no científicas")) {
					nombre += elem.text() + "\n";
					elementos.add(elem.text());
				}

			}

			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter("d://test.txt"));
				writer.write(nombre);
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			obtenerDatos(elementos);

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
	 * 
	 * @param url,
	 *            el enlace de la página web a analizar.
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
	
	public static void obtenerDatos(ArrayList<String>elementos){
		try{
		Investigador i = new Investigador();
		String [] datos=extraerDatosPersonales(elementos);
		String formacion=extraerFormacionAcademica(elementos);
		i.setNombre(datos[0]);
		i.setCategoria(datos[1]);
		i.setFormacion(formacion);
		System.out.println(i.getNombre());
		System.out.println(i.getCategoria());
		System.out.println(i.getFormacion());
		}catch(NullPointerException ex){
			
		}
	}
	
	public static String[] extraerDatosPersonales(ArrayList<String>elementos){
		String categoria="";
		String nombre="";		
		try{
		int pos=obtenerPosicionArray(elementos, "Nombre en citaciones");
		String datos=elementos.get(pos);	
		if(datos.contains("Categoría")){
			categoria=obtenerSubcadena(datos, "Categoría", "Nombre");		
		}else{
			categoria="Sin Categoría";
		}		
		nombre=obtenerSubcadena(datos, "Nombre", "Nombre en citaciones");
		
		String valores[] = {nombre, categoria};
		return valores;

		}catch(ArrayIndexOutOfBoundsException ex){
			return null;
		}
		
	}
	
	public static String extraerFormacionAcademica(ArrayList<String>elementos){
		
		try{
			String clave="Formación Académica";
			int pos=obtenerPosicionArray(elementos, clave);
			String datos=elementos.get(pos);
			int fin=datos.indexOf(clave)+clave.length();
			
			int inicio=0;
			for (int i=fin+1; i < datos.length(); i++) {
				if(datos.charAt(i+1)!=' '){
					inicio=i+1;
					break;
				}
			}
			
			for (int i=inicio; i < datos.length(); i++) {
				if(datos.charAt(i+1)==' '){
					fin=i+1;
					break;
				}
			}
			return datos.substring(inicio, fin);
			
		}catch(ArrayIndexOutOfBoundsException ex){
			return null;
		}
		
	}
	
	
	public static int obtenerPosicionArray(ArrayList<String>elementos, String cadena){
		int pos=-1;
		for (int i = 0; i < elementos.size(); i++) {
			if(elementos.get(i).contains(cadena)){
				pos=i;			
		}
		}
			return pos;
	}
	
	public static String obtenerSubcadena (String datos, String claveInicio, String claveFin){
		int inicio=datos.indexOf(claveInicio);
		int fin=datos.indexOf(claveFin);
		String subcadena=datos.substring(inicio+(claveInicio.length()+1), fin);
		return subcadena;
	}

}
