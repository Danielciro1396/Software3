package co.edu.uniquindio.software3.proyecto.GrupLacScraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import co.edu.uniquindio.software3.proyecto.CvLacScraper.Investigador;

public class GrupLac {

	ArrayList<String> urlSet;

	 String titulo;

	 List<Grupo> grupos = Collections.synchronizedList(new ArrayList<Grupo>());
	

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

				// nombre += elem.toString() + "\n";
				// BufferedWriter writer = null;
				// try {
				// writer = new BufferedWriter(new FileWriter("d://test.txt"));
				// writer.write(nombre);
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
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
//		 for (int i = 0; i < elementosLimpio.size(); i++) {
//		 System.out.println(elementosLimpio.get(i));
//		 }
		return elementosLimpio;
	}

	/**
	 * 
	 * @param datosBasicos
	 * @param articulos
	 * @param eventos
	 * @param informes
	 * @param innovaciones
	 * @param libros
	 * @param softwares
	 */
	public void extraerDatos(ArrayList<String> datosBasicos, ArrayList<String> articulos, ArrayList<String> eventos,
			ArrayList<String> informes, ArrayList<String> innovaciones, ArrayList<String> libros,
			ArrayList<String> softwares) {
		Grupo grupo = new Grupo();
		try {
			
		} catch (NullPointerException e) {

		}
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
		ArrayList<String> aux = new ArrayList<>();
		String anio;
		String lider;
		String clasificacion;
		String area = "";
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).equals("AÑO Y MES DE FORMACIÓN")) {
				anio = elementos.get(i + 1);
				grupo.setAñoFundación(anio);
			} else if (elementos.get(i).equals("LÍDER")) {
				lider = elementos.get(i + 1);
				grupo.setLider(lider);
			} else if (elementos.get(i).equals("CLASIFICACIÓN")) {
				 clasificacion = elementos.get(i + 1);
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
	public void extraerEventos(ArrayList<String> elementos,Grupo grupo) {
		String nombre = "";
		String tipo = "";
		String ambito = "";
		String fecha = "";
		String lugar = "";
		String tipoParticipacion = "";
		ArrayList<EventoCientifico> auxEvento = new ArrayList<>();
		EventoCientifico evento = new EventoCientifico();
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).contains(".-")) {
				tipo = elementos.get(i + 1);
			}
			if (elementos.get(i).contains(".-")) {
				nombre = elementos.get(i + 2).substring(2);
			}
			if (elementos.get(i).contains("DESDE")) {
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				int posF = 0;
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						posF = j;
						lugar = cadena.substring(0, posF);
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
						ambito = cadena.substring(posI, posF);
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
						posI = j + 3;
						tipoParticipacion = cadena.substring(posI);
						break;
					}
				}
			}
		}
		evento.setTipo(tipo);
		evento.setNombre(nombre);
		evento.setLugar(lugar);
		evento.setFecha(fecha);
		evento.setAmbito(ambito);
		evento.setTipoParticipacion(tipoParticipacion);
		auxEvento.add(evento);
		grupo.setEventos(auxEvento);


	}

	/**
	 * Metodo para la extraccion de los articulos de un grupo
	 * 
	 * @param elementos
	 *            arreglo que contiene lo articuos
	 */
	public void extraerArticulos(ArrayList<String> elementos, Grupo grupo) {
		String autores ="";
		String titulo="";
		String lugar="";
		String nomRevista="";
		String anio="";
		String tipo="";
		ArrayList<Articulo> auxArticulos = new ArrayList<>();
		Articulo articulo = new Articulo();
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).contains(".-")) {
				titulo = elementos.get(i + 2);
			}
			if (elementos.get(i).contains(".-")) {
				tipo = elementos.get(i + 1).replaceAll(":", "");
			}
			if (elementos.get(i).contains("AUTORES:")) {
				String cadena = elementos.get(i);
				int posI = 0;
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ':') {
						posI = j + 2;
						autores = cadena.substring(posI);
					}
				}
			}
			
			if (elementos.get(i).contains("ISSN")) {
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				int posF = 0;
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						posF = j;
						lugar = cadena.substring(0, posF);
						j = posF;
						break;
					}
				}

			}
			if (elementos.get(i).contains("ISSN")) {
				String cadena = elementos.get(i);
				int posI = 0;
				int posF = 0;
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						posI = j + 2;
					}
					if (aux[j] == 'I' && aux[j + 1] == 'S' && aux[j + 2] == 'S') {
						posF = j - 1;
						nomRevista = cadena.substring(posI, posF);
						break;
					}
				}

			}
			if (elementos.get(i).contains("VOL")) {
				String cadena = elementos.get(i);
				int posI = 0;
				int posF = 0;
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == ',') {
						posI = j + 2;
					}
					if (aux[j] == 'V' && aux[j + 1] == 'O' && aux[j + 2] == 'L') {
						posF = j - 1;
						anio = cadena.substring(posI, posF);
						break;
					}
				}

			}
		}
		articulo.setAnio(anio);
		articulo.setAutores(autores);
		articulo.setLugar(lugar);
		articulo.setNomRevista(nomRevista);
		articulo.setTipo(tipo);
		articulo.setTitulo(titulo);
		auxArticulos.add(articulo);
		grupo.setArticulos(auxArticulos);
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
		String autores="";
		String titulo="";
		String lugar="";
		String anio="";
		String editorial="";
		ArrayList<Libro> auxLib = new ArrayList<>();
		Libro libro = new Libro();
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).contains(".-")) {
				titulo = elementos.get(i + 2).substring(2);
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
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == 'E' && aux[j + 1] == 'D' && aux[j + 2] == '.') {
						posI = j + 4;
						editorial = cadena.substring(posI);
						break;
					}
				}

			}
			if (elementos.get(i).contains("AUTORES:")) {
				int posI = 0;
				String cadena = elementos.get(i);
				char[] aux = cadena.toCharArray();
				for (int j = 0; j < aux.length; j++) {
					if (aux[j] == 'E' && aux[j + 1] == 'S' && aux[j + 2] == ':') {
						posI = j + 4;
						autores = cadena.substring(posI);
						break;
					}
				}
			}
		}
		libro.setAnio(anio);
		libro.setAutores(autores);
		libro.setEditorial(editorial);
		libro.setLugar(lugar);
		libro.setTitulo(titulo);
		auxLib.add(libro);
		grupo.setLibros(auxLib);
	}

	public void extraerProyectos(ArrayList<String> elementos, Grupo grupo) {
		String tipo="";
		String nombre="";
		String fecha="";
		ArrayList<Proyecto> auxPro = new ArrayList<>();
		Proyecto proyecto = new Proyecto();
		for (int i = 0; i < elementos.size(); i++) {
			if (elementos.get(i).contains(".-")) {
				tipo = elementos.get(i + 1);
			}
			if (elementos.get(i).contains(".-")) {
				nombre = elementos.get(i + 2).substring(2);
			}
			if (elementos.get(i).contains(".-")) {
				if (elementos.get(i + 3).contains(".-")) {
					fecha = "N/D";
				} else {
					fecha = elementos.get(i + 3);
				}
			}

		}
		proyecto.setFecha(fecha);
		proyecto.setNombre(nombre);
		proyecto.setTipo(tipo);
		auxPro.add(proyecto);
		grupo.setProyectos(auxPro);
	}

	public void extraerIntegrantes(ArrayList<String> elementos, Grupo grupo) {
		String nombre;
		ArrayList<String> aux = new ArrayList<>();
		ArrayList<Investigador> auxInvestigador = new ArrayList<>();
		Investigador investigador = new Investigador();
		for (int i = 0; i < elementos.size(); i++) {
			if(elementos.get(i).contains(".-")&&elementos.get(i+4).contains("ACTUAL")) {
				nombre=elementos.get(i+1);
				aux.add(nombre);
			}
		}
		

	}
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
}
