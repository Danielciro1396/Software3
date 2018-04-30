package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import co.edu.uniquindio.software3.proyecto.CvLacScraper.CvLac;
import co.edu.uniquindio.software3.proyecto.GrupLacScraper.GrupLac;
import co.edu.uniquindio.software3.proyecto.OrcidScraper.Orcid;

/**
 * 
 *
 */
public class ResearchScrapper {

	public static void main(String[] args) {
		CvLac cv = new CvLac();
		GrupLac grp = new GrupLac();
		Orcid o = new Orcid();

		// cv.scrapData();
		// o.extraer("https://orcid.org/0000-0002-7021-099X");

		cv.extraer("http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0000947610");

//		grp.extraer("http://scienti.colciencias.gov.co:8085/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000002591");

		 //cv.scrapData();

		// cv.extraer("http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0001336346");
	}
}