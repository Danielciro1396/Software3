package co.edu.uniquindio.software3.proyecto.test;

import co.edu.uniquindio.software3.proyecto.UI.VentanaReporte;

public class CvlacTest {

	public static void main(String[] args) {
		VentanaReporte vr = new VentanaReporte(
				"http://scienti.colciencias.gov.co:8081/cvlac/visualizador/generarCurriculoCv.do?cod_rh=0000438383");
		vr.setLocationRelativeTo(null);
		vr.setVisible(true);
	}
}
