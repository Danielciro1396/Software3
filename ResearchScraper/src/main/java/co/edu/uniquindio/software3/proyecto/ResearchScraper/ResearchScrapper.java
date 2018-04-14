package co.edu.uniquindio.software3.proyecto.ResearchScraper;

/**
 * 
 *
 */
public class ResearchScrapper {

	public static void main(String[] args) {
		final String url = "http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0000438383";
		CvLac cvLac = new CvLac();
		cvLac.extraer(url);
	}
}