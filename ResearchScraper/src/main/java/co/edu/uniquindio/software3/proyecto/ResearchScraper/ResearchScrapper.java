package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import co.edu.uniquindio.software3.proyecto.test.CvlacTest;

/**
 * 
 *
 */
public class ResearchScrapper {
	
	public static void main(String[] args) {
		CvLac cv = new CvLac();
		cv.scrapData();
		//cv.extraer("http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0001336946");
	}
}