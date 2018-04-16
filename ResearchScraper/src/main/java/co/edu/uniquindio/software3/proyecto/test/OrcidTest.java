package co.edu.uniquindio.software3.proyecto.test;

import co.edu.uniquindio.software3.proyecto.UI.VentanaReporteOrcid;

public class OrcidTest {

	public static void main(String[] args) {
		VentanaReporteOrcid vr = new VentanaReporteOrcid("https://orcid.org/0000-0002-6111-3055");
		vr.setLocationRelativeTo(null);
		vr.setVisible(true);
	}
}
