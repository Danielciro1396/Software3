package co.edu.uniquindio.software3.proyecto.OrcidScraper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Orcid {
	
	// Lista en la que se guardan las direcciones url de cada investigador
		public ArrayList<String> urlSet;

	private ArrayList<Investigador> investigadores = new ArrayList<Investigador>();
	
	/**
	 * Este metodo se encarga de hacer el llamado al metodo que lee un archivo
	 * plano y carga el dataSet de url's, ademas, crea y lanza un pool de hilos
	 * para mejorar el tiempo de ejecucion del programa
	 */
	public void scrapData() {
		long startTime = System.currentTimeMillis();
		long stopTime = 0;
		long elapsedTime = 0;
		leerDataSet();
		for (int i = 0; i < urlSet.size(); i++) {

			extraer(urlSet.get(i));
		}

		stopTime = System.currentTimeMillis();
		elapsedTime = stopTime - startTime;
		System.err.println(elapsedTime);

	}

	/**
	 * Metodo que lee un archivo de texto y carga la lista con las url's de los
	 * investigadores
	 */
	public void leerDataSet() {
		try {
			urlSet = new ArrayList<String>();
			String cadena;
			FileReader f = new FileReader("src//main//java//datasets//DatasetOrcid.txt");
			BufferedReader b = new BufferedReader(f);
			while ((cadena = b.readLine()) != null) {
				
				String url = "https://orcid.org/"
						+ cadena;
				System.out.println(url);
				urlSet.add(url);
			}
			b.close();

		} catch (IOException ex) {
			ex.printStackTrace();

		}
	}

	/**
	 * Método que extrae y organiza los datos de un investigador
	 * 
	 * @param url
	 *            La url del perfil de ORCID del investigador
	 * @throws UrlOrcidInvalidaException
	 */
	public Investigador extraer(String url) {

		// Se inicializa el driver para la extracción.
		System.setProperty("webdriver.chrome.driver", "src//main//java//driver//chromedriver.exe");
		System.out.println("1");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		WebDriver driver = new ChromeDriver(options);
		driver.get(url);

		//
		String contenido = driver.getPageSource();
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("d://test.txt"));
			writer.write(contenido);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Se obtiene el nombre del investigador
		WebElement wb = driver.findElement(By.className("full-name"));
		String nombre = wb.getText();

		// Se obtienen los trabajos realizados
		// Nombre del trabajo.
		List<WebElement> nombreTrabajos = driver.findElements(By.cssSelector("span[ng-bind='work.title.value']"));

		// Revista de publicación
		List<WebElement> revistas = driver.findElements(By.className("journaltitle"));

		// Año publicación
		List<WebElement> anios = driver.findElements(By.cssSelector("span[ng-bind='work.publicationDate.year']"));

		// Tipo de publicación
		List<WebElement> tipos = driver.findElements(By.cssSelector("span[ng-bind='work.workType.value']"));

		ArrayList<Produccion> producciones = new ArrayList<Produccion>();

		for (int i = 0; i < nombreTrabajos.size(); i++) {
			String name = nombreTrabajos.get(i).getText();
			String revista = revistas.get(i).getText();
			String anio = anios.get(i).getText();
			String tipo = tipos.get(i).getText();

			Produccion prod = new Produccion(name, revista, anio, tipo);
			producciones.add(prod);
		}

		Investigador inv = new Investigador(nombre, producciones);
		investigadores.add(inv);

		setInvestigadores(investigadores);

		driver.close();
		System.out.println("2");
		driver.quit();

		return inv;

	}

	public ArrayList<Investigador> getListaInvestigadores() {
		return investigadores;
	}

	public void setInvestigadores(ArrayList<Investigador> investigadores) {
		this.investigadores = investigadores;
	}

}
