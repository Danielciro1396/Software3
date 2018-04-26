package co.edu.uniquindio.software3.proyecto.ResearchScraper;

public class ArrayThread implements Runnable {

	public String url;
	public int inicio;
	CvLac cvLac;

	public ArrayThread(String url, int inicio, CvLac cvLac) {
		this.url = url;
		this.inicio = inicio;
		this.cvLac = cvLac;
	}

	/**
	 * Metodo que ejecuta cada hilo, y hace un llamado a las clases que hacen la
	 * estraccion de los datos
	 */
	@Override
	public void run() {

		cvLac.extraer(url);

		System.out.println(inicio + " " + url);
	}

}
