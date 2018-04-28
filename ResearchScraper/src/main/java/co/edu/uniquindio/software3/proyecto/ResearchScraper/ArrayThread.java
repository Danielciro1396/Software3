package co.edu.uniquindio.software3.proyecto.ResearchScraper;

import co.edu.uniquindio.software3.proyecto.CvLacScraper.CvLac;
import co.edu.uniquindio.software3.proyecto.GrupLacScraper.GrupLac;
import co.edu.uniquindio.software3.proyecto.OrcidScraper.Orcid;

public class ArrayThread implements Runnable {

	public String url;
	public int inicio;
	GrupLac grupLac;
	CvLac cvLac;
	Orcid orcid;
	public int bandera;

	public ArrayThread(String url, int inicio, CvLac cvLac, int bandera, GrupLac grupLac, Orcid orcid) {
		this.url = url;
		this.inicio = inicio;
		this.cvLac = cvLac;
		this.bandera = bandera;
		this.grupLac = grupLac;
		this.orcid = orcid;
	}

	/**
	 * Metodo que ejecuta cada hilo, y hace un llamado a las clases que hacen la
	 * estraccion de los datos
	 */
	@Override
	public void run() {
		if (bandera == 0) {
			cvLac.extraer(url);

			System.out.println(inicio + " " + url);
		} else if (bandera == 1) {
//			grupLac.extraer(url);
//
//			System.out.println(inicio + " " + url);
		}else if(bandera == 2){
			orcid.scrapData(url);

			System.out.println(inicio + " " + url);
		}

	}

}
