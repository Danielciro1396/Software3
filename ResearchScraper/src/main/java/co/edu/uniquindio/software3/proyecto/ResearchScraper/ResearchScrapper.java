package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import co.edu.uniquindio.software3.proyecto.GrupLacScraper.GrupLac;

/**
 * 
 *
 */
public class ResearchScrapper {

	public static void main(String[] args) {
		GrupLac grp = new GrupLac();
//		Orcid o = new Orcid();

		//grp.scrapData();
		grp.extraerReferencia("http://scienti.colciencias.gov.co:8085/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000005918");
		
//		grp.extraer("http://scienti.colciencias.gov.co:8085/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000006997");
		//grp.extraer("http://scienti.colciencias.gov.co:8085/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000009219");
	

	
		
		
	}
}