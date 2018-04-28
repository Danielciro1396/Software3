package co.edu.uniquindio.software3.proyecto.OrcidScraper;

import java.io.BufferedWriter;
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

	private ArrayList<Investigador> investigadores = new ArrayList<Investigador>();

	/**
	 * Método que extrae y organiza los datos de un investigador
	 * 
	 * @param url
	 *            La url del perfil de ORCID del investigador
	 * @throws UrlOrcidInvalidaException
	 */
	public Investigador scrapData(String url) {

		// Se inicializa el driver para la extracción.
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		WebDriver driver = new ChromeDriver(options);
		org.openqa.selenium.Point point = new org.openqa.selenium.Point(-10000, 0);
		driver.manage().window().setPosition(point);
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
