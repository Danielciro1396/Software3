package co.edu.uniquindio.software3.proyecto.ResearchScraper;

/**
 * 
 *
 */
public class ResearchScrapper {
	
	public static void main(String[] args) {
		CvLac cv = new CvLac();
		cv.scrapData();
//		cv.extraer("http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0000438383");
//		cv.extraer("http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0001398775");
	}
}